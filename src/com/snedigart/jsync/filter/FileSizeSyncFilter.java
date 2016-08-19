/**
 * a3c2f7239b19cf7e25ed Header Placeholder
 **/
package com.snedigart.jsync.filter;

import java.io.File;

/**
 * FileSizeSyncFilter matches against files within the given range
 *
 * @author Tyler Snedigar
 * @version 1.0
 */
public class FileSizeSyncFilter implements SyncFilter {

    protected final long lowerSize;

    protected final long upperSize;

    /**
     * Constructor
     * 
     * @param lowerSize
     *            the lower bound (inclusive) of the range
     * @param upperSize
     *            the upper bound (inclusive) of the range
     */
    public FileSizeSyncFilter(long lowerSize, long upperSize) {
        this.lowerSize = lowerSize;
        this.upperSize = upperSize;
    }

    /**
     * @see com.snedigart.jsync.filter.SyncFilter#matches(java.io.File)
     */
    @Override
    public boolean matches(File f) {
        return (f.length() >= lowerSize && f.length() <= upperSize);
    }

}
