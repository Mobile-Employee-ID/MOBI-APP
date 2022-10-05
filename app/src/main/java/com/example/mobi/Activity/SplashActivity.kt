package com.example.mobi.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.mobi.R
import kotlinx.android.synthetic.main.activity_splash.*

private val identitycard: LottieAnimationView? = null

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val clicktext = findViewById<TextView>(R.id.splash_text)
        val hellotext = findViewById<TextView>(R.id.splash_welcome)

        val anim = AnimationUtils.loadAnimation(this,R.anim.blink_animation)

//        Handler(Looper.getMainLooper()).postDelayed({
//            val intent = Intent(this, LoginActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//            startActivity(intent)
//            finish()
//        }, 3000)

        clicktext.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
        clicktext.startAnimation(anim)
       // hellotext.startAnimation(anim)

    }
}