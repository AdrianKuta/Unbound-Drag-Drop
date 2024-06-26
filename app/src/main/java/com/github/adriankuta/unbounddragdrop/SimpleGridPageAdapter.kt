package com.github.adriankuta.unbounddragdrop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.adriankuta.unbounddragdrop.databinding.TextItemBinding
import com.github.adriankuta.unbounddragdrop.model.Task


class SimpleGridPageAdapter :
    RecyclerView.Adapter<SimpleGridPageAdapter.ViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Task,
            newItem: Task
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun submitList(list: List<Task>) {
        asyncListDiffer.submitList(list)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(viewGroup.inflate())
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = asyncListDiffer.currentList[position]
        viewHolder.itemView.isLongClickable = true
        viewHolder.bind(data)
    }

    override fun getItemCount() = asyncListDiffer.currentList.size

    class ViewHolder(private val binding: TextItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.textView.text = task.title
        }
    }

    private fun ViewGroup.inflate() =
        TextItemBinding.inflate(LayoutInflater.from(context), this, false)

}