package blackboard.test2;

public class UIUpdateThread extends Thread {
    public boolean shouldBeRunning = true;
    public MainActivity mainActivity;
    public DetailedViewActivity detailedViewActivity;

    public UIUpdateThread (MainActivity mainActivity, DetailedViewActivity detailedViewActivity) {
        super();
        this.mainActivity = mainActivity;
        this.detailedViewActivity = detailedViewActivity;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted() && shouldBeRunning) {
                Thread.sleep(1000);
                if (mainActivity != null) {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainActivity.updateUI();
                        }
                    });
                }
                if (detailedViewActivity != null) {
                    detailedViewActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            detailedViewActivity.updateUI();
                        }
                    });
                }
            }
        } catch (InterruptedException e) {}
    }
}
