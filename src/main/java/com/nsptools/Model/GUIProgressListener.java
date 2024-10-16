package com.nsptools.Model;

import javafx.scene.control.ProgressBar;

/**
 * Implementation of ProgressListener that updates a ProgressBar in the GUI.
 * 
 * @author V-Karch
 */
public class GUIProgressListener implements ProgressListener {
    private final ProgressBar progressBar;

    /**
     * Constructor to initialize the GuiProgressListener with a ProgressBar.
     * 
     * @param progressBar the ProgressBar to update
     */
    public GUIProgressListener(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public void onProgressUpdate(double progress) {
        // Update the progress bar value
        progressBar.setProgress(progress);
    }
}
