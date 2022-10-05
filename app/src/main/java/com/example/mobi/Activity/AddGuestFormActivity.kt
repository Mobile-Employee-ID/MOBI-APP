package com.example.mobi.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.mobi.ArticleModel
import com.example.mobi.DBkey.Companion.DB_ARTICLES
import com.example.mobi.DBkey.Companion.DB_FORMS
import com.example.mobi.GuestSignModel
import com.example.mobi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class AddGuestFormActivity : AppCompatActivity() {

    private var selectedUri: Uri? = null //uri 변수호출
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }                                   //firebase 호출

    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }

    private val guestformDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DB_FORMS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_guestsign)


        findViewById<Button>(R.id.guestsignSubmitButton).setOnClickListener {
            val title = findViewById<EditText>(R.id.guestsignTitleText).text.toString()
            val contents = findViewById<EditText>(R.id.guestsignContentsText).text.toString()
            val writerId = auth.currentUser?.uid.orEmpty()

            showProgress()


            uploadForm(writerId, title, contents)

        }

    }


    //신청서 업로드 함수
    private fun uploadForm(writerId: String, title: String, contents: String) {
        val model = GuestSignModel(writerId, title, System.currentTimeMillis(), contents)
        guestformDB.push().setValue(model)
        Toast.makeText(this, "출입 신청이 등록되었습니다 기록을 확인해보세요.", Toast.LENGTH_SHORT).show()
        hideProgress()

        finish()


    }
    
    //퍼미션 요청에 대한 함수 오버라이드
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1010 -> //승낙이 됬는지 확인한다
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startContentProvider()
                } else {
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun startContentProvider() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*" //이미지 타입만 가져오도록
        activityResultLauncher.launch(intent)

    }

    private fun showProgress() {
        findViewById<ProgressBar>(R.id.progressBar).isVisible = true
    }

    private fun hideProgress() {
        findViewById<ProgressBar>(R.id.progressBar).isVisible = false
    }

    //activityresultlauncher 처리하기 위한 설정
    private val activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        //result.getResultCode()를 통하여 결과값 확인
        if (it.resultCode == RESULT_OK) {
            //ToDo
            val uri = it.data?.data
            if (uri != null) {
                findViewById<ImageView>(R.id.photoImageView).setImageURI(uri)
                selectedUri = uri
            } else {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
        }
        if (it.resultCode == RESULT_CANCELED) {
            //ToDo
            return@registerForActivityResult
        }
    }

}


