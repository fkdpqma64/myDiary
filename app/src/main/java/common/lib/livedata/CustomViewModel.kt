package common.lib.livedata

import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class CustomViewModel : ViewModel(), Observable {

    private val mDataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = mDataLoading

    private fun startDataLoading() = mDataLoading.postValue(true)
    private fun stopDataLoading() = mDataLoading.postValue(false)
    fun isDataLoading() = mDataLoading.value == true

    suspend fun <T> runDataLoading(block: suspend () -> T) {
        startDataLoading()
        try {
            block()
        } catch (e: Exception) {
            Log.d("ERROR", e.message+"")
        } finally {
            stopDataLoading()
        }
    }

    suspend fun <T> runProcessing(block: suspend () -> T) {
        try {
            block()
        } catch (e: Exception) {
            Log.d("ERROR", e.message+"")
        }
    }

    @Transient
    private var mCallbacks: PropertyChangeRegistry? = null

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            if (mCallbacks == null) {
                mCallbacks = PropertyChangeRegistry()
            }
        }
        mCallbacks!!.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks!!.remove(callback)
    }
}