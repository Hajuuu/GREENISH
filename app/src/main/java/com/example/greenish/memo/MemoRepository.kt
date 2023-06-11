package com.example.greenish.memo

import androidx.lifecycle.LiveData
import com.example.greenish.Plant
import com.example.greenish.PlantDao

class MemoRepository(private val memoDao: MemoDao) {

    val readAllData: LiveData<List<Memo>> = memoDao.readAllData()

    suspend fun addMemo(memo: Memo) {
        memoDao.addMemo(memo)
    }

    suspend fun deleteMemo(memo: Memo) { //
        memoDao.deleteMemo(memo)
    }


    suspend fun updateMemo(memo: Memo){
         memoDao.updatePlant(memo)
    }

}