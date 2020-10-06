package com.dicoding.picodiploma.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }, 2000L)
    }
}
