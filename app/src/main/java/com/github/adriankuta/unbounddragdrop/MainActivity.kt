package com.github.adriankuta.unbounddragdrop

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.adriankuta.unbounddragdrop.databinding.ActivityMainBinding
import com.github.adriankuta.unbounddragdrop.model.TaskStatus
import dev.adriankuta.unbounddragdrop.DragDropHelper
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: SimpleGridPageAdapter
    private lateinit var inProgressAdapter: SimpleGridPageAdapter
    private val callback = object : DragDropHelper.Callback() {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            targetRecyclerView: RecyclerView,
            targetViewHolder: RecyclerView.ViewHolder?
        ): Boolean {
            return onMove(
                recyclerView,
                viewHolder.adapterPosition,
                targetRecyclerView,
                targetViewHolder?.adapterPosition
            )
        }

        override fun onMoved(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            targetRecyclerView: RecyclerView,
            targetViewHolder: RecyclerView.ViewHolder?
        ) = Unit

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerViews()
        setObservers()
    }

    private fun setupRecyclerViews() {
        todoAdapter = SimpleGridPageAdapter()
        inProgressAdapter = SimpleGridPageAdapter()

        with(binding) {
            setupRecyclerView(toDoRecyclerView, todoAdapter)
            setupRecyclerView(inProgressRecyclerView, inProgressAdapter)
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect(::render)
        }
    }

    private fun render(uiState: MainUiState) {
        todoAdapter.submitList(uiState.todo)
        inProgressAdapter.submitList(uiState.inProgress)
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>
    ) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            GridLayoutManager(this, GRID_ROWS, GridLayoutManager.HORIZONTAL, false)
        DragDropHelper(callback).attachToRecyclerView(recyclerView)
    }

    private fun onMove(
        recyclerView: RecyclerView,
        sourcePosition: Int,
        targetRecyclerView: RecyclerView,
        targetPosition: Int?
    ): Boolean {
        if (recyclerView == targetRecyclerView) {
            onChangeOrder(recyclerView, sourcePosition, targetPosition)
        } else {
            onChangeStatus(recyclerView, sourcePosition, targetRecyclerView, targetPosition)
        }
        return true
    }

    private fun onChangeOrder(
        recyclerView: RecyclerView,
        sourcePosition: Int,
        targetPosition: Int?
    ) {
        targetPosition ?: return
        val taskStatus = when (recyclerView.id) {
            R.id.to_do_recycler_view -> TaskStatus.TODO
            R.id.in_progress_recycler_view -> TaskStatus.IN_PROGRESS
            else -> null
        } ?: return

        viewModel.onChangeOrder(taskStatus, sourcePosition, targetPosition)
    }

    private fun onChangeStatus(
        recyclerView: RecyclerView,
        sourcePosition: Int,
        targetRecyclerView: RecyclerView,
        targetPosition: Int?
    ) {
        val fromStatus = when (recyclerView.id) {
            R.id.to_do_recycler_view -> TaskStatus.TODO
            R.id.in_progress_recycler_view -> TaskStatus.IN_PROGRESS
            else -> null
        }
        val toStatus = when (targetRecyclerView.id) {
            R.id.to_do_recycler_view -> TaskStatus.TODO
            R.id.in_progress_recycler_view -> TaskStatus.IN_PROGRESS
            else -> null
        }
        if (fromStatus != null && toStatus != null) {
            viewModel.onChangeStatus(fromStatus, toStatus, sourcePosition, targetPosition)
        }
    }

    companion object {
        const val GRID_ROWS = 2
    }
}