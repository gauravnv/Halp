package com.example.halp.resultDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.halp.database.ResultDatabaseDao

class ResultDetailViewModelFactory(
    private val resultKey: String,
    private val dataSource: ResultDatabaseDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultDetailViewModel::class.java)) {
            return ResultDetailViewModel(resultKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}