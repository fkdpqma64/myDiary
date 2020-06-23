package com.example.mydairy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.android.newsapp.newslist.DiaryListFragment
import com.example.mydairy.databinding.ActivityMainBinding
import com.example.mydairy.ui.splash.SplashActivity
import common.di.injector

class MainActivity : AppCompatActivity() {
    companion object {

        const val EXTRA_LAUNCH_FROM_SPLASH = "EXTRA_LAUNCH_FROM_SPLASH"

        fun createIntent(context: Context) = Intent(context, MainActivity::class.java)

        fun createIntentFromSplash(context: Context) =
            Intent(context, MainActivity::class.java).also {
                it.putExtra(EXTRA_LAUNCH_FROM_SPLASH, true)
            }

        // 백키 눌렀을때 한번 더 눌러야 종료되도록
        private const val DELAY_FINISH_TIMEOUT_MILLIS = 2 * 1000
    }


    /**
     * 백키를 두번 눌러야 앱을 종료 - 마지막으로 백키를 누른 시간
     */
    private var mLastBackKeyPressedTime = 0L

    private var mSplashVisited = false
    private lateinit var mBind: ActivityMainBinding

    private val mViewModel by lazy { injector.mainViewModel }
    private val mApp by lazy { injector.app }

    override fun onCreate(savedInstanceState: Bundle?) {
        val launchFromLink = mApp.backupMainIntent(intent)

        /**
         * 스플래쉬 화면 실행
         */
        if (!launchFromLink && checkNeedSplashVisit(mSplashVisited)) {
            super.onCreate(savedInstanceState)
            finish()
            startActivity(
                SplashActivity.createIntent(this).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
            return
        }

        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        customInit()
        setupEvents()

    }

    private fun customInit() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.diary_list_fragment, DiaryListFragment.newInstance()).commit()
    }

    /**
     * 스플래쉬 화면 방문 체크
     */
    private fun checkNeedSplashVisit(mSplashVisited: Boolean): Boolean {
        if (mSplashVisited)
            return false

        val fromSplash = intent?.getBooleanExtra(EXTRA_LAUNCH_FROM_SPLASH, false) == true
        return !fromSplash
    }

    private fun setupEvents() {

    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            return
        }

        val now = System.currentTimeMillis()
        if (now - mLastBackKeyPressedTime > DELAY_FINISH_TIMEOUT_MILLIS) {
            mLastBackKeyPressedTime = now
            Toast.makeText(this, R.string.msg_one_more_back_button, Toast.LENGTH_SHORT).show()
            return
        }

        super.onBackPressed()
    }
}