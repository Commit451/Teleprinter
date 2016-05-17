package com.commit451.teleprinter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Teleprinter, like the old electromechanical typewriters. This class fills the holes of dealing
 * with Keyboard on Android
 */
public class Teleprinter {

    private WeakReference<Activity> mActivityWeakReference;
    private WeakReference<View> mRootViewWeakReference;
    private InputMethodManager mInputMethodManager;
    private ArrayList<OnKeyboardToggleListener> mKeyboardListeners;
    private ViewTreeObserver.OnGlobalLayoutListener mViewTreeObserverListener;

    public Teleprinter(Activity activity) {
        mActivityWeakReference = new WeakReference<>(activity);
        mInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * Hides the keyboard
     *
     * @return true if the keyboard is successfully hidden, false otherwise
     */
    public boolean hideKeyboard() {
        Activity activity = mActivityWeakReference.get();
        if (activity == null) {
            return false;
        }
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            return mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return false;
    }

    /**
     * Show the keyboard
     *
     * @param view the view to get focus
     * @return true if keyboard is show, false otherwise
     */
    public boolean showKeyboard(View view) {
        return mInputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Listen for when the keyboard is toggled by the user. NOTE: this will only work if your {@link Activity}
     * android:windowSoftInputMode="adjustResize"
     * @param listener the listener to register
     */
    public void addKeyboardToggleListener(OnKeyboardToggleListener listener) {
        if (mRootViewWeakReference == null) {
            if (mActivityWeakReference.get() != null) {
                mRootViewWeakReference = new WeakReference<>(mActivityWeakReference.get().findViewById(Window.ID_ANDROID_CONTENT));
                mViewTreeObserverListener = new GlobalLayoutListener();
                mRootViewWeakReference.get().getViewTreeObserver().addOnGlobalLayoutListener(mViewTreeObserverListener);
            }
        }
        if (mKeyboardListeners == null) {
            mKeyboardListeners = new ArrayList<>();
        }
        mKeyboardListeners.add(listener);
    }

    /**
     * Unregister your listener so that it no longer is alerted to keyboard toggling
     * @param listener the listener to unregister
     */
    public void removeKeyboardWatcher(OnKeyboardToggleListener listener) {
        if (mKeyboardListeners != null) {
            mKeyboardListeners.remove(listener);
            if (mKeyboardListeners.isEmpty()) {
                mRootViewWeakReference.clear();
                removeLayoutListener();
            }
        }

    }

    private void removeLayoutListener() {
        if (mRootViewWeakReference.get() != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                mRootViewWeakReference.get().getViewTreeObserver().removeOnGlobalLayoutListener(mViewTreeObserverListener);
            } else {
                mRootViewWeakReference.get().getViewTreeObserver().removeGlobalOnLayoutListener(mViewTreeObserverListener);
            }
        }
    }

    /**
     * Listener to be alerted to keyboard events
     */
    public interface OnKeyboardToggleListener {
        /**
         * Called when the keyboard is shown
         * @param keyboardSize the size of the keyboard, in pixels
         */
        void onKeyboardShown(int keyboardSize);

        /**
         * Called when the keyboard is closed
         */
        void onKeyboardClosed();
    }

    private class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        int initialValue;
        boolean hasSentInitialAction;
        boolean isKeyboardShown;

        @Override
        public void onGlobalLayout() {
            if (initialValue == 0) {
                initialValue = mRootViewWeakReference.get().getHeight();
            } else {
                if (initialValue > mRootViewWeakReference.get().getHeight()) {
                        if (!hasSentInitialAction || !isKeyboardShown) {
                            isKeyboardShown = true;
                            if (mKeyboardListeners != null) {
                                for (OnKeyboardToggleListener listener : mKeyboardListeners) {
                                    listener.onKeyboardShown(initialValue - mRootViewWeakReference.get().getHeight());
                                }
                            }
                        }
                } else {
                    if (!hasSentInitialAction || isKeyboardShown) {
                        isKeyboardShown = false;
                        mRootViewWeakReference.get().post(new Runnable() {
                            @Override
                            public void run() {
                                if (mKeyboardListeners != null) {
                                    for (OnKeyboardToggleListener listener : mKeyboardListeners) {
                                        listener.onKeyboardClosed();
                                    }
                                }
                            }
                        });
                    }
                }
                hasSentInitialAction = true;
            }
        }
    }
}
