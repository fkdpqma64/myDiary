package com.example.mydairy

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.example.mydairy.databinding.ActivityMainBinding
import com.example.mydairy.ui.diary.create.DiaryCreateActivity
import com.example.mydairy.ui.diary.list.DiaryListFragment
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
    private var mDrawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val launchFromLink = mApp.backupMainIntent(intent)

        /**
         * 스플래쉬 화면 실행
         */
        if (!launchFromLink && checkNeedSplashVisit(mSplashVisited)) {
            startActivity(
                SplashActivity.createIntent(this).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
        }

        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        customInit()
        setupEvents()

    }

    private fun customInit() {
        setSupportActionBar(mBind.toolbar)
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            title = null
            elevation = 0f
        }
        setupDrawerLayout()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_diary_list, DiaryListFragment.newInstance()).commit()
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

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDrawerToggle?.syncState()
    }

    private fun setupDrawerLayout() {
        mBind.navView.visibility = View.VISIBLE
        val toggle = ActionBarDrawerToggle(
            this,
            mBind.drawerLayout,
            mBind.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        mBind.drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()
        mDrawerToggle = toggle
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mDrawerToggle?.onOptionsItemSelected(item) == true)
            return true

        when (item.itemId) {
            android.R.id.home -> {
                if (mBind.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mBind.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    mBind.drawerLayout.openDrawer(GravityCompat.START)
                }
                return true
            }

            R.id.create_diary -> {
                startActivity(
                    DiaryCreateActivity.createIntent(this)
                        .addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle?.onConfigurationChanged(newConfig)
    }

    override fun onBackPressed() {
        if (mBind.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBind.drawerLayout.closeDrawer(GravityCompat.START)
            return
        }

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