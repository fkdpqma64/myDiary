package com.example.mydairy.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.mydairy.R
import com.example.mydairy.databinding.ActivitySplashBinding
import com.example.mydairy.ui.splash.SplashActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(){
    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    private lateinit var mBind: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_login)
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