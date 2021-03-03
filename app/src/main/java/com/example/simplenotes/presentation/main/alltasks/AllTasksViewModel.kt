package com.example.simplenotes.presentation.main.alltasks

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotes.R
import com.example.simplenotes.data.repositories.FirestoreRepository
import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.usecases.GetAllTasksByUserUseCase
import com.example.simplenotes.domain.usecases.GetTasksByCategoryUseCase
import com.example.simplenotes.domain.usecases.UpdateTaskStatusUseCase
import kotlinx.coroutines.launch

@ExperimentalStdlibApi
class AllTasksViewModel(private val categoryId: String) : ViewModel() {
    private val _listOfTasks = MutableLiveData<List<Task>>()
    val listOfTasks: LiveData<List<Task>>
        get() = _listOfTasks

    private val _listOfOptions = MutableLiveData<List<String>>()
    val listOfOptions: LiveData<List<String>>
        get() = _listOfOptions

    private val _activeSortIndex = MutableLiveData<Int>()
    val activeSortIndex: LiveData<Int>
        get() = _activeSortIndex

    init {
        getData(categoryId)
    }

    @ExperimentalStdlibApi
    private fun getData(categoryId: String) {
        viewModelScope.launch {

            GetTasksByCategoryUseCase(FirestoreRepository()).execute(category = categoryId)
                .let { snapshot ->
                    _listOfTasks.value = buildList {
                        snapshot?.forEach {
                            add(it.toObject(Task::class.java))
                        }
                    }
                }


//            GetAllTasksByUserUseCase(FirestoreRepository()).execute()?.let { snapshot ->
//                _listOfTasks.value = buildList {
//                    snapshot.forEach {
//                        add(it.toObject(Task::class.java))
//                    }
//                }
//            }
        }
    }

    fun updateStatus(status: Boolean, id: String) {
        viewModelScope.launch {
            UpdateTaskStatusUseCase(FirestoreRepository()).execute(status, id)
        }
    }

    fun getOptions(context: Context) {
        _listOfOptions.postValue(context.resources.getStringArray(R.array.sorting_options).toList())
    }

    fun setActiveSortOption(index: Int) {
        _activeSortIndex.postValue(index)
    }
}