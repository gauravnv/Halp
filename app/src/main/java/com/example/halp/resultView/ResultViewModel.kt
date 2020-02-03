package com.example.halp.resultView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.halp.database.ResultDatabaseDao
import com.example.halp.database.ResultRow
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class ResultViewModel(
    val database: ResultDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var result = MutableLiveData<ResultRow?>()

    val results = database.getAllResults()


    private val _navigateToResultDetail = MutableLiveData<ResultRow>()
    /**
     * Call this immediately after calling `show()` on a toast.
     *
     * It will clear the toast request, so if the user rotates their phone it won't show a duplicate
     * toast.
     */

    fun onResultClicked(id: ResultRow) {
        _navigateToResultDetail.value = id
    }

    /**
     * If this is non-null, immediately navigate to [SleepQualityFragment] and call [doneNavigating]
     */
    val navigateToResultDetail: LiveData<ResultRow>
        get() = _navigateToResultDetail

    /**
     * Call this immediately after navigating to [SleepQualityFragment]
     *
     * It will clear the navigation request, so if the user rotates their phone it won't navigate
     * twice.
     */
    fun doneNavigating() {
        _navigateToResultDetail.value = null
    }


    init {
        initializeResult()
    }

    private fun initializeResult() {
        uiScope.launch {
            result.value = getResultFromDatabase()
        }
    }

    /**
     *  Handling the case of the stopped app or forgotten recording,
     *  the start and end times will be the same.j
     *
     *  If the start time and end time are not the same, then we do not have an unfinished
     *  recording.
     */
    private suspend fun getResultFromDatabase(): ResultRow? {
        return withContext(Dispatchers.IO) {
            val result = database.getResult()
            result
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    private suspend fun update(result: ResultRow) {
        withContext(Dispatchers.IO) {
            database.update(result)
        }
    }

    private suspend fun insert(result: ResultRow) {
        withContext(Dispatchers.IO) {
            database.insert(result)
        }
    }

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all coroutines;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
