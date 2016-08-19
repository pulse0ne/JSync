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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.snedigart.jsync.filter.SyncFilter;

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

    private final List<SyncFilter> inclusionFilters;

    private final List<SyncFilter> exclusionFilters;

    private final boolean matchAllInclusionFilters;

    private final boolean matchAllExclusionFilters;

    // private for builder pattern
    private SyncOptions(SyncOptionsBuilder builder) {
        this.deleteUnmatchedTargets = builder.deleteUnmatchedTargets;
        this.smartCopy = builder.smartCopy;
        this.chunkSize = builder.chunkSize;
        this.inclusionFilters = new ArrayList<>(builder.inclusionFilters);
        this.exclusionFilters = new ArrayList<>(builder.exclusionFilters);
        this.matchAllInclusionFilters = builder.matchAllInclusionFilters;
        this.matchAllExclusionFilters = builder.matchAllExclusionFilters;
    }

    /**
     * Returns the option of whether or not to delete target files/directories
     * that do not correspond to a source file/directory. Defaults to true.
     * 
     * @return boolean
     */
    public boolean isDeleteUnmatchedTargets() {
        return this.deleteUnmatchedTargets;
    }

    /**
     * Returns the option of whether or not smart copy will be used. Defaults to
     * true.
     * 
     * @return boolean
     */
    public boolean isSmartCopy() {
        return this.smartCopy;
    }

    /**
     * Returns the buffer size that will be used while copying. Defaults to
     * 16MB.
     * 
     * @return long
     */
    public long getChunkSize() {
        return this.chunkSize;
    }

    /**
     * Returns an unmodifiable List of the inclusion filters. Defaults to an
     * empty list.
     * 
     * @return List of SyncFilters
     */
    public List<SyncFilter> getInclusionFilters() {
        return Collections.unmodifiableList(this.inclusionFilters);
    }

    /**
     * Returns an unmodifiable List of the exclusion filters. Defaults to an
     * empty list.
     * 
     * @return List of SyncFilters
     */
    public List<SyncFilter> getExclusionFilters() {
        return Collections.unmodifiableList(this.exclusionFilters);
    }

    /**
     * Returns the flag to match all inclusion filters, or match any inclusion
     * filter. Defaults to true (must match all inclusion filters).
     * 
     * @return boolean
     */
    public boolean isMatchAllInclusionFilters() {
        return this.matchAllInclusionFilters;
    }

    /**
     * Returns the flag to match all exclusion filters, or match any exclusion
     * filter. Defaults to true (must match all exclusion filters).
     * 
     * @return boolean
     */
    public boolean isMatchAllExclusionFilters() {
        return this.matchAllExclusionFilters;
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

        private List<SyncFilter> inclusionFilters = new ArrayList<>();

        private List<SyncFilter> exclusionFilters = new ArrayList<>();

        private boolean matchAllInclusionFilters = true;

        private boolean matchAllExclusionFilters = true;

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
         * Sets the inclusion filters, overwriting any previous list built with
         * .addInclusionFilter()
         * 
         * @param f
         *            filters
         * @return SyncOptionsBuilder
         */
        public SyncOptionsBuilder setInclusionFilters(List<SyncFilter> f) {
            if (f != null) {
                this.inclusionFilters = f;
            }
            return this;
        }

        /**
         * Adds a new inclusion filter
         * 
         * @param f
         *            filter
         * @return SyncOptionsBuilder
         */
        public SyncOptionsBuilder addInclusionFilter(SyncFilter f) {
            this.inclusionFilters.add(f);
            return this;
        }

        /**
         * Sets the exclusion filters, overwriting any previous built with
         * .addExclusionFilter()
         * 
         * @param f
         *            filters
         * @return SyncOPtionsBuilder
         */
        public SyncOptionsBuilder setExclusionFilters(List<SyncFilter> f) {
            if (f != null) {
                this.exclusionFilters = f;
            }
            return this;
        }

        /**
         * Adds a new exclusion filter
         * 
         * @param f
         *            filter
         * @return SyncOptionsBuilder
         */
        public SyncOptionsBuilder addExclusionFilter(SyncFilter f) {
            this.exclusionFilters.add(f);
            return this;
        }

        /**
         * Sets matchAllInclusionFilters flag. If set to true, the file will be
         * included only if ALL inclusion filters match. If set to false, the
         * file will be included if ANY ONE of the inclusion filters match.
         * 
         * @param b
         *            flag
         * @return SyncOptionsBuilder
         */
        public SyncOptionsBuilder matchAllInclusionFilters(boolean b) {
            this.matchAllInclusionFilters = b;
            return this;
        }

        /**
         * Sets matchAllExclusionsFilters flag. If set to true, the file will be
         * excluded only if ALL exclusion filters match. If set to false, the
         * file will be excluded if ANY ONE of the exclusion filters match.
         * 
         * @param b
         *            flag
         * @return SyncOptionsBuilder
         */
        public SyncOptionsBuilder matchAllExclusionFilters(boolean b) {
            this.matchAllExclusionFilters = b;
            return this;
        }

        /**
         * Builds the SyncOptions object and returns it. Guaranteed to return a
         * fully built and initialized object (read: no nulls).
         * 
         * @return SyncOptions
         */
        public SyncOptions build() {
            return new SyncOptions(this);
        }
    }
}
