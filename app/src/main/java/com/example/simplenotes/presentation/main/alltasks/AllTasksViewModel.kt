package com.example.simplenotes.presentation.main.alltasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotes.data.repositories.FirestoreRepository
import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.usecases.GetTasksByCategoryUseCase
import kotlinx.coroutines.launch

@ExperimentalStdlibApi
class AllTasksViewModel(private val categoryId: String) : ViewModel() {
    private val _listOfTasks = MutableLiveData<List<Task>>()
    val listOfTasks: LiveData<List<Task>>
        get() = _listOfTasks

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
}