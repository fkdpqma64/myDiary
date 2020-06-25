package common.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import common.data.local.DiaryItem

@Dao
interface DiaryDao : BaseDao<DiaryItem> {
    @Query("SELECT * FROM diaryTable WHERE id = :id")
    fun selectById(id: Int): DiaryItem

    @Query("SELECT * FROM diaryTable ORDER BY createTime DESC")
    fun selectAll(): List<DiaryItem>

    @Query("DELETE FROM diaryTable WHERE id = :id")
    fun deleteById(id: Int)
}