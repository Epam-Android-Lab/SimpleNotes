package com.example.simplenotes.presentation.main.alltasks.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotes.data.repositories.FirestoreRepository
import com.example.simplenotes.domain.entities.Category
import com.example.simplenotes.domain.usecases.GetAllCategoriesByUser
import kotlinx.coroutines.launch

class FilterViewModel : ViewModel() {
    private val _allCategories = MutableLiveData<List<Category>>()
    val allCategories: LiveData<List<Category>>
        get() = _allCategories

    private val _checkedCategories = MutableLiveData<MutableList<Category>>()
    val checkedCategories: LiveData<MutableList<Category>>
        get() = _checkedCategories

    private val _priority = MutableLiveData<Int>()
    val priority: LiveData<Int>
        get() = _priority

    companion object {
        const val DEFAULT_PRIORITY = 3
    }



    fun defaults() {
        viewModelScope.launch {
            GetAllCategoriesByUser(FirestoreRepository()).execute()?.let { querySnapshot ->
                val result = mutableListOf<Category>()

                querySnapshot.forEach {
                    result.add(it.toObject(Category::class.java))
                }

                _allCategories.postValue(result.toList())
                _checkedCategories.postValue(result)
            }
        }

        updatePriority(DEFAULT_PRIORITY)

    }

    fun updatePriority(value: Int){
        _priority.postValue(value)
    }

    fun updateCheckedList(category: Category, isChecked: Boolean) {
        if (isChecked) _checkedCategories.value?.add(category) else _checkedCategories.value?.remove(category)
    }

}