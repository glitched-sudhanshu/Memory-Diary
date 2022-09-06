package com.example.memodiary.models.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.memodiary.models.entities.MemoDiary
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDiaryDao {
    //MemoDiary is the entity that we created
    //Insert annotation will allow us to insert data into our database
    @Insert
    suspend fun insertFavMemoDiaryDetails(memoDiary: MemoDiary)

    /**
     * For flow, the idea is that when data changes, do you really want to take some actions such as displaying the updated data to the UI. So, this means you have to update the data. So, when it changes, you can react and to observe the data change we will use FLOW
     */
    //this is not a suspend function because we are using FLOW. when we use flow we do not mark it as a suspend function
    @Query("SELECT * FROM MEMO_DIARY_TABLE ORDER BY ID")
    fun getAllMemoryList(): Flow<List<MemoDiary>>

    @Update
    suspend fun updateFavMemoryDetails(memoDiary: MemoDiary)

    @Query("SELECT * FROM MEMO_DIARY_TABLE WHERE favourite_memory = 1")
    fun getFavouriteMemory(): Flow<List<MemoDiary>>

}