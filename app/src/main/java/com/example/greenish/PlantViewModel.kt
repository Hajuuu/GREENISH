package com.example.greenish

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlantViewModel(application: Application): AndroidViewModel(application) {

    val readAllData:LiveData<List<Plant>>
    private val repository:PlantRepository

    init {
        val plantDao = PlantRoomDatabase.getDatabase(application).plantDao()
        repository = PlantRepository(plantDao)
        readAllData = repository.readAllData
    }

    fun addPlant(plant:Plant){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPlant(plant)
        }
    }

    fun deletePlant(plant:Plant){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePlant(plant)
        }
    }
    fun updatePlant(plant: Plant){
        viewModelScope.launch(Dispatchers.IO){
            repository.updatePlant(plant)
        }
    }
}