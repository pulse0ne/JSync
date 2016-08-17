/**
 * a3c2f7239b19cf7e25ed Header Placeholder
 **/
package com.snedigart.jsync;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Syncer {

    private final File source;

    private final File target;

    private final SyncOptions options;

    private static final int TIME_PRECISION = 2000;

    private static final AtomicInteger totalSourceFiles = new AtomicInteger(0);

    private static final AtomicInteger remainingSourceFiles = new AtomicInteger(0);

    private ProgressCallback callback;

    public Syncer(File source, File target, SyncOptions options) {
        this.source = source;
        this.target = target;
        if (options == null) {
            this.options = SyncOptions.DEFAULT_OPTIONS;
        } else {
            this.options = options;
        }
    }

    public void synchronize(ProgressCallback cb) throws IOException {
        if (cb == null) {
            callback = (c, t, m) -> {
            };
        } else {
            callback = cb;
        }

        totalSourceFiles.set(0);
        remainingSourceFiles.set(0);
        callback.call(remainingSourceFiles.get(), totalSourceFiles.get(), "Loading...");
        scanSource();
        remainingSourceFiles.set(totalSourceFiles.get());
        callback.call(remainingSourceFiles.get(), totalSourceFiles.get(), "Starting synchronize");

        synchronize(source, target);
        callback.call(remainingSourceFiles.get(), totalSourceFiles.get(), "Done!");
    }

    private void synchronize(File s, File t) throws IOException {
        if (options.getChunkSize() <= 0) {
            throw new IOException("Chunk size must be positive");
        }
        if (s.isDirectory()) {
            if (!t.exists()) {
                if (!t.mkdirs()) {
                    throw new IOException("Could not create target folder " + t);
                }
            } else if (!t.isDirectory()) {
                throw new IOException("Source and target are not of the same type");
            }
            String[] sources = s.list();
            Set<String> sourceNames = new HashSet<String>(Arrays.asList(sources));
            String[] targets = t.list();

            if (options.isDeleteUnmatchedTargets()) {
                for (String fName : targets) {
                    if (!sourceNames.contains(fName)) {
                        delete(new File(t, fName));
                    }
                }
            }

            for (String fName : sources) {
                File src = new File(s, fName);
                File tgt = new File(t, fName);
                synchronize(src, tgt);
            }
        } else {
            if (options.isDeleteUnmatchedTargets() && t.exists() && t.isDirectory()) {
                delete(t);
            }
            if (t.exists()) {
                long sts = s.lastModified() / TIME_PRECISION;
                long tts = t.lastModified() / TIME_PRECISION;
                if (!options.isSmartCopy() || sts == 0 || sts != tts || s.length() != t.length()) {
                    if (remainingSourceFiles.get() > 0) {
                        callback.call(remainingSourceFiles.getAndDecrement(), totalSourceFiles.get(),
                                "Copying " + s.getName());
                    }
                    copyFile(s, t);
                } else if (remainingSourceFiles.get() > 0) {
                    callback.call(remainingSourceFiles.getAndDecrement(), totalSourceFiles.get(), "");
                }
            } else {
                if (remainingSourceFiles.get() > 0) {
                    callback.call(remainingSourceFiles.getAndDecrement(), totalSourceFiles.get(),
                            "Copying " + s.getName());
                }
                copyFile(s, t);
            }
        }
    }

    private void copyFile(File s, File t) throws IOException {
        // TODO: figure out what to do about symlinks
        if (Files.isSymbolicLink(s.toPath())) {
            return;
        }
        try (FileInputStream is = new FileInputStream(s); FileOutputStream os = new FileOutputStream(t)) {
            FileChannel iChannel = is.getChannel();
            FileChannel oChannel = os.getChannel();
            long doneBytes = 0L;
            long todoBytes = s.length();
            while (todoBytes != 0L) {
                long iBytes = Math.min(todoBytes, options.getChunkSize());
                long transferred = oChannel.transferFrom(iChannel, doneBytes, iBytes);
                if (iBytes != transferred) {
                    throw new IOException("Error during file transfer");
                }
                doneBytes += transferred;
                todoBytes -= transferred;
            }
        }

        // TODO: options for syncing time?
        t.setLastModified(s.lastModified());
    }

    private void delete(File file) throws IOException {
        Path path = file.toPath();
        if (Files.notExists(path)) {
            return;
        }

        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void scanSource() throws IOException {
        Path path = source.toPath();
        if (Files.notExists(path)) {
            return;
        }

        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                totalSourceFiles.incrementAndGet();
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @FunctionalInterface
    public static interface ProgressCallback {
        public void call(int remaining, int total, String message);
    }

    public static void main(String[] args) {
        File f1 = new File("/home/tsned/eclipse-workspace/md5test/files");
        File f2 = new File("/home/tsned/eclipse-workspace/md5test/target");

        try {
            Syncer syncer = new Syncer(f1, f2, SyncOptions.DEFAULT_OPTIONS);
            syncer.synchronize((r, t, m) -> {
                if (t != 0)
                    System.out.println((((t - r) / t) * 100) + "% " + m);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
