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
 * Callback for progress information
 * 
 * @author Tyler Snedigar
 * @version 1.0
 */
public interface ProgressCallback {

    /**
     * The method that is called at each progress step. The remaining and total
     * values will be 0 when syncing starts (important to note that calculations
     * will have a divide by zero error). They will go to -1 if an error occurs,
     * and the message will explain the error.
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