package com.example.mobi.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mobi.Fragment.*
import com.example.mobi.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth

var homeFragment = HomeFragment()
val noticeFragment = NoticeFragment()
val settingsFragment = SettingFragment()
val friendFragment = FriendFragment()
val chatFragment = ChatFragment()

class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        replaceFragment(homeFragment)

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.friend -> replaceFragment(friendFragment)
                R.id.chat -> replaceFragment(chatFragment)
                R.id.home -> replaceFragment(homeFragment)
                R.id.notice -> replaceFragment(noticeFragment)
                R.id.settings -> replaceFragment(settingsFragment)
            }
            true
        }


    }

    private fun replaceFragment(fragment: Fragment)
    {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer, fragment)
                commit()
            }
    }
}