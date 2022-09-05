package com.example.memodiary.models.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.memodiary.models.entities.MemoDiary

//@Database annotation is used to make it a room db.
//entities = [MemoDiary::class] is an array of entities that we are gonna use inside our db.
//version is whenever you make a change to your db structure, you need to migrate that and change the version up.
//You can wipe all the data and then use the same version as you go but then all the data will be gone and you need to handle it via migration.This is why, it is not a recommended way of doing that.
@Database(entities = [MemoDiary::class], version = 1)
abstract class MemoDiaryRoomDatabase :RoomDatabase(){
    companion object{
//        Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE : MemoDiaryRoomDatabase? = null

        fun getDatabase(context : Context) : MemoDiaryRoomDatabase {
            //if INSTANCE is not null, then return it
            //if it is, then create the database
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MemoDiaryRoomDatabase::class.java,
                    "memo_diary_database"
                ).build()
                INSTANCE = instance
                //return instance
                instance
            }
        }
    }

    abstract fun memoDiaryDao() : MemoDiaryDao

//    For each DAO class that is associated with the database, the database class must define an abstract method that has zero arguments and returns an instance of the DAO class.
}

/**
 * Note: If your app runs in a single process, you should follow the singleton design pattern when instantiating an AppDatabase object. Each RoomDatabase instance is fairly expensive, and you rarely need access to multiple instances within a single process.

 * If your app runs in multiple processes, include enableMultiInstanceInvalidation() in your database builder invocation. That way, when you have an instance of AppDatabase in each process, you can invalidate the shared database file in one process, and this invalidation automatically propagates to the instances of AppDatabase within other processes.
**/