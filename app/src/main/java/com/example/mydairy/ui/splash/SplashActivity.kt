package com.example.mydairy.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.mydairy.R
import com.example.mydairy.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 스플래쉬 액티비티
 */
class SplashActivity : AppCompatActivity() {
    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SplashActivity::class.java)
        }
    }

    private lateinit var mBind: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        customInit()
        setupEvents()
    }

    private fun customInit() {
        lifecycleScope.launch {
            delay(2000)
            finish()
        }
    }

    private fun setupEvents() {

    }

    override fun finish() {
        overridePendingTransition(0, 0)
        super.finish()
    }
}
