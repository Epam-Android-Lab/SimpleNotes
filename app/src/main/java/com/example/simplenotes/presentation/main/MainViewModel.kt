package com.example.simplenotes.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotes.data.repositories.FirestoreRepository
import com.example.simplenotes.domain.entities.Category
import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.usecases.*
import kotlinx.coroutines.launch


class MainViewModel (
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val getAllCategoriesByUser: GetAllCategoriesByUser,
    private val getAllTasksByUserUseCase: GetAllTasksByUserUseCase,
) : ViewModel(), Contract.IMainViewModel {

    private val _categoryState = MutableLiveData<List<Category>>()
    override val categoryState: LiveData<List<Category>>
        get() = _categoryState
    private val _latestTaskState = MutableLiveData<List<Task>>()
    override val latestTaskState: LiveData<List<Task>>
        get() = _latestTaskState

    override fun addCategory(category: Category) {
        viewModelScope.launch {
            createCategoryUseCase.execute(category)
        }
    }
    override fun getAllCategories() {
        viewModelScope.launch {
            getAllCategoriesByUser.execute()
                .let { categorySnapshot ->
                    val list: MutableList<Category> = mutableListOf()
                    categorySnapshot?.forEach {
                        list.add(it.toObject(Category::class.java))
                    }
                    _categoryState.value = list
                }

        }
    }

    override fun getLatestTasks() {
        viewModelScope.launch {
            getAllTasksByUserUseCase.execute()
                .let { latestTaskSnapshot ->
                    val list: MutableList<Task> = mutableListOf()
                    latestTaskSnapshot?.forEach {
                        list.add(it.toObject(Task::class.java))
                    }
                    _latestTaskState.value = list
                }

        }
    }

    // при добавлении не отображаются новые категории
    override fun subscribeToFireBase() {
        TODO("Not yet implemented")
    }
}