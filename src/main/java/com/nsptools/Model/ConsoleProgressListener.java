package com.nsptools.Model;

/**
 * Implementation of ProgressListener that displays progress in the console.
 * 
 * @author V-Karch
 */
public class ConsoleProgressListener implements ProgressListener {

    @Override
    public void onProgressUpdate(double progress) {
        int percent = (int) (progress * 100);
        System.out.print("\rProgress: " + percent + "%");
        if (percent == 100) {
            System.out.println("\nFile splitting completed.");
        }
    }
}
