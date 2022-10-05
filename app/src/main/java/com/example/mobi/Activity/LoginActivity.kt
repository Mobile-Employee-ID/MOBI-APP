package com.example.mobi.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import com.example.mobi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.view_loader.*

private val loginButton: LottieAnimationView? = null
private val signUpButton: LottieAnimationView? = null

class LoginActivity : AppCompatActivity()
{

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth


        val logintext = findViewById<TextView>(R.id.loginText)
        val signText = findViewById<TextView>(R.id.signText)
        val passwordResetText = findViewById<TextView>(R.id.passwordResetText)
        val signinbutton = findViewById<LottieAnimationView>(R.id.signUpButton)
        val passwordResetButton = findViewById<LottieAnimationView>(R.id.gotoPasswordResetButton)
        logintext.bringToFront()
        signText.bringToFront()
        passwordResetText.bringToFront()

        initLoginButton()

        signinbutton.setOnClickListener {
            let {
                val intent = Intent(this, RegisterActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        passwordResetButton.setOnClickListener {
            let {
                val intent = Intent(this, PasswordResetActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
    }


    //로그인 버튼 함수
    private fun initLoginButton()
    {

        val loginButton = findViewById<LottieAnimationView>(R.id.loginButton)
        val logintext = findViewById<TextView>(R.id.loginText)
        loginButton?.setOnClickListener {

            //변수 선언
            val email = (findViewById<View>(R.id.emailEditText) as EditText).text.toString()
            val password = (findViewById<View>(R.id.passwordEditText) as EditText).text.toString()

            showProgress()
            //firebase 리스너 호출
            if (email.isNotEmpty() && password.isNotEmpty())
            {


                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        hideProgress()
                        if (task.isSuccessful)
                        {

                            Toast.makeText(
                                this,
                                "로그인에 성공하셨습니다", Toast.LENGTH_SHORT
                            ).show()
                            hideProgress()
                            myStartActivity(MainActivity::class.java)
                        } else
                        {

                            Toast.makeText(
                                this,
                                "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해 주세요", Toast.LENGTH_SHORT
                            ).show()

                            hideProgress()
                        }


                    }
            } else
            {

                Toast.makeText(
                    this,
                    "이메일 또는 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT
                ).show()

                hideProgress()
            }

        }
    }

    private fun showProgress() {
        findViewById<ProgressBar>(R.id.progressBar)?.isVisible = true
    }

    private fun hideProgress() {
        findViewById<ProgressBar>(R.id.progressBar)?.isVisible = false
    }

    // 액티비티 이동
    private fun myStartActivity(c: Class<*>)
    {
        val intent = Intent(this, c)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}
