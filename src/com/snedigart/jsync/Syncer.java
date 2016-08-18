/*
 * Copyright 2016 Tyler Snedigar.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details.
 */
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

/**
 * The Syncer class provides a convenient and fast way of syncing two
 * directories
 * 
 * @author Tyler Snedigar
 * @version 1.0
 */
public final class Syncer {

    private final File source;

    private final File target;

    private final SyncOptions options;

    private static final int TIME_PRECISION = 2000;

    private int totalSourceFiles = 0;

    private int remainingSourceFiles = 0;

    private int filesDeleted = 0;

    private int filesCopied = 0;

    private ProgressCallback callback;

    private SyncResults.SyncResultsBuilder results;

    /**
     * Constructor. Creates a new Syncer object and initializes it. If options
     * are null, a set of defaults will be used.
     * 
     * @param source
     *            directory
     * @param target
     *            directory
     * @param options
     *            sync options
     */
    public Syncer(File source, File target, SyncOptions options) {
        this.source = source;
        this.target = target;
        if (options == null) {
            this.options = SyncOptions.DEFAULT_OPTIONS;
        } else {
            this.options = options;
        }
    }

    /**
     * This method performs the synchronization
     * 
     * @param cb
     *            callback that gets called periodically with progress info
     * @return results of the sync
     * @throws IOException
     *             thrown on IO errors
     */
    public SyncResults synchronize(ProgressCallback cb) throws IOException {
        if (cb == null) {
            callback = (c, t, m) -> {
            };
        } else {
            callback = cb;
        }

        totalSourceFiles = remainingSourceFiles = filesCopied = filesDeleted = 0;
        callback.call(remainingSourceFiles, totalSourceFiles, "Loading...");

        long start = System.nanoTime();
        scanSource();
        results.filesScanned(totalSourceFiles).scanTimeNanos(System.nanoTime() - start);

        remainingSourceFiles = totalSourceFiles;
        callback.call(remainingSourceFiles, totalSourceFiles, "Starting synchronize");
        synchronize(source, target);

        results.totalTimeNanos(System.nanoTime() - start);
        callback.call(remainingSourceFiles, totalSourceFiles, "Done!");

        results.filesCopied(filesCopied).filesDeleted(filesDeleted);

        return results.build();
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
                    if (remainingSourceFiles > 0) {
                        callback.call(remainingSourceFiles--, totalSourceFiles, "Copying " + s.getName());
                    }
                    copyFile(s, t);
                } else if (remainingSourceFiles > 0) {
                    callback.call(remainingSourceFiles--, totalSourceFiles, "");
                }
            } else {
                if (remainingSourceFiles > 0) {
                    callback.call(remainingSourceFiles--, totalSourceFiles, "Copying " + s.getName());
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

            filesCopied++;
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
                filesDeleted++;
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
                totalSourceFiles++;
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * This functional interface describes a callback that will be called with
     * progress information.
     * 
     * @author Tyler Snedigar
     * @version 1.0
     */
    @FunctionalInterface
    public static interface ProgressCallback {
        /**
         * The remaining and total values will be 0 when syncing starts
         * (important to note that calculations will have a divide by zero
         * error). They will go to -1 if an error occurs, and the message will
         * explain the error.
         * 
         * @param remaining
         *            files to be processed
         * @param total
         *            files being processed
         * @param message
         *            the message
         */
        public void call(int remaining, int total, String message);
    }
}
