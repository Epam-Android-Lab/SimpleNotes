package com.example.simplenotes.presentation.main.alltasks.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotes.data.repositories.FirestoreRepository
import com.example.simplenotes.data.repositories.RemoteRepository
import com.example.simplenotes.domain.entities.Category
import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.usecases.GetAllCategoriesByUser
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch

class FilterViewModel: ViewModel() {
    private val _allCategories = MutableLiveData<List<Category>>()
    val allCategories: LiveData<List<Category>>
        get() = _allCategories

    private val _checkedCategories = MutableLiveData<List<Category>>()
    val checkedCategories: LiveData<List<Category>>
        get() = _checkedCategories

    fun defaults(){
        viewModelScope.launch {
            GetAllCategoriesByUser(FirestoreRepository()).execute()?.let { querySnapshot ->
                val result = mutableListOf<Category>()

                querySnapshot.forEach {
                    result.add(it.toObject(Category::class.java))
                }

                _allCategories.postValue(result.toList())
                _checkedCategories.postValue(result.toList())
            }
        }
    }

}