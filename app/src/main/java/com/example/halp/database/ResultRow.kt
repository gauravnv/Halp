package com.example.halp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "result_row_table")
data class ResultRow (
    @PrimaryKey(autoGenerate = true)
    var r_id: Long = 0L,

    @ColumnInfo(name = "id")
    var id: String = "",

    @ColumnInfo(name = "alias")
    val alias: String = "Business name alias",

    @ColumnInfo(name = "name")
    val name: String = "Business Name",

    @ColumnInfo(name = "image_url")
    val image_url: String = "",

    @ColumnInfo(name = "is_closed")
    val is_closed: Boolean = false,

    @ColumnInfo(name = "rating")
    val rating: Double = 0.0,

    @ColumnInfo(name = "price")
    val price: String = "$$$$$$$",

    @ColumnInfo(name = "phone")
    val phone: String = "+1234567890",

    @ColumnInfo(name = "distance")
    val distance: Double = 1234.0

)