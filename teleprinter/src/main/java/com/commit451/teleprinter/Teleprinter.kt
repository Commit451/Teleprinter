@file:Suppress("unused")

package com.commit451.teleprinter

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.lang.ref.WeakReference


/**
 * Teleprinter, like the old electromechanical typewriters. This class fills the holes of dealing
 * with the keyboard API
 */
class Teleprinter(
    activity: AppCompatActivity,
    private val observeOpenCloseEvents: Boolean = false
) : LifecycleObserver, ViewTreeObserver.OnGlobalLayoutListener {

    companion object {
        private const val NEEDED_DELAY = 100L
    }

    private val activityWeakReference: WeakReference<Activity> = WeakReference(activity)
    private val viewWeakReference: WeakReference<View> = WeakReference(activity.findViewById(Window.ID_ANDROID_CONTENT))
    private val inputMethodManager: InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    private val onKeyboardOpenedListeners by lazy { mutableListOf<OnKeyboardOpenedListener>() }
    private val onKeyboardClosedListeners by lazy { mutableListOf<OnKeyboardClosedListener>() }
    /**
     * Amount of px to consider keyboard opened
     */
    private val keyboardThreshold = activity.resources.getDimensionPixelSize(R.dimen.teleprinter_keyboard_threshold)

    private var isSoftKeyboardOpened: Boolean = false

    init {
        if (observeOpenCloseEvents) {
            viewWeakReference.get()?.viewTreeObserver?.addOnGlobalLayoutListener(this)
            activity.lifecycle.addObserver(this)
        }
    }

    /**
     * Hides the keyboard
     *
     * @return true if the keyboard is successfully hidden, false otherwise
     */
    fun hideKeyboard(flags: Int = 0): Boolean {
        val activity = activityWeakReference.get() ?: return false
        // Check if no view has focus:
        val view = activity.currentFocus
        return if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, flags)
        } else false
    }

    /**
     * Show the keyboard
     *
     * @param view the view to get focus
     * @return true if keyboard is show, false otherwise
     */
    fun showKeyboard(view: View, flags: Int = InputMethodManager.SHOW_IMPLICIT): Boolean {
        return inputMethodManager.showSoftInput(view, flags)
    }

    /**
     * Show the keyboard after its needed delay, specifically for usage when an activity is just created.
     */
    fun showKeyboardWithDelay(view: View, flags: Int = InputMethodManager.SHOW_IMPLICIT) {
        view.postDelayed({
            showKeyboard(view, flags)
        }, NEEDED_DELAY)
    }

    /**
     * Toggle the keyboard, which forces it to show without a reference to a view
     */
    fun toggleKeyboard(showFlags: Int = InputMethodManager.SHOW_IMPLICIT, hideFlags: Int = 0) {
        inputMethodManager.toggleSoftInput(showFlags, hideFlags)
    }

    /**
     * Is the keyboard currently showing?
     * @return true if showing, false if not
     */
    fun isKeyboardShowing(): Boolean {
        checkObserving()
        return isSoftKeyboardOpened
    }

    /**
     * Listen for when the keyboard is opened.
     * @param listener the listener to register
     */
    fun addOnKeyboardOpenedListener(listener: OnKeyboardOpenedListener) {
        checkObserving()
        onKeyboardOpenedListeners.add(listener)
    }

    /**
     * Unregister for keyboard open events
     * @param listener the listener to unregister
     */
    fun removeOnKeyboardOpenedListener(listener: OnKeyboardOpenedListener) {
        checkObserving()
        onKeyboardOpenedListeners.remove(listener)
    }

    /**
     * Listen for when the keyboard is closed. NOTE: this will only work if your [Activity]
     * android:windowSoftInputMode="adjustResize"
     * @param listener the listener to register
     */
    fun addOnKeyboardClosedListener(listener: OnKeyboardClosedListener) {
        checkObserving()
        onKeyboardClosedListeners.add(listener)
    }

    /**
     * Unregister for keyboard close events
     * @param listener the listener to unregister
     */
    fun removeOnKeyboardClosedListener(listener: OnKeyboardClosedListener) {
        checkObserving()
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
        if (!isSoftKeyboardOpened && heightDiff > keyboardThreshold) { // if more than threshold pixels, its probably a keyboard...
            isSoftKeyboardOpened = true
            onKeyboardOpenedListeners.forEach { it.invoke(heightDiff) }
        } else if (isSoftKeyboardOpened && heightDiff < keyboardThreshold) {
            isSoftKeyboardOpened = false
            onKeyboardClosedListeners.forEach { it.invoke() }
        }
    }

    private fun checkObserving() {
        if (!observeOpenCloseEvents) {
            throw IllegalStateException("You are not observing open/close events. Change your constructor of Teleprinter to be Teleprinter(activity, true)")
        }
    }
}

typealias OnKeyboardOpenedListener = (height: Int) -> Unit
typealias OnKeyboardClosedListener = () -> Unit
