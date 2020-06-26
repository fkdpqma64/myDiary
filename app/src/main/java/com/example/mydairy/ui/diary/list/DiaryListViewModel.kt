package com.example.mydairy.ui.diary.list

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

class DiaryListViewModel @Inject constructor(
    private val context: Context
) : CustomViewModel() {

    private val mViewData = MutableLiveData<List<Any>>()
    val viewData = mViewData.distinctUntilChanged()
    private var mPageOffset = BuildVar.PAGELIMIT
    private val diaryDatabase = DiaryDatabase.getInstance(context)
    init {

    }

    fun refreshViewData() {

        if (isDataLoading()) {
            Log.d("XXX", "already DataLoading...")
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            Log.d("XXX", "Refreshnig Data...")
            runDataLoading {
                mPageOffset = BuildVar.PAGELIMIT
                mViewData.postValue(diaryDatabase?.diaryDao()?.selectAllPage(0))
            }
        }
    }

    fun scrollViewData() {

        if (isDataLoading()) {
            Log.d("XXX", "already DataLoading...")
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            Log.d("XXX", "Scrolling...")
            runDataLoading {
                val previewData = viewData.value as MutableList<DiaryItem>
                    diaryDatabase?.diaryDao()?.selectAllPage(mPageOffset)?.forEach {
                        previewData.add(it)
                    }
                Log.d("XXX", "${previewData.count()}")
                mViewData.postValue(previewData)
                mPageOffset += BuildVar.PAGELIMIT
            }
        }
    }

}





