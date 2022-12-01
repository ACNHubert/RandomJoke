package com.example.randomjoke.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface JokeDAO {
    @Insert
    suspend fun insertJoke(manageCities: ManageFavoriteJoke): Long

    @Delete
    suspend fun deleteJoke(manageCities: ManageFavoriteJoke)

    @Query("SELECT * FROM favorite_joke_table")
    fun getAllJoke(): LiveData<List<ManageFavoriteJoke>>
}