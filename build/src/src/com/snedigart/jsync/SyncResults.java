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

import java.util.concurrent.TimeUnit;

/**
 * The immutable SyncResults object gets returned after a successful sync with
 * statistic about the synchronization
 * 
 * @author Tyler Snedigar
 * @version 1.0
 */
public final class SyncResults {

    private final int numFilesScanned;

    private final int numFilesDeleted;

    private final int numFilesCopied;

    private final int numFilesFiltered;

    private final long scanTimeNanos;

    private final long totalTimeNanos;

    // private for builder pattern
    private SyncResults(SyncResultsBuilder builder) {
        this.numFilesScanned = builder.numFilesScanned;
        this.numFilesDeleted = builder.numFilesDeleted;
        this.numFilesCopied = builder.numFilesCopied;
        this.numFilesFiltered = builder.numFilesFiltered;
        this.scanTimeNanos = builder.scanTimeNanos;
        this.totalTimeNanos = builder.totalTimeNanos;
    }

    /**
     * @return the numFilesScanned
     */
    public int getNumFilesScanned() {
        return numFilesScanned;
    }

    /**
     * @return the numFilesDeleted
     */
    public int getNumFilesDeleted() {
        return numFilesDeleted;
    }

    /**
     * @return the numFilesCopied
     */
    public int getNumFilesCopied() {
        return numFilesCopied;
    }

    /**
     * @return the numFilesFiltered
     */
    public int getNumFilesFiltered() {
        return numFilesFiltered;
    }

    /**
     * @return the scanTimeNanos
     */
    public long getScanTimeNanos() {
        return scanTimeNanos;
    }

    /**
     * @return the copy time
     */
    public long getCopyTimeNanos() {
        return totalTimeNanos - scanTimeNanos;
    }

    /**
     * @return the totalTimeNanos
     */
    public long getTotalTimeNanos() {
        return totalTimeNanos;
    }

    /**
     * Returns a textual representation of the results
     * 
     * @return string
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("Scanned    : ").append(numFilesScanned).append(System.lineSeparator());
        b.append("Filtered   : ").append(numFilesFiltered).append(System.lineSeparator());
        b.append("Copied     : ").append(numFilesCopied).append(System.lineSeparator());
        b.append("Deleted    : ").append(numFilesDeleted).append(System.lineSeparator());
        b.append("Scan Time  : ").append(getTimeString(scanTimeNanos)).append(System.lineSeparator());
        b.append("Copy Time  : ").append(getTimeString(getCopyTimeNanos())).append(System.lineSeparator());
        b.append("Total Time : ").append(getTimeString(totalTimeNanos)).append(System.lineSeparator());
        return b.toString();
    }

    private String getTimeString(long nanos) {
        long remaining = nanos;
        long hours = TimeUnit.NANOSECONDS.toHours(remaining);
        remaining -= TimeUnit.HOURS.toNanos(hours);
        long mins = TimeUnit.NANOSECONDS.toMinutes(remaining);
        remaining -= TimeUnit.MINUTES.toNanos(mins);
        long secs = TimeUnit.NANOSECONDS.toSeconds(remaining);
        remaining -= TimeUnit.SECONDS.toNanos(secs);
        long mils = TimeUnit.NANOSECONDS.toMillis(remaining);
        remaining -= TimeUnit.MILLISECONDS.toNanos(mils);
        long micr = TimeUnit.NANOSECONDS.toMicros(remaining);
        remaining -= TimeUnit.MICROSECONDS.toNanos(micr);

        StringBuilder builder = new StringBuilder();
        if (hours > 0) {
            builder.append(hours).append("h ");
        }
        if (mins > 0) {
            builder.append(mins).append("m ");
        }
        if (secs > 0) {
            builder.append(secs).append("s ");
        }
        if (mils > 0) {
            builder.append(mils).append("ms ");
        }
        if (micr > 0) {
            builder.append(micr).append("µs ");
        }
        builder.append(remaining).append("ns");

        return builder.toString();
    }

    /**
     * Builder class for SyncResults
     * 
     * @author Tyler Snedigar
     * @version 1.0
     */
    public static class SyncResultsBuilder {

        private int numFilesScanned = 0;

        private int numFilesDeleted = 0;

        private int numFilesCopied = 0;

        private int numFilesFiltered = 0;

        private long scanTimeNanos = 0L;

        private long totalTimeNanos = 0L;

        /**
         * Builds and returns a new SyncResults object
         * 
         * @return SyncResults
         */
        public SyncResults build() {
            return new SyncResults(this);
        }

        /**
         * @param n
         *            numFilesScanned
         * @return SyncResultsBuilder
         */
        public SyncResultsBuilder filesScanned(int n) {
            numFilesScanned = n;
            return this;
        }

        /**
         * @param n
         *            numFilesDeleted
         * @return SyncResultsBuilder
         */
        public SyncResultsBuilder filesDeleted(int n) {
            numFilesDeleted = n;
            return this;
        }

        /**
         * @param n
         *            numFilesCopied
         * @return SyncResultsBuilder
         */
        public SyncResultsBuilder filesCopied(int n) {
            numFilesCopied = n;
            return this;
        }

        /**
         * @param n
         *            numFilesFiltered
         * @return SyncResultsBuilder
         */
        public SyncResultsBuilder filesFiltered(int n) {
            numFilesFiltered = n;
            return this;
        }

        /**
         * @param ns
         *            time in nanoseconds
         * @return SyncResultsBuilder
         */
        public SyncResultsBuilder scanTimeNanos(long ns) {
            scanTimeNanos = ns;
            return this;
        }

        /**
         * @param ns
         *            time in nanoseconds
         * @return SyncResultsBuilder
         */
        public SyncResultsBuilder totalTimeNanos(long ns) {
            totalTimeNanos = ns;
            return this;
        }
    }
}
