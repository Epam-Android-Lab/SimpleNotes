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

    private val _isDone = MutableLiveData<Boolean>()
    val isDone: LiveData<Boolean>
        get() = _isDone


    private val _filterOptions = MutableLiveData<ConfirmState>(ConfirmState.NotPrepared)
    val filterOptions: LiveData<ConfirmState>
        get() = _filterOptions

    companion object {
        const val DEFAULT_PRIORITY = 3
        const val DEFAULT_IS_DONE = false
    }

    sealed class ConfirmState {
        object NotPrepared : ConfirmState()
        data class Prepared(val filterOptions: FilterOptions) : ConfirmState()
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
        updateIsDone(DEFAULT_IS_DONE)

    }

    fun receives(filterOptions: FilterOptions){
        viewModelScope.launch {
            GetAllCategoriesByUser(FirestoreRepository()).execute()?.let { querySnapshot ->
                val result = mutableListOf<Category>()

                querySnapshot.forEach {
                    result.add(it.toObject(Category::class.java))
                }

                _allCategories.postValue(result.toList())
            }
        }

        filterOptions.priority?.let { updatePriority(it) }
        filterOptions.status?.let { updateIsDone(it) }

        filterOptions.categories?.forEach {
            updateCheckedList(it, true)
        }
    }

    fun updatePriority(value: Int) {
        _priority.postValue(value)
    }

    fun updateIsDone(isDone: Boolean) {
        _isDone.postValue(isDone)
    }

    fun updateCheckedList(category: Category, isChecked: Boolean) {
        if (isChecked) _checkedCategories.value?.add(category) else _checkedCategories.value?.remove(
            category
        )
    }

    fun confirm() {
        _filterOptions.postValue(
            ConfirmState.Prepared(
                FilterOptions(
                    categories = checkedCategories.value,
                    priority = priority.value,
                    status = isDone.value
                )
            )
        )
    }

}