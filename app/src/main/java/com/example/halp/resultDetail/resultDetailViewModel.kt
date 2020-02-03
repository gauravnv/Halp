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

    /**
     * Hold a reference to ResultDatabase via its ResultDatabaseDao.
     */
    val database = dataSource

    /** Coroutine setup variables */

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    private val result = MediatorLiveData<ResultRow>()

    fun getResult() = result

    init {
        result.addSource(database.getResultWithId(resultKey), result::setValue)
    }

    /**
     * Variable that tells the fragment whether it should navigate to [ResultFragment].
     *
     * This is `private` because we don't want to expose the ability to set [MutableLiveData] to
     * the [Fragment]
     */
    private val _navigateToResultView = MutableLiveData<Boolean?>()

    /**
     * When true immediately navigate back to the [SleepTrackerFragment]
     */
    val navigateToResultView: LiveData<Boolean?>
        get() = _navigateToResultView

    /**
     * Cancels all coroutines when the ViewModel is cleared, to cleanup any pending work.
     *
     * onCleared() gets called when the ViewModel is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    /**
     * Call this immediately after navigating to [SleepTrackerFragment]
     */
    fun doneNavigating() {
        _navigateToResultView.value = null
    }

    fun onClose() {
        _navigateToResultView.value = true
    }

}
