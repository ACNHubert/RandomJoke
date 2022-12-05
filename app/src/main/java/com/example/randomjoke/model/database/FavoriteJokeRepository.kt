package com.example.randomjoke.model.database

class FavoriteJokeRepository(private val dao : JokeDAO) {
    val jokes = dao.getAllJoke()
    suspend fun insert(manageCities: ManageFavoriteJoke){
        dao.insertJoke(manageCities)
    }
    suspend fun delete(manageCities: ManageFavoriteJoke){
        dao.deleteJoke(manageCities)
    }
    suspend fun viewAll(){
        dao.getAllJoke()
    }
}