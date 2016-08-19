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
package com.snedigart.jsync.filter;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;

/**
 * SyncFilter that matches on a file-name glob
 * 
 * @author Tyler Snedigar
 * @version 1.0
 */
public class FileNameSyncFilter implements SyncFilter {

    protected static final String GLOB_SYNTAX = "glob:";

    protected final PathMatcher matcher;

    /**
     * Constructor
     * 
     * @param glob
     *            to match against
     */
    public FileNameSyncFilter(String glob) {
        this.matcher = FileSystems.getDefault().getPathMatcher(GLOB_SYNTAX + glob);
    }

    /**
     * @see com.snedigart.jsync.filter.SyncFilter#matches(java.io.File)
     */
    @Override
    public boolean matches(File f) {
        return matcher.matches(f.toPath());
    }

}
