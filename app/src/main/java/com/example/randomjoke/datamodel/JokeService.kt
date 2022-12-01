package com.example.randomjoke.datamodel

import retrofit.Call
import retrofit.http.GET

interface JokeService {
    @GET("random_ten")
    fun getJokes(): Call<JokesDataModel>
}