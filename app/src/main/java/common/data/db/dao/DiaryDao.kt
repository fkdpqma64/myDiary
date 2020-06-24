package common.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import common.data.local.DiaryItem

@Dao
interface DiaryDao : BaseDao<DiaryItem> {
    @Query("SELECT * FROM diaryTable ORDER BY dateTime DESC")
    fun selectById(): DiaryItem

    @Query("SELECT * FROM diaryTable ORDER BY dateTime DESC")
    fun selectAll(): List<DiaryItem>
}