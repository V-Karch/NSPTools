package com.nsptools.Model;

/**
 * A simple interface to listen for progress updates during file operations.
 * Useful for displaying progress in a console or GUI.
 * 
 * @author V-Karch
 */
public interface ProgressListener {
    /**
     * Called to update the progress of a task.
     * 
     * @param progress a value between 0.0 and 1.0 representing the task completion percentage.
     */
    void onProgressUpdate(double progress);
}
