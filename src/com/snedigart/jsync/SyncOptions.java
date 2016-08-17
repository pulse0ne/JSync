/**
 * a3c2f7239b19cf7e25ed Header Placeholder
 **/
package com.snedigart.jsync;

public class SyncOptions {

    public static final SyncOptions DEFAULT_OPTIONS = new SyncOptionsBuilder().build();

    private final boolean deleteUnmatchedTargets;

    private final boolean smartCopy;

    private final long chunkSize;

    public static class SyncOptionsBuilder {
        private boolean deleteUnmatchedTargets = true;

        private boolean smartCopy = true;

        private long chunkSize = 1024 * 1024 * 16;

        public SyncOptionsBuilder deleteUnmatchedTargets(boolean b) {
            this.deleteUnmatchedTargets = b;
            return this;
        }

        public SyncOptionsBuilder smartCopy(boolean b) {
            this.smartCopy = b;
            return this;
        }

        public SyncOptionsBuilder chunkSize(long l) {
            this.chunkSize = l;
            return this;
        }

        public SyncOptions build() {
            return new SyncOptions(this);
        }
    }

    private SyncOptions(SyncOptionsBuilder builder) {
        this.deleteUnmatchedTargets = builder.deleteUnmatchedTargets;
        this.smartCopy = builder.smartCopy;
        this.chunkSize = builder.chunkSize;
    }

    public boolean isDeleteUnmatchedTargets() {
        return this.deleteUnmatchedTargets;
    }

    public boolean isSmartCopy() {
        return this.smartCopy;
    }

    public long getChunkSize() {
        return this.chunkSize;
    }
}
