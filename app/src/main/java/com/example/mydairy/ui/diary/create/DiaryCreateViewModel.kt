package com.example.mydairy.ui.diary.create

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import common.data.db.DiaryDatabase
import common.data.local.DiaryItem
import common.lib.livedata.CustomViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject


class DiaryCreateViewModel @Inject constructor(
    private val context: Context
) : CustomViewModel() {

    private val mViewData = MutableLiveData<List<Any>>()
    val viewData = mViewData.value

    init {

    }

    fun insertData(diaryItem: DiaryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("XXX", "${Thread.currentThread()}")
            runDataLoading {
                val diaryDatabase = DiaryDatabase.getInstance(context)
                diaryDatabase?.diaryDao()?.insert(diaryItem)
            }
        }
    }

    fun createDiary(title: String, content: String): DiaryItem {
        val date = ZonedDateTime.now().toEpochSecond()
        Log.d("XXX", "$title $content")
        if (title.isBlank())
            return DiaryItem(null,"오늘의 일기", content, date, date,false)
        return DiaryItem(null, title, content, date, date,false)
    }
}