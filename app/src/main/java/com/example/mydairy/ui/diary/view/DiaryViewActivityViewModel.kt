package com.example.mydairy.ui.diary.view

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import common.lib.livedata.CustomViewModel
import javax.inject.Inject

class DiaryViewActivityViewModel @Inject constructor(
    val context: Context
) : CustomViewModel() {

    private val mViewData = MutableLiveData<List<Any>>()
    val viewData = mViewData.distinctUntilChanged()

    init{

    }
}