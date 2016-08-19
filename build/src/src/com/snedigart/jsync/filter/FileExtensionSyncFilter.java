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
 * The FileExtensionSyncFilter is a convenience subclass of the
 * FileNameSyncFilter
 * 
 * @author Tyler Snedigar
 * @version 1.0
 */
public class FileExtensionSyncFilter extends FileNameSyncFilter {

    protected static final String ALL_PATHS_PREFIX = "**" + File.separator + "*";

    protected static final String PERIOD = ".";

    /**
     * Constructor
     * 
     * @param extension
     *            the extension to filter on, with or without the leading '.'
     */
    public FileExtensionSyncFilter(String extension) {
        super(generateGlobWithExtension(extension));
    }

    /**
     * Protected static method to generate the Glob with the proper extension
     * 
     * @param extension
     *            the extension
     * @return String
     */
    protected static String generateGlobWithExtension(String extension) {
        if (extension.startsWith(PERIOD)) {
            return ALL_PATHS_PREFIX + extension.trim();
        } else {
            return ALL_PATHS_PREFIX + PERIOD + extension.trim();
        }
    }

}
