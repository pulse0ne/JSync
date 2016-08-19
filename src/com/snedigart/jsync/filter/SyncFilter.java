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

/**
 * Interface for SyncFilters
 * 
 * @author Tyler Snedigar
 * @version 1.0
 */
public interface SyncFilter {

    /**
     * Operation that is performed on the passed-in file to determine if filter
     * matches
     * 
     * @param f
     *            file to match against
     * @return boolean
     */
    public abstract boolean matches(File f);
}
