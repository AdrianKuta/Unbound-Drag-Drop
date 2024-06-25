package com.github.adriankuta.unbounddragdrop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.adriankuta.unbounddragdrop.model.Task
import com.github.adriankuta.unbounddragdrop.model.TaskStatus
import com.github.adriankuta.unbounddragdrop.model.TaskStatus.IN_PROGRESS
import com.github.adriankuta.unbounddragdrop.model.TaskStatus.TODO
import com.github.adriankuta.unbounddragdrop.util.WhileUiSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Collections
import java.util.UUID

data class MainUiState(
    val todo: List<Task> = emptyList(),
    val inProgress: List<Task> = emptyList(),
)

class MainViewModel : ViewModel() {


    private val _todoTasks = MutableStateFlow<List<Task>>(emptyList())
    private val _inProgressTasks = MutableStateFlow<List<Task>>(emptyList())

    init {
        _todoTasks.value =
            List(30) { Task(UUID.randomUUID().toString(), "Task $it", TODO) }
        _inProgressTasks.value =
            List(4) { Task(UUID.randomUUID().toString(), "Task $it", IN_PROGRESS) }
    }


    val uiState: StateFlow<MainUiState> = combine(_todoTasks, _inProgressTasks) { listA, listB ->
        MainUiState(listA, listB)
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = MainUiState()
    )

    fun onChangeOrder(status: TaskStatus, sourcePosition: Int, targetPosition: Int) {
        when (status) {
            TODO -> _todoTasks.swapElements(sourcePosition, targetPosition)
            IN_PROGRESS -> _inProgressTasks.swapElements(sourcePosition, targetPosition)
        }
    }

    fun onChangeStatus(
        fromStatus: TaskStatus,
        toStatus: TaskStatus,
        fromPosition: Int,
        toPosition: Int?
    ) {
        val itemToMove = when (fromStatus) {
            TODO -> _todoTasks.removeAt(fromPosition)
            IN_PROGRESS -> _inProgressTasks.removeAt(fromPosition)
        }

        when (toStatus) {
            TODO -> _todoTasks.add(toPosition ?: _todoTasks.size(), itemToMove)
            IN_PROGRESS -> _inProgressTasks.add(toPosition ?: _inProgressTasks.size(), itemToMove)
        }
    }

    private fun <T> MutableStateFlow<List<T>>.swapElements(
        sourcePosition: Int,
        targetPosition: Int
    ) {
        val newList = value.toMutableList()
        Collections.swap(newList, sourcePosition, targetPosition)
        value = newList
    }

    private fun <T> MutableStateFlow<List<T>>.removeAt(index: Int): T {
        val newList = value.toMutableList()
        val removedItem = newList.removeAt(index)
        value = newList
        return removedItem
    }

    private fun <T> MutableStateFlow<List<T>>.add(index: Int, item: T) {
        val newList = value.toMutableList()
        newList.add(index, item)
        value = newList
    }

    private fun <T> MutableStateFlow<List<T>>.size(): Int {
        return value.size
    }

}