package common.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import common.data.db.dao.DiaryDao
import common.data.local.DiaryItem
import javax.inject.Inject

@Database(entities = [DiaryItem::class], version = 2, exportSchema = false)
abstract class DiaryDatabase: RoomDatabase() {
    abstract fun diaryDao(): DiaryDao

    companion object {
        private var INSTANCE: DiaryDatabase? = null

        fun getInstance(context: Context): DiaryDatabase? {
            if (INSTANCE == null) {
                synchronized(DiaryDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DiaryDatabase::class.java,
                        "your_db.db"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}