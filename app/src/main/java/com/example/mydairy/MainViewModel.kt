package com.example.mydairy

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.distinctUntilChanged
import common.lib.livedata.CustomViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val context: Context
)  : CustomViewModel(){

    private val mViewData = MediatorLiveData<List<Any>>()
    val viewData = mViewData.distinctUntilChanged()
    init {

    }
}