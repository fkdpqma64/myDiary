package common.di

import android.app.Application
import android.content.Context
import com.example.mydairy.ui.diary.list.DiaryListViewModel
import com.example.mydairy.MainViewModel
import com.example.mydairy.ui.diary.write.DiaryCreateViewModel
import common.App
import common.data.db.DiaryDatabase
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 *  Dagger - component
 */
@Singleton
@Component(
    modules = [

    ]
)

interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application, @BindsInstance applicationContext: Context): AppComponent
    }

    val app: App

    val mainViewModel: MainViewModel
    val diaryListViewModel: DiaryListViewModel
    val diaryCreateViewModel: DiaryCreateViewModel

}