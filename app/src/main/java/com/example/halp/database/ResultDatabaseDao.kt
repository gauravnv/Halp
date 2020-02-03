package com.example.halp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ResultDatabaseDao {
    @Insert
    fun insert(resultRowRow: ResultRow)

    @Update
    fun update(resultRowRow: ResultRow)

    @Query("SELECT * from result_row_table WHERE  id = :key")
    fun get(key: Long): ResultRow?

    @Query("DELETE FROM result_row_table")
    fun clear()

    @Query("SELECT * FROM result_row_table  ORDER BY id DESC")
    fun getAllResults(): LiveData<List<ResultRow>>

    @Query("SELECT *  FROM result_row_table ORDER BY id DESC LIMIT 1")
    fun getResult(): ResultRow?

    @Query("SELECT * from result_row_table WHERE id = :key")
    fun getResultWithId(key: String): LiveData<ResultRow>
}