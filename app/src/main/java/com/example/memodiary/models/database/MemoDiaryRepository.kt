package com.example.memodiary.models.database

import androidx.annotation.WorkerThread
import com.example.memodiary.models.entities.MemoDiary
import kotlinx.coroutines.flow.Flow

/**
 * A Repository manages queries and allows you to use multiple backends. In the most common example, the Repository implements the logic for deciding whether to fetch data from a network or use results cached in a local database.
 */

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class MemoDiaryRepository(private val memoDiaryDao: MemoDiaryDao) {
    /**
     * Denotes that the annotated method should only be called on a worker thread. If the annotated element is a class, then all methods in the class should be called on a worker thread.
     */

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
//    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()


    @WorkerThread
    suspend fun insertMemoDiaryData(memoDiary: MemoDiary){
        memoDiaryDao.insertFavMemoDiaryDetails(memoDiary)
    }

    val allMemoDiaryList: Flow<List<MemoDiary>> = memoDiaryDao.getAllMemoryList()
}

/**
 * A Flow is an async sequence of values

Flow produces values one at a time (instead of all at once) that can generate values from async operations like network requests, database calls, or other async code. It supports coroutines throughout its API, so you can transform a flow using coroutines as well!

By default, to avoid poor UI performance, Room doesn't allow you to issue queries on the main thread. When Room queries return Flow, the queries are automatically run asynchronously on a background thread.
 */