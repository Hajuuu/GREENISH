package com.example.greenish.memo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.greenish.Plant
import com.example.greenish.PlantDao

@Database(entities = [Memo::class], version = 1, exportSchema = false)
abstract class MemoRoomDatabase : RoomDatabase() {

    abstract fun memoDao(): MemoDao

    companion object{
        @Volatile
        private var INSTANCE: MemoRoomDatabase? = null

        fun getDatabase(context: Context):MemoRoomDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MemoRoomDatabase::class.java,
                    "memo_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}