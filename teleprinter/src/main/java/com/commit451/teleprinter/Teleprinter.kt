package com.commit451.teleprinter

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.inputmethod.InputMethodManager

import java.lang.ref.WeakReference
import android.graphics.Rect


/**
 * Teleprinter, like the old electromechanical typewriters. This class fills the holes of dealing
 * with keyboards
 */
class Teleprinter(activity: AppCompatActivity) : LifecycleObserver, ViewTreeObserver.OnGlobalLayoutListener {

    companion object {
        //TODO should this be adjusted depending on the density of the device?
        private const val KEYBOARD_THRESHHOLD = 250 //px to consider keyboard opened
    }

    private val activityWeakReference: WeakReference<Activity> = WeakReference(activity)
    private val viewWeakReference: WeakReference<View> = WeakReference(activity.findViewById(Window.ID_ANDROID_CONTENT))
    private val inputMethodManager: InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    private val onKeyboardOpenedListeners by lazy { mutableListOf<OnKeyboardOpenedListener>() }
    private val onKeyboardClosedListeners by lazy { mutableListOf<OnKeyboardClosedListener>() }

    private var isSoftKeyboardOpened: Boolean = false

    init {
        viewWeakReference.get()?.viewTreeObserver?.addOnGlobalLayoutListener(this)
        activity.lifecycle.addObserver(this)
    }

    /**
     * Hides the keyboard
     *
     * @return true if the keyboard is successfully hidden, false otherwise
     */
    fun hideKeyboard(): Boolean {
        val activity = activityWeakReference.get() ?: return false
        // Check if no view has focus:
        val view = activity.currentFocus
        return if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } else false
    }

    /**
     * Show the keyboard
     *
     * @param view the view to get focus
     * @return true if keyboard is show, false otherwise
     */
    fun showKeyboard(view: View): Boolean {
        return inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    /**
     * Is the keyboard currently showing?
     * @return true if showing, false if not
     */
    fun isKeyboardShowing(): Boolean = isSoftKeyboardOpened

    /**
     * Listen for when the keyboard is opened. NOTE: this will only work if your [Activity]
     * android:windowSoftInputMode="adjustResize"
     * @param listener the listener to register
     */
    fun addOnKeyboardOpenedListener(listener: OnKeyboardOpenedListener) {
        onKeyboardOpenedListeners.add(listener)
    }

    /**
     * Unregister for keyboard open events
     * @param listener the listener to unregister
     */
    fun removeOnKeyboardOpenedListener(listener: OnKeyboardOpenedListener) {
        onKeyboardOpenedListeners.remove(listener)
    }

    /**
     * Listen for when the keyboard is closed. NOTE: this will only work if your [Activity]
     * android:windowSoftInputMode="adjustResize"
     * @param listener the listener to register
     */
    fun addOnKeyboardClosedListener(listener: OnKeyboardClosedListener) {
        onKeyboardClosedListeners.add(listener)
    }

    /**
     * Unregister for keyboard close events
     * @param listener the listener to unregister
     */
    fun removeOnKeyboardClosedListener(listener: OnKeyboardClosedListener) {
        onKeyboardClosedListeners.remove(listener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        viewWeakReference.get()?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
        onKeyboardOpenedListeners.clear()
        onKeyboardClosedListeners.clear()
    }

    override fun onGlobalLayout() {
        val r = Rect()
        val activityRootView = viewWeakReference.get() ?: return
        //r will be populated with the coordinates of your view that area still visible.
        activityRootView.getWindowVisibleDisplayFrame(r)

        val heightDiff = activityRootView.rootView.height - (r.bottom - r.top)
        if (!isSoftKeyboardOpened && heightDiff > KEYBOARD_THRESHHOLD) { // if more than 100 pixels, its probably a keyboard...
            isSoftKeyboardOpened = true
            onKeyboardOpenedListeners.forEach { it.invoke(heightDiff) }
        } else if (isSoftKeyboardOpened && heightDiff < KEYBOARD_THRESHHOLD) {
            isSoftKeyboardOpened = false
            onKeyboardClosedListeners.forEach { it.invoke() }
        }
    }
}

typealias OnKeyboardOpenedListener = (height: Int) -> Unit
typealias OnKeyboardClosedListener = () -> Unit
