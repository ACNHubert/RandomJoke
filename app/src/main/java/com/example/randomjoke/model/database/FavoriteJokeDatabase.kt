package com.example.randomjoke.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ManageFavoriteJoke::class], version = 1)
abstract class FavoriteJokeDatabase : RoomDatabase() {

    abstract val jokeDAO : JokeDAO

    companion object{
        @Volatile
        private var instance : FavoriteJokeDatabase? = null
        fun getInstance(context: Context): FavoriteJokeDatabase {
            synchronized(this){
                var instance = instance
                if(instance==null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteJokeDatabase::class.java,
                        "city_data_database"
                    ).build()
                    Companion.instance = instance
                }
                Companion.instance = instance
                return instance
            }
        }

    }
}