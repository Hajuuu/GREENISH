package com.example.greenish.memo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "memo_table")
@Parcelize
class Memo(var name: String, var memo:String, var image: String, val year: Int, val month: Int, val day: Int): Parcelable{
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}