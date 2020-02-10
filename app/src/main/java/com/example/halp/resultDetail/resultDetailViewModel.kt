package com.example.halp.resultDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.halp.database.ResultDatabaseDao
import com.example.halp.database.ResultRow
import kotlinx.coroutines.Job

class ResultDetailViewModel(
    private val resultKey: String = "",
    dataSource: ResultDatabaseDao) : ViewModel() {


}
