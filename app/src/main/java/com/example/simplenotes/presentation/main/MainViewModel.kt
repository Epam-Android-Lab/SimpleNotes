package com.example.simplenotes.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotes.data.repositories.FirestoreRepository
import com.example.simplenotes.domain.entities.Category
import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.usecases.CreateCategoryUseCase
import com.example.simplenotes.domain.usecases.GetAllCategoriesByUser
import com.example.simplenotes.domain.usecases.GetAllTasksByUserUseCase
import kotlinx.coroutines.launch


@ExperimentalStdlibApi
class MainViewModel : ViewModel(), Contract.IMainViewModel {

    private val _categoryState = MutableLiveData<List<Category>>()
    override val categoryState: LiveData<List<Category>>
        get() = _categoryState
    private val _latestTaskState = MutableLiveData<List<Task>>()
    override val latestTaskState: LiveData<List<Task>>
        get() = _latestTaskState

    override fun addCategory(category: Category) {
        viewModelScope.launch {
            CreateCategoryUseCase(FirestoreRepository()).execute(category)
        }
    }
    override fun getAllCategories() {
        viewModelScope.launch {
            GetAllCategoriesByUser(FirestoreRepository()).execute()
                .let { categorySnapshot ->
                    _categoryState.value = buildList {
                        categorySnapshot?.forEach {
                            add(it.toObject(Category::class.java))
                        }
                    }
                }

        }
    }

    override fun getLatestTasks() {
        viewModelScope.launch {
            GetAllTasksByUserUseCase(FirestoreRepository()).execute()
                .let { latestTaskSnapshot ->
                    _latestTaskState.value = buildList {
                        latestTaskSnapshot?.forEach {
                            add(it.toObject(Task::class.java))
                        }
                    }
                }

        }
    }

    // при добавлении не отображаются новые категории
    override fun subscribeToFireBase() {
        TODO("Not yet implemented")
    }
}