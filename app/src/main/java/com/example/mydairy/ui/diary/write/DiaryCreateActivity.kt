package com.example.mydairy.ui.diary.write

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.mydairy.MainActivity
import com.example.mydairy.R
import com.example.mydairy.databinding.ActivityDiaryCreateBinding
import com.example.mydairy.databinding.ActivityMainBinding
import common.di.injector

class DiaryCreateActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, DiaryCreateActivity::class.java)
    }

    private lateinit var mBind: ActivityDiaryCreateBinding
    private val mViewModel by lazy { injector.diaryCreateViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_diary_create)
        customInit()
        setupEvents()
    }

    private fun customInit() {
        setSupportActionBar(mBind.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = null
        }

    }

    private fun setupEvents() {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.create_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.create_diary -> {
                if (mBind.txtDiaryTitle.text.isNotBlank() || mBind.txtDiaryContent.text.isNotBlank()) {
                    mViewModel.insertData( mViewModel.createDiary(
                        mBind.txtDiaryTitle.text.toString(),
                        mBind.txtDiaryContent.text.toString()
                    ))
                    finish()
                }else {
                    Toast.makeText(this, getString(R.string.need_diary_text), Toast.LENGTH_SHORT).show()
                }
            }
            android.R.id.home -> {
                if (mBind.txtDiaryTitle.text.isNotBlank() || mBind.txtDiaryContent.text.isNotBlank()) {
                    lateinit var dialog: AlertDialog
                    val builder = AlertDialog.Builder(this)
                    dialog = builder.apply {
                        setMessage("작성을 취소합니까?")
                        setPositiveButton("예") { _, _ ->
                            finish()
                        }
                        setNegativeButton("아니오") { _, _ ->
                            dialog.dismiss()
                        }
                    }.create()
                    dialog.show()
                } else {
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}