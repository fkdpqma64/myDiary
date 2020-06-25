package com.example.mydairy.ui.diary.view.fragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import common.data.db.DiaryDatabase
import common.data.local.DiaryItem
import common.lib.livedata.CustomViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

class DiaryViewFragmentViewModel @Inject constructor(
    private val context: Context
) : CustomViewModel() {

    private val mViewData = MutableLiveData<DiaryItem>()
    val viewData = mViewData.distinctUntilChanged()

    private val diaryDatabase = DiaryDatabase.getInstance(context)

    init {

    }

    fun selectByIdData(id: Int) {

        if (isDataLoading()) {
            Log.d("XXX", "already DataLoading...")
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            Log.d("XXX", "Selecting data...")
            runDataLoading {
                mViewData.postValue(diaryDatabase?.diaryDao()?.selectById(id))
            }
        }
    }

    fun updateData(diaryItem: DiaryItem) {
        viewModelScope.launch(Dispatchers.Default) {
            Log.d("XXX", "Updating data...")
            runDataLoading {
                diaryDatabase?.diaryDao()?.update(diaryItem)
            }
        }
    }

    fun deleteData(id: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            Log.d("XXX", "deleting data...")
            runDataLoading {
                diaryDatabase?.diaryDao()?.deleteById(id)
            }
        }
    }

    fun updateDiary(diaryItem: DiaryItem, title: String, content: String): DiaryItem {
        val date = ZonedDateTime.now().toEpochSecond()
        Log.d("XXX", "$title $content")
        if (title.isBlank())
            return DiaryItem(
                diaryItem.id,
                "오늘의 일기",
                content,
                diaryItem.createTime,
                date,
                diaryItem.bookMark
            )
        return DiaryItem(
            diaryItem.id,
            title,
            content,
            diaryItem.createTime,
            date,
            diaryItem.bookMark
        )
    }

}