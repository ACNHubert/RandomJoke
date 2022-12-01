package com.example.randomjoke.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_joke_table")
data class ManageFavoriteJoke (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "favorite_joke")
    var id : Int,

    @ColumnInfo(name = "joke_id")
    var favoriteJoke : String
)