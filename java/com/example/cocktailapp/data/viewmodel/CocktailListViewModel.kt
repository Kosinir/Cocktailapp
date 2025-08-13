package com.example.cocktailapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailapp.data.local.CocktailRepository
import com.example.cocktailapp.data.local.CocktailEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CocktailListViewModel(private val repository: CocktailRepository) : ViewModel() {

    private val _cocktails = MutableStateFlow<List<CocktailEntity>>(emptyList())
    val cocktails: StateFlow<List<CocktailEntity>> = _cocktails

    init {
        loadCocktails()
    }

    private fun loadCocktails() {
        viewModelScope.launch {
            _cocktails.value = repository.getAllCocktails()
        }
    }
}