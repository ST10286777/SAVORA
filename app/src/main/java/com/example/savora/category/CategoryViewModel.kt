package com.example.savora.category

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CategoryViewModel(private val dao: CategoryDao): ViewModel()
{
    private val _state = MutableStateFlow(CategoryState())
    val state: StateFlow<CategoryState> = _state

    fun onEvent(event: CategoryEvent)
    {
        when(event)
        {
            is CategoryEvent.DeleteCategory -> {
                viewModelScope.launch{
                    dao.delete(event.category)
                }
            }

           is CategoryEvent.SetCategory ->{
                _state.update {it.copy( category = event.category)}
            }

            CategoryEvent.ShowDialog -> {
                _state.update { it.copy(isAddingCategory = true) }
            }

            CategoryEvent.HideDialog -> {
                _state.update { it.copy(isAddingCategory = false) }
            }

            CategoryEvent.SaveCategory -> {
                val category = _state.value.category
                if(category.isBlank()) { return }

                val cat = Category(category = category)

                viewModelScope.launch { dao.insert(cat) }
                _state.update { it.copy(isAddingCategory = false, category ="") }
            }


        }
    }
}