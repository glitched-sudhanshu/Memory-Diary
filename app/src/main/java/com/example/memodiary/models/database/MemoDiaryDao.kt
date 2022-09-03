package com.example.memodiary.models.database

import androidx.room.Dao
import androidx.room.Insert
import com.example.memodiary.models.entities.MemoDiary

@Dao
interface MemoDiaryDao {
    //MemoDiary is the entity that we created
    //Insert annotation will allow us to insert data into our database
    @Insert
    suspend fun insertFavMemoDiaryDetails(memoDiary: MemoDiary){

    }

}