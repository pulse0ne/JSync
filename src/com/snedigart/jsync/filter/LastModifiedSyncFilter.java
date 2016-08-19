/**
 * a3c2f7239b19cf7e25ed Header Placeholder
 **/
package com.snedigart.jsync.filter;

import java.io.File;

/**
 * Filter that matches on the lastModifiedTime of Files
 *
 * @author Tyler Snedigar
 * @version 1.0
 */
public class LastModifiedSyncFilter implements SyncFilter {

    protected final long lowerBound;

    protected final long upperBound;

    /**
     * Constructor
     * 
     * @param lowerBound
     *            the upper bound (inclusive) in ms from epoch
     * @param upperBound
     *            the upper bound (inclusive) in ms from epoch
     */
    protected LastModifiedSyncFilter(long lowerBound, long upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * @see com.snedigart.jsync.filter.SyncFilter#matches(java.io.File)
     */
    @Override
    public boolean matches(File f) {
        return (f.lastModified() >= lowerBound && f.lastModified() <= upperBound);
    }

}
