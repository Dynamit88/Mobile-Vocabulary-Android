/**
 * @author: Ivan Mykolenko
 * @date: 24.04.2019
 */
package ivan.vocabulary.misc;

/**
 * An abstract class to be implemented so that and instance could be passed to the SwipeController to act as a callback.
 */
public abstract class SwipeControllerActions {
    public void onSwipeLeft(int position) {}
    public void onSwipeRight(int position) {}
}
