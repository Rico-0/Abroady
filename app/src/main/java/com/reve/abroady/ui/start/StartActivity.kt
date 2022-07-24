package com.reve.abroady.ui.start

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.reve.abroady.R
import com.reve.abroady.databinding.ActivityStartBinding
import com.reve.abroady.ui.login.LoginSelectActivity

class StartActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)
        val handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                startActivity(Intent(this@StartActivity, LoginSelectActivity::class.java))
                finish()
            }
        }
        handler.sendEmptyMessageDelayed(0, 1000) // 1초 후 화면 전환
    }
}