package dev.adriankuta.unbounddragdrop

import android.content.ClipData
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.DRAG_FLAG_OPAQUE
import androidx.recyclerview.widget.RecyclerView.ViewHolder
/**
 * Helper class to handle drag and drop functionality in a RecyclerView.
 *
 * @param callback A Callback object to handle the drag and drop events.
 */
class DragDropHelper(callback: Callback) :
    RecyclerView.OnChildAttachStateChangeListener {
    private var mRecyclerView: RecyclerView? = null
    private var recyclerItemClickListener: RecyclerItemClickListener? = null
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
        if (mRecyclerView === recyclerView) {
            return  // nothing to do
        }
        if (mRecyclerView != null) {
            destroyCallbacks()
        }
        mRecyclerView = recyclerView
        mRecyclerView?.let {
            setupCallbacks()
        }
    }

    /**
     * Sets up the necessary callbacks for the RecyclerView.
     */
    private fun setupCallbacks() {
        mRecyclerView?.apply {
            recyclerItemClickListener = RecyclerItemClickListener(
                context,
                this,
                onItemLongClickListener
            ).also {
                mRecyclerView?.addOnItemTouchListener(it)
            }
            addOnChildAttachStateChangeListener(this@DragDropHelper)
            setOnDragListener(dropListener)
        }
    }

    /**
     * Removes the callbacks from the RecyclerView.
     */
    private fun destroyCallbacks() {
        recyclerItemClickListener?.let {
            mRecyclerView?.removeOnItemTouchListener(it)
        }
        mRecyclerView?.removeOnChildAttachStateChangeListener(this)
        mRecyclerView?.setOnDragListener(null)
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