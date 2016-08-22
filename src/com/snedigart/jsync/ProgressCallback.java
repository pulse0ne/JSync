/**
 * a3c2f7239b19cf7e25ed Header Placeholder
 **/
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