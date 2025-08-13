package com.example.cocktailapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailapp.data.local.CocktailRepository
import com.example.cocktailapp.data.local.CocktailEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CocktailDetailViewModel(
    private val repository: CocktailRepository
) : ViewModel() {

    private val _cocktail = MutableStateFlow<CocktailEntity?>(null)
    val cocktail: StateFlow<CocktailEntity?> = _cocktail

    fun loadCocktail(id: Int) {
        viewModelScope.launch {
            _cocktail.value = repository.getCocktailById(id)
        }
    }

    fun updateCocktail(updated: CocktailEntity) {
        viewModelScope.launch {
            repository.updateCocktail(updated)
            _cocktail.value = updated
        }
    }
}