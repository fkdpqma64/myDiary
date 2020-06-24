package com.example.mydairy

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import common.lib.livedata.CustomViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val context: Context
)  : CustomViewModel(){

    private val mViewData = MutableLiveData<List<Any>>()
    val viewData = mViewData.distinctUntilChanged()

    init {

    }
}