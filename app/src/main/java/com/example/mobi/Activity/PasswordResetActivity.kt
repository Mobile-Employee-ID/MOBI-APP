package com.example.mobi.Activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.mobi.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_password_reset.*

private val resetpassword: LottieAnimationView? = null

class PasswordResetActivity : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        resetText.bringToFront()
        auth = Firebase.auth
        val sendButton = findViewById<LottieAnimationView>(R.id.sendButton)
//        findViewById<View>(R.id.sendButton).setOnClickListener(onClickListener)
        val resetText = findViewById<TextView>(R.id.resetText)

        sendButton.setOnClickListener{
            send()
        }
    }
//    var onClickListener = View.OnClickListener { v ->
//        when (v.id)
//        {
//            R.id.sendButton -> send()
//        }
//    }

    private fun send()
    {
        val email = (findViewById<View>(R.id.emailEditText) as EditText).text.toString()
        if (email.length > 0)
        {
            val loaderLayout = findViewById<RelativeLayout>(R.id.loaderLayout)
            loaderLayout.visibility = View.VISIBLE
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                    loaderLayout.visibility = View.GONE
                    if (task.isSuccessful)
                    {
                        Toast.makeText(
                            this,
                            "이메일을 보냈습니다", Toast.LENGTH_SHORT
                        ).show()

                    }
                })
        } else
        {
            Toast.makeText(
                this,
                "이메일을 입력해 주세요", Toast.LENGTH_SHORT
            ).show()

        }
    }
}