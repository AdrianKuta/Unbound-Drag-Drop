package dev.adriankuta.unbounddragdrop

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal class RecyclerItemClickListener(
    context: Context,
    private val recyclerView: RecyclerView,
    private val listener: OnItemLongClickListener?
) : RecyclerView.OnItemTouchListener {

    private val gestureDetector: GestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            /**
             * Called when a long press gesture is detected.
             *
             * @param e The motion event triggering the long press.
             */
            override fun onLongPress(e: MotionEvent) {
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                if (childView != null && listener != null) {
                    listener.onItemLongClick(
                        childView,
                        recyclerView.getChildAdapterPosition(childView)
                    )
                }
            }

            /**
             * Called when a single tap up gesture is detected.
             *
             * @param e The motion event triggering the single tap.
             * @return True to indicate the event is handled.
             */
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }
        })

    /**
     * Interface definition for a callback to be invoked when an item in this
     * RecyclerView has been long clicked.
     */
    interface OnItemLongClickListener {
        /**
         * Called when an item has been long clicked.
         *
         * @param view The view that was clicked.
         * @param position The position of the view in the adapter.
         */
        fun onItemLongClick(view: View, position: Int)
    }

    /**
     * Intercept touch events to determine if a long press has occurred.
     *
     * @param rv The RecyclerView.
     * @param e The motion event.
     * @return True if the event is intercepted, false otherwise.
     */
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val childView = rv.findChildViewUnder(e.x, e.y)
        return childView != null && childView.isLongClickable && gestureDetector.onTouchEvent(e)
    }

    /**
     * Handle touch events (not needed for this implementation).
     *
     * @param rv The RecyclerView.
     * @param e The motion event.
     */
    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        // Not needed
    }

    /**
     * Request to disallow intercepting touch events (not needed for this implementation).
     *
     * @param disallowIntercept True to disallow intercepting touch events.
     */
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        // Not needed
    }
}