package com.example.mydairy.ui.diary.list.bookmark

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import common.BuildVar
import common.data.db.DiaryDatabase
import common.data.local.DiaryItem
import common.lib.livedata.CustomViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiaryBookmarkListViewModel @Inject constructor(
    private val context: Context
) : CustomViewModel() {

    private val mViewData = MutableLiveData<List<Any>>()
    val viewData = mViewData.distinctUntilChanged()
    private var mPageOffset = BuildVar.PAGE_LIMIT
    private val diaryDatabase = DiaryDatabase.getInstance(context)

    init {

    }

    fun refreshViewData() {

        if (isDataLoading()) {
            Log.d("XXX", "already DataLoading...")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("XXX", "Refreshnig Data...")
            runDataLoading {
                mPageOffset = BuildVar.PAGE_LIMIT
                mViewData.postValue(diaryDatabase?.diaryDao()?.selectAllBookMarkPage(0))
            }
        }
    }

    fun scrollViewData() {

        if (isDataLoading()) {
            Log.d("XXX", "already DataLoading...")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("XXX", "Scrolling...")
            runDataLoading {
                val preData = viewData.value as MutableList<DiaryItem>
                diaryDatabase?.diaryDao()?.selectAllBookMarkPage(mPageOffset)?.forEach {
                    preData.add(it)
                }
                Log.d("XXX", "${preData.count()}")
                mViewData.postValue(preData)
                mPageOffset += BuildVar.PAGE_LIMIT
            }
        }
    }

}





