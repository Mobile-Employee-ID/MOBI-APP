package com.example.mobi.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Contacts.SettingsColumns.KEY
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mobi.ArticleModel
import com.example.mobi.Fragment.NoticeFragment
import com.example.mobi.R
import com.example.mobi.databinding.ActivityNoticeDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_notice_detail.*
import kotlinx.android.synthetic.main.fragment_setting.*
import java.text.SimpleDateFormat
import java.util.*

val nnoticeFragment = NoticeFragment()

class NoticeDetailActivity : AppCompatActivity() {

    companion object {
        private val auth: FirebaseAuth by lazy {
            Firebase.auth
        }

        private var imageUri: Uri? = null
        private val fireStorage = FirebaseStorage.getInstance().reference
        private val fireDatabase = FirebaseDatabase.getInstance().reference
        private val user = Firebase.auth.currentUser
        private val uid = user?.uid.toString()

    }

    private val articleList = mutableListOf<ArticleModel>() // 데이터 스냅샷을 통한 데이터 변경을 알기위해 아티클데이터 변수 설정
    private lateinit var binding: ActivityNoticeDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val format = SimpleDateFormat("MM월dd일 hh:mm:ss") //날짜를 가지고 오기 위한 포맷

        val title = getIntent().getStringExtra("title")
        val contents = getIntent().getStringExtra("contents")
        val photo = getIntent().getStringExtra("photo")
        val date = getIntent().getExtras()?.getLong("date")

        Log.d("date 데이터", "$date")
        val realdate = date?.let { Date(it) }
//        val format = SimpleDateFormat("MM월 dd일") //날짜를 가지고 오기 위한 포맷
//        val date = Date(articleModel.createdAt) //날짜로 데이터타입 변경
//
//        binding.titleTextView.text = articleModel.title
//        binding.dateTextView.text = format.format(date).toString()
        val photolayout = findViewById<ImageView>(R.id.coverImageView)
        binding.dateTextView.text = realdate?.let { format.format(it).toString() }
        Log.d("date 데이터222", "$date")
        binding.titleTextView.text = title.orEmpty()
        binding.descriptionTextView.text = contents.orEmpty()
        if (binding.coverImageView.toString().isNotEmpty()) {
            Glide.with(binding.coverImageView.context)
                .load(photo)
                .into(binding.coverImageView)
        }
        if (photo!!.isEmpty()) {
            photolayout.setVisibility(View.GONE)
        }


    }

//    @Override
//    override fun onBackPressed() {
//
//        finish()
//        articleList.clear()
//    }

//    @Override
//    override fun onPause() {
//        super.onPause()
//        articleList.clear()
//        Log.d("life_cycle", "onPause")
//    }

//    private fun exitApp() {
//        val i = Intent(this, NoticeFragment::class.java)
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        i.putExtra("EXIT_ROOT", true)
//        startActivity(i)
//    }

//
//    private fun replaceFragment(fragment : Fragment) {
//        supportFragmentManager.beginTransaction()
//            .apply {
//                replace(R.id.fragmentContainer, fragment)
//                commit()
//            }
//    }

}
