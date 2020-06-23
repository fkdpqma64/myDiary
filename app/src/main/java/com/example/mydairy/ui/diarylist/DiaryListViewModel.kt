package com.example.mydairy.ui.diarylist

import android.content.Context
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import common.data.local.DiaryItem
import common.lib.livedata.CustomViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiaryListViewModel @Inject constructor(
    private val context: Context
) : CustomViewModel() {

    private val mViewData = MediatorLiveData<List<Any>>()
    val viewData = mViewData.distinctUntilChanged()

    init {

    }

    fun refreshViewData() {

        if (isDataLoading()) {
            Log.d("XXX", "already DataLoading...")
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            Log.d("XXX", "${Thread.currentThread()}")
            runDataLoading {
                mViewData.postValue(List<DiaryItem>(10){DiaryItem("다이어리 제목")})
            }
        }
    }

}





