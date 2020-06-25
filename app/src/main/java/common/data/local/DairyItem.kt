package common.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diaryTable")
data class DiaryItem(@PrimaryKey(autoGenerate = true) val id: Int?, val title: String, val contents: String?, val createTime: Long, val updateTime: Long, var bookMark: Boolean)