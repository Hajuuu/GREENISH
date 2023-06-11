package com.example.greenish

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class PlantRepository(private val plantDao: PlantDao) {

    val readAllData: LiveData<List<Plant>> = plantDao.readAllData()

    suspend fun addPlant(plant: Plant) {
        plantDao.addPlant(plant)
    }

    suspend fun deletePlant(plant:Plant) {
        plantDao.deletePlant(plant)
    }


    suspend fun updatePlant(plant:Plant){
        plantDao.updatePlant(plant)
    }
}