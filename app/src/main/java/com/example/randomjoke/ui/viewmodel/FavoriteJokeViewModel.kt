package com.example.randomjoke.ui.viewmodel

import androidx.lifecycle.*
import com.example.randomjoke.model.database.FavoriteJokeRepository
import com.example.randomjoke.model.database.ManageFavoriteJoke
import kotlinx.coroutines.launch

class FavoriteJokeViewModel (private val repository: FavoriteJokeRepository) : ViewModel(){


    val jokes = repository.jokes
    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete : ManageFavoriteJoke

    val inputJoke = MutableLiveData<String?>()
    fun save(){
        val name = inputJoke.value!!
                    insert(ManageFavoriteJoke(0, name))
                    inputJoke.value = null
    }

    fun Delete(){
            delete(subscriberToUpdateOrDelete)
    }

     fun insert(manageFavJoke : ManageFavoriteJoke)= viewModelScope.launch {
        repository.insert(manageFavJoke)
    }

    fun delete(manageFavJoke: ManageFavoriteJoke) = viewModelScope.launch {
        repository.delete(manageFavJoke)
        inputJoke.value = null
        isUpdateOrDelete = false
    }

    fun initUpdateAndDelete(manageJokes: ManageFavoriteJoke){
        inputJoke.value = manageJokes.favoriteJoke
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = manageJokes
    }
}