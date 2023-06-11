package com.example.greenish.memo

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.greenish.Plant

@Dao
interface MemoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMemo(memo: Memo)


    @Query("SELECT * FROM memo_table ORDER BY name ASC")
    fun readAllData(): LiveData<List<Memo>>

    @Query("SELECT * FROM memo_table WHERE name = :name")
    fun loadMemoByName(name: String): LiveData<List<Memo>>


    @Delete
    suspend fun deleteMemo(memo: Memo)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlant(memo: Memo)

}