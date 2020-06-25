package com.example.mydairy.ui.diary.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mydairy.R
import com.example.mydairy.databinding.ActivityDiaryViewBinding
import com.example.mydairy.ui.diary.view.fragment.DiaryViewFragment
import common.di.injector

class DiaryViewActivity : AppCompatActivity() {
    companion object {
        const val FRAGMENT_MODE_VIEW_DIARY = "FRAGMENT_MODE_VIEW_DIARY"
        const val EXTRA_DIARY_ITEM_ID = "EXTRA_DIARY_ITEM_ID"

        fun createIntent(context: Context) = Intent(context, DiaryViewActivity::class.java)

    }

    var mFragmentMode = FRAGMENT_MODE_VIEW_DIARY
    private var mDiaryId = 1

    lateinit var mBind: ActivityDiaryViewBinding
    private val mViewModel by lazy { injector.diaryViewActivityViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_diary_view)
        mDiaryId = intent.getIntExtra(EXTRA_DIARY_ITEM_ID, 1)
        customInit()
        setupEvents()
    }

    private fun customInit() {
        setSupportActionBar(mBind.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = null
        }

        replaceFragment()
    }

    private fun setupEvents() {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_view_diary, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun replaceFragment() {
        val bundle = Bundle()
        bundle.putInt(EXTRA_DIARY_ITEM_ID, mDiaryId)
        when (mFragmentMode) {
            FRAGMENT_MODE_VIEW_DIARY -> {
                val instance = DiaryViewFragment.newInstance()
                instance.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_diary_view, instance).commit()
            }
        }

    }
}