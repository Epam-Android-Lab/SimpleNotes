package com.example.simplenotes.presentation.main.alltasks

import android.content.Context
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
import com.example.simplenotes.presentation.main.alltasks.filter.FilterOptions
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch

class AllTasksViewModel(
    private val getTasksByCategoryUseCase: GetTasksByCategoryUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
    private val getTasksByUserUseCase: GetAllTasksByUserUseCase
) : ViewModel() {
    private val _listOfTasks = MutableLiveData<List<Task>>()
    val listOfTasks: LiveData<List<Task>>
        get() = _listOfTasks

    private val _listOfOptions = MutableLiveData<List<String>>()
    val listOfOptions: LiveData<List<String>>
        get() = _listOfOptions

    private val _activeSort = MutableLiveData<String>()
    val activeSort: LiveData<String>
        get() = _activeSort


    @ExperimentalStdlibApi
    fun getData(categoryId: String, filterOptions: FilterOptions? = null) {
        viewModelScope.launch {

            getTasksByCategoryUseCase.execute(category = categoryId)
                .let { snapshot ->
                    val list: MutableList<Task> = mutableListOf()
                    snapshot?.forEach {
                        list.add(it.toObject(Task::class.java))
                    }
                    _listOfTasks.value = list
                }

            if (filterOptions != null) {
                applyFilters(filterOptions)
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

    fun getTasksByUserUseCase() {
        viewModelScope.launch {

            getTasksByUserUseCase.execute().let { snapshot ->
                val list: MutableList<Task> = mutableListOf()
                snapshot?.forEach {
                    list.add(it.toObject(Task::class.java))
                }
                _listOfTasks.value = list
            }
        }
    }

    fun updateStatus(status: Boolean, id: String) {
        viewModelScope.launch {
            updateTaskStatusUseCase.execute(status, id)
        }
    }

    fun getOptions(context: Context) {
        _listOfOptions.postValue(context.resources.getStringArray(R.array.sorting_options).toList())
    }

    fun setActiveSortOption(index: Int) {
        _activeSort.postValue(_listOfOptions.value?.get(index))

        when (index) {
            0 -> {
                _listOfTasks.value?.let {
                    _listOfTasks.postValue(it.sortedBy { task ->
                        task.timeLastEdit
                    }.asReversed())
                }
            }
            1 -> {
                _listOfTasks.value?.let {
                    _listOfTasks.postValue(it.sortedBy { task ->
                        task.timeLastEdit
                    })
                }
            }
            2 -> {
                _listOfTasks.value?.let {
                    _listOfTasks.postValue(it.sortedBy { task ->
                        task.deadline
                    }.asReversed())
                }
            }
            3 -> {
                _listOfTasks.value?.let {
                    _listOfTasks.postValue(it.sortedBy { task ->
                        task.deadline
                    })
                }
            }
            4 -> {
                _listOfTasks.value?.let {
                    _listOfTasks.postValue(it.sortedBy { task ->
                        task.priority
                    }.asReversed())
                }
            }
            5 -> {
                _listOfTasks.value?.let {
                    _listOfTasks.postValue(it.sortedBy { task ->
                        task.priority
                    })
                }
            }
        }
    }

    private fun applyFilters(filterOptions: FilterOptions) {

        _listOfTasks.value?.let { list ->
            val filterResult = list.filter {
                it.status == filterOptions.status
            }
            _listOfTasks.postValue(filterResult)
        }

//        if (filterOptions.categories != null) {
//            _listOfTasks.value?.let { list ->
//
//                _listOfTasks.postValue( list.filter {
//                    filterOptions.categories.run {
//                        var result = false
//
//                        forEach { category ->
//                            result = category.name == it.category
//                        }
//
//                        result
//                    }
//                })
//
//            }
//        }

        _listOfTasks.value?.let { list ->
            _listOfTasks.postValue(list.filter {
                it.priority == filterOptions.priority
            })
        }

    }
}