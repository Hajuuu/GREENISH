package com.example.greenish.memo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.greenish.Plant
import com.example.greenish.PlantRepository
import com.example.greenish.PlantRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Memo>>
    private val repository: MemoRepository

    init {
        val memoDao = MemoRoomDatabase.getDatabase(application).memoDao()
        repository = MemoRepository(memoDao)
        readAllData = repository.readAllData

    }

    fun addMemo(memo: Memo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMemo(memo)
        }
    }

    fun deleteMemo(memo: Memo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMemo(memo)
        }
    }
    fun updateMemo(memo: Memo){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateMemo(memo)
        }
    }

}