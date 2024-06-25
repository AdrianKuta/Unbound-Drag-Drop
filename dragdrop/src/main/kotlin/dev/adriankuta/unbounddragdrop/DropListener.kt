package dev.adriankuta.unbounddragdrop

import android.view.DragEvent
import android.view.View
import android.view.View.OnDragListener
import androidx.recyclerview.widget.RecyclerView

internal class DropListener(private val callback: DragDropHelper.Callback) : OnDragListener {

    /**
     * Handles drag events on the target view.
     *
     * @param targetView The view that is being dragged over or dropped onto.
     * @param event The drag event.
     * @return True if the event was handled, false otherwise.
     */
    override fun onDrag(targetView: View?, event: DragEvent?): Boolean {
        targetView ?: return false
        event?.let {
            if (it.action == DragEvent.ACTION_DROP) {
                val sourceView = it.localState as View
                val sourceRecyclerView = sourceView.parent as RecyclerView
                val sourcePosition = sourceRecyclerView.getChildAdapterPosition(sourceView)
                val sourceViewHolder =
                    sourceRecyclerView.findViewHolderForAdapterPosition(sourcePosition)
                        ?: return false

                val targetRecyclerView = getRecyclerView(targetView) ?: return false
                val targetAdapter = targetRecyclerView.adapter
                val targetPosition = if (targetView is RecyclerView) {
                    targetAdapter?.itemCount ?: 0
                } else {
                    targetRecyclerView.getChildAdapterPosition(targetView)
                }
                val targetViewHolder =
                    targetRecyclerView.findViewHolderForAdapterPosition(targetPosition)

                if (callback.onMove(
                        sourceRecyclerView,
                        sourceViewHolder,
                        targetRecyclerView,
                        targetViewHolder
                    )
                ) {
                    callback.onMoved(
                        sourceRecyclerView,
                        sourceViewHolder,
                        targetRecyclerView,
                        targetViewHolder
                    )
                }
            }
        }
        return true
    }

    /**
     * Retrieves the RecyclerView associated with the given view.
     *
     * @param view The view to check.
     * @return The RecyclerView if found, null otherwise.
     */
    private fun getRecyclerView(view: View): RecyclerView? {
        return view as? RecyclerView ?: view.parent as? RecyclerView
    }
}