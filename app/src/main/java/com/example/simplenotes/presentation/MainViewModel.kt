package com.example.simplenotes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotes.data.repositories.FirestoreRepository
import com.example.simplenotes.domain.entities.Category
import com.example.simplenotes.domain.usecases.CreateCategoryUseCase
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(), Contract.IMainViewModel {

    override val state: LiveData<MutableList<Category>> by lazy { MutableLiveData() }

    override fun addCategory(category: Category) {
        viewModelScope.launch {
            CreateCategoryUseCase(FirestoreRepository()).execute(category)
        }
    }
}