package com.example.randomjoke.ui.viewmodel

import androidx.lifecycle.*
import com.example.randomjoke.model.database.FavoriteJokeRepository

class FavJokeViewModelFactory(private val repository: FavoriteJokeRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteJokeViewModel::class.java)) {
            return FavoriteJokeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}