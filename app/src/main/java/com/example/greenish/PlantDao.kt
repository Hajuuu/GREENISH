package com.example.greenish

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlantDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlant(plant:Plant)

    @Query("SELECT * FROM plant_table ORDER BY name ASC")
    fun readAllData(): LiveData<List<Plant>>

    @Delete
    suspend fun deletePlant(plant:Plant)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlant(plant:Plant)

}