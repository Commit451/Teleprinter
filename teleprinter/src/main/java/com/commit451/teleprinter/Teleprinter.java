package com.commit451.teleprinter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.ref.WeakReference;

/**
 * Teleprinter, like the old electromechanical typewriters. This class fills the holes of dealing
 * with Keyboard on Android
 */
public class Teleprinter {

    private WeakReference<Activity> mActivityWeakReference;
    InputMethodManager mInputMethodManager;


    public Teleprinter(Activity activity) {
        mActivityWeakReference = new WeakReference<>(activity);
        mInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * Hides the keyboard
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
     * @param view the view to get focus
     * @return true if keyboard is show, false otherwise
     */
    public boolean showKeyboard(View view) {
        return mInputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
}
