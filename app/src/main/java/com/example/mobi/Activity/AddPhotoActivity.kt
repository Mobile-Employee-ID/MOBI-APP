package com.example.mobi.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.mobi.ArticleModel
import com.example.mobi.DBkey
import com.example.mobi.Fragment.SettingFragment
import com.example.mobi.Friend
import com.example.mobi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_setting.*

class AddPhotoActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private val fireStorage = FirebaseStorage.getInstance().reference
    private val fireDatabase = FirebaseDatabase.getInstance().reference
    private val user = Firebase.auth.currentUser
    private val uid = user?.uid.toString()

    private var selectedUri: Uri? = null //uri 변수호출
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }                                   //firebase 호출

    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        //사진 추가버튼 활성화시키기
        findViewById<Button>(R.id.imageAddButton).setOnClickListener {
            when {
                //사진 허가받고 사진올리는과정
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startContentProvider()
                }
                //교육용 팝업이 필요한 경우
                shouldShowRequestPermissionRationale(
                    android.Manifest.permission
                        .READ_EXTERNAL_STORAGE
                ) -> {
                    showPermissionContextPopup()
                }
                else -> {
                    //그외에 경우에는 해당권한에 대해서 요청하는 코드
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        1010
                    )
                }

            }
        }


        //사진 등록


        findViewById<Button>(R.id.submitButton).setOnClickListener {

            showProgress()

            // 중간에 이미지가 있으면 업로드 과정을 추가
            if (selectedUri != null) { //비동기일때도 함수실행
                val photoUri = selectedUri ?: return@setOnClickListener
                uploadPhoto(photoUri)

                hideProgress() //프로그래스 화면 없애기
                Toast.makeText(this, "프로필사진이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            } else { //동기일때도 함수 실행
                Toast.makeText(this, "사진 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun uploadPhoto(imageUri: Uri) {

        fireStorage.child("userImages/$uid/photo").putFile(
            imageUri!!
        ).addOnSuccessListener {
            fireStorage.child("userImages/$uid/photo")
                .downloadUrl.addOnSuccessListener {
                    val photoUri: Uri = it
                    println("$photoUri")
                    fireDatabase.child("users/$uid/profileImageUrl")
                        .setValue(photoUri.toString())

                }
        }
    }

    //아티클 업로드 함수
    private fun uploadArticle(imageUrl: String) {
        val model = Friend(imageUrl)
        Firebase.database.reference.child(name.toString()).push().setValue(model)

        hideProgress()

        finish()


    }

//    private val getContent =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            if (result.resultCode == RESULT_OK) {
//                imageUri = result.data?.data //이미지 경로 원본
//                profile.setImageURI(imageUri) //이미지 뷰를 바꿈
//
//                //기존 사진을 삭제 후 새로운 사진을 등록
//                // fireStorage.child("userImages/$uid/photo").delete().addOnSuccessListener {
//                fireStorage.child("userImages/$uid/photo").putFile(
//                    imageUri!!
//                ).addOnSuccessListener {
//                    fireStorage.child("userImages/$uid/photo")
//                        .downloadUrl.addOnSuccessListener {
//                            val photoUri: Uri = it
//                            println("$photoUri")
//                            fireDatabase.child("users/$uid/profileImageUrl")
//                                .setValue(photoUri.toString())
//                            Toast.makeText(this, "프로필사진이 변경되었습니다.", Toast.LENGTH_SHORT).show()
//                        }
//                }
//                //}
//                Log.d("이미지", "성공")
//            } else {
//                Log.d("이미지", "실패")
//            }
//        }

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
   // if (result.resultCode == RESULT_OK) {
//                imageUri = result.data?.data //이미지 경로 원본
//                profile.setImageURI(imageUri) //이미지 뷰를 바꿈
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

    //교육용 팝업 함수
    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기 위해 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1010)
            }
            .create()
            .show()

    }

}
