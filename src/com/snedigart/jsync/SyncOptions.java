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

/**
 * The SyncOptions class defines the options that will be used by Syncer.
 * Example use: SyncOptions opts = new
 * SyncOptionsBuilder().deleteUnmatchedTargets(true).smartCopy(true).chunkSize(
 * 1024).build();
 *
 * @author Tyler Snedigar
 * @version 1.0
 */
public final class SyncOptions {

    public static final SyncOptions DEFAULT_OPTIONS = new SyncOptionsBuilder().build();

    private final boolean deleteUnmatchedTargets;

    private final boolean smartCopy;

    private final long chunkSize;

    // private for builder pattern
    private SyncOptions(SyncOptionsBuilder builder) {
        this.deleteUnmatchedTargets = builder.deleteUnmatchedTargets;
        this.smartCopy = builder.smartCopy;
        this.chunkSize = builder.chunkSize;
    }

    /**
     * Returns the option of whether or not to delete target files/directories
     * that do not correspond to a source file/directory
     * 
     * @return boolean
     */
    public boolean isDeleteUnmatchedTargets() {
        return this.deleteUnmatchedTargets;
    }

    /**
     * Returns the option of whether or not smart copy will be used
     * 
     * @return boolean
     */
    public boolean isSmartCopy() {
        return this.smartCopy;
    }

    /**
     * Returns the buffer size that will be used while copying
     * 
     * @return long
     */
    public long getChunkSize() {
        return this.chunkSize;
    }

    /**
     * This builder class builds a complete SyncOptions object and returns it
     * 
     * @author Tyler Snedigar
     * @version 1.0
     */
    public static class SyncOptionsBuilder {
        private boolean deleteUnmatchedTargets = true;

        private boolean smartCopy = true;

        private long chunkSize = 1024 * 1024 * 16;

        /**
         * Sets the delete unmatched targets option. The default is true
         * 
         * @param b
         *            unmatched
         * @return SyncOptionsBuilder
         */
        public SyncOptionsBuilder deleteUnmatchedTargets(boolean b) {
            this.deleteUnmatchedTargets = b;
            return this;
        }

        /**
         * Sets the smartCopy option. The default is true
         * 
         * @param b
         *            smartcopy
         * @return SyncOptionsBuilder
         */
        public SyncOptionsBuilder smartCopy(boolean b) {
            this.smartCopy = b;
            return this;
        }

        /**
         * Sets the copy chunk size. It is strongly recommended that a power of
         * 2 is used. The default is 16MB (1024*1024*16).
         * 
         * @param l
         *            chunksize
         * @return SyncOptionsBuilder
         */
        public SyncOptionsBuilder chunkSize(long l) {
            this.chunkSize = l;
            return this;
        }

        /**
         * Builds the SyncOptions object and returns it
         * 
         * @return SyncOptions
         */
        public SyncOptions build() {
            return new SyncOptions(this);
        }
    }
}
