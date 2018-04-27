package blackboard.test2;

/**
 * Used by UIUpdateThread to update either MainActivity or DetailedViewActivity
 * Classes that implement this interface must also be subclasses of Activity
 * Otherwise UIUpdateThread.run() will cause errors
 */
public interface HasBlackBoardUI {
    /**
     * update the user interface based on blackboard information
     */
    void updateUI ();
}
