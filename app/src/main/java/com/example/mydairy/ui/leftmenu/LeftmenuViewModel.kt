package com.example.mydairy.ui.leftmenu

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import common.data.local.MenuTextValueItem
import common.lib.livedata.CustomViewModel
import javax.inject.Inject

class LeftmenuViewModel @Inject constructor(
    private val context: Context
) : CustomViewModel() {

    private val mViewData = MutableLiveData<List<Any>>()
    val viewData = mViewData.distinctUntilChanged()

    init {
        createMenuItem()
    }

    private fun createMenuItem() {
        mViewData.postValue(
            listOf(
                MenuTextValueItem(1, "목록", "목록"),
                MenuTextValueItem(2, "북마크", "북마크")
            )
        )
    }

}





