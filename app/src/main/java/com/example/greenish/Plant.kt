package com.example.greenish

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "plant_table")
@Parcelize
class Plant(@PrimaryKey var name: String, var image: String?, var place: String?, var date: String?, var water: String?, var waterCycle: String?): Parcelable

