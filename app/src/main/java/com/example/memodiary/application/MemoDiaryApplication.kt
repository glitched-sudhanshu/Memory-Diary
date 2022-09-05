package com.example.memodiary.application

import android.app.Application
import com.example.memodiary.models.database.MemoDiaryRepository
import com.example.memodiary.models.database.MemoDiaryRoomDatabase

//the idea behind this application class is so that we can define that variable scope to use throughout the application
class MemoDiaryApplication : Application() {

    //by lazy means these variables will be initialized only when they will be required not just as soon as our app starts
    private val database by lazy{
        MemoDiaryRoomDatabase.getDatabase((this@MemoDiaryApplication))
    }

//    the return instance uses itself to synchronize on and don't synchronize from external code on the return, for instance, as it may cause accidental deadlock
val repository by lazy{
        MemoDiaryRepository(database.memoDiaryDao())
    }
}