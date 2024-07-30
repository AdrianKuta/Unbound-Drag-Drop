package dev.adriankuta.unbounddragdrop

import android.content.ClipData
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.DRAG_FLAG_OPAQUE
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * Helper class to handle drag and drop functionality in multiple RecyclerViews.
 *
 * @param callback A Callback object to handle the drag and drop events.
 */
class DragDropHelper(private val callback: Callback) : RecyclerView.OnChildAttachStateChangeListener {
    private val recyclerViews = mutableListOf<RecyclerView>()
    private val recyclerItemClickListeners = mutableMapOf<RecyclerView, RecyclerItemClickListener>()
    private val dropListener = DropListener(callback)
    private val onItemLongClickListener: RecyclerItemClickListener.OnItemLongClickListener by lazy {
        object : RecyclerItemClickListener.OnItemLongClickListener {
            /**
             * Called when an item is long-clicked. Starts the drag operation.
             *
             * @param view The view that was long-clicked.
             * @param position The position of the item in the adapter.
             */
            override fun onItemLongClick(view: View, position: Int) {
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = View.DragShadowBuilder(view)
                view.startDragAndDrop(data, shadowBuilder, view, DRAG_FLAG_OPAQUE)
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            }
        }
    }

    /**
     * Attaches the DragDropHelper to the specified RecyclerView.
     *
     * @param recyclerView The RecyclerView to attach to.
     */
    fun attachToRecyclerView(recyclerView: RecyclerView?) {
        recyclerView?.let {
            if (recyclerViews.contains(it)) return  // already attached

            recyclerViews.add(it)
            setupCallbacks(it)
        }
    }

    /**
     * Detaches the DragDropHelper from the specified RecyclerView.
     *
     * @param recyclerView The RecyclerView to detach from.
     */
    fun detachFromRecyclerView(recyclerView: RecyclerView?) {
        recyclerView?.let {
            if (!recyclerViews.contains(it)) return  // not attached

            destroyCallbacks(it)
            recyclerViews.remove(it)
        }
    }

    /**
     * Sets up the necessary callbacks for the RecyclerView.
     *
     * @param recyclerView The RecyclerView to setup callbacks for.
     */
    private fun setupCallbacks(recyclerView: RecyclerView) {
        recyclerView.apply {
            val clickListener = RecyclerItemClickListener(
                context,
                this,
                onItemLongClickListener
            )
            recyclerItemClickListeners[this] = clickListener
            addOnItemTouchListener(clickListener)
            addOnChildAttachStateChangeListener(this@DragDropHelper)
            setOnDragListener(dropListener)
        }
    }

    /**
     * Removes the callbacks from the RecyclerView.
     *
     * @param recyclerView The RecyclerView to remove callbacks from.
     */
    private fun destroyCallbacks(recyclerView: RecyclerView) {
        recyclerItemClickListeners[recyclerView]?.let {
            recyclerView.removeOnItemTouchListener(it)
            recyclerItemClickListeners.remove(recyclerView)
        }
        recyclerView.removeOnChildAttachStateChangeListener(this)
        recyclerView.setOnDragListener(null)
    }

    /**
     * Called when a child view is attached to the RecyclerView.
     *
     * @param view The child view that was attached.
     */
    override fun onChildViewAttachedToWindow(view: View) {
        view.setOnDragListener(dropListener)
    }

    /**
     * Called when a child view is detached from the RecyclerView.
     *
     * @param view The child view that was detached.
     */
    override fun onChildViewDetachedFromWindow(view: View) {
        view.setOnDragListener(null)
    }

    /**
     * Abstract class to handle drag and drop events.
     */
    abstract class Callback {

        /**
         * Called when an item is moved within or between RecyclerViews.
         *
         * @param recyclerView The RecyclerView containing the dragged item.
         * @param viewHolder The ViewHolder of the dragged item.
         * @param targetRecyclerView The RecyclerView where the item is dropped.
         * @param targetViewHolder The ViewHolder of the target position.
         * @return True if the move was handled, false otherwise.
         */
        abstract fun onMove(
            recyclerView: RecyclerView,
            viewHolder: ViewHolder,
            targetRecyclerView: RecyclerView,
            targetViewHolder: ViewHolder?
        ): Boolean

        /**
         * Called when an item has been dropped.
         *
         * @param recyclerView The RecyclerView containing the dragged item.
         * @param viewHolder The ViewHolder of the dragged item.
         * @param targetRecyclerView The RecyclerView where the item is dropped.
         * @param targetViewHolder The ViewHolder of the target position.
         */
        abstract fun onMoved(
            recyclerView: RecyclerView,
            viewHolder: ViewHolder,
            targetRecyclerView: RecyclerView,
            targetViewHolder: ViewHolder?
        )
    }
}
