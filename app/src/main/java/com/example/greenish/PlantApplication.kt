package com.example.greenish

import android.app.Application

class PlantApplication : Application() {
    val database by lazy { PlantRoomDatabase.getDatabase(this) }
    val repository by lazy { PlantRepository(database.plantDao()) }
}