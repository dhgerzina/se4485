package blackboard.test2;

import android.app.Activity;

/**
 * Automatically updates the UI for the given Activity based on BlackBoard data
 */
public class UIUpdateThread extends Thread {
    public boolean shouldBeRunning = true;
    public HasBlackBoardUI userInterface;
    private int delay;

    public UIUpdateThread (HasBlackBoardUI ui, int delay) {
        super();
        userInterface = ui;
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted() && shouldBeRunning) {
                Thread.sleep(delay);
                ((Activity)userInterface).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userInterface.updateUI();
                    }
                });
            }
        } catch (InterruptedException e) {}
    }
}
