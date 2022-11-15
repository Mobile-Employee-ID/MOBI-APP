package com.example.mobi.Fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mobi.Activity.*
import com.example.mobi.CustomDialog
import com.example.mobi.Friend
import com.example.mobi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_setting.*


class SettingFragment : Fragment(R.layout.fragment_setting) {
    //lateinit var binding: FragmentSettingBinding
    companion object {
        private val auth: FirebaseAuth by lazy {
            Firebase.auth
        }

        private var imageUri: Uri? = null
        private val fireStorage = FirebaseStorage.getInstance().reference
        private val fireDatabase = FirebaseDatabase.getInstance().reference
        private val user = Firebase.auth.currentUser
        private val uid = user?.uid.toString()
        fun newInstance(): ChatFragment {
            return ChatFragment()
        }
    }

    //메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //프레그먼트를 포함하고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    //사진 등록
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if(result.resultCode == AppCompatActivity.RESULT_OK) {
                imageUri = result.data?.data //이미지 경로 원본
                profile.setImageURI(imageUri) //이미지 뷰를 바꿈

                //기존 사진을 삭제 후 새로운 사진을 등록
               // fireStorage.child("userImages/$uid/photo").delete().addOnSuccessListener {
                    fireStorage.child("userImages/$uid/photo").putFile(imageUri!!).addOnSuccessListener {
                        fireStorage.child("userImages/$uid/photo").downloadUrl.addOnSuccessListener {
                            val photoUri : Uri = it
                            println("$photoUri")
                            fireDatabase.child("users/$uid/profileImageUrl").setValue(photoUri.toString())
                            Toast.makeText(requireContext(), "프로필사진이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                //}
                Log.d("이미지", "성공")
            }
            else{
                Log.d("이미지", "실패")
            }
        }

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        val photo = view?.findViewById<ImageView>(R.id.profile)
        val photoedit = view?.findViewById<ImageView>(R.id.photoedit)
        val email = view?.findViewById<TextView>(R.id.email)
        val name = view?.findViewById<TextView>(R.id.name)
        val grant = view?.findViewById<TextView>(R.id.AccessGrant)

        val gatePassID = fireDatabase.child("gatePassID").child(uid)
        val userID = fireDatabase.child("users").child(uid)
        val gateApprove =
            fireDatabase.child("gatePassID").child("11").child(uid).child("access").toString()


        println(gatePassID)
        println(userID)
        println(gateApprove)
        //프로필 구현
        fireDatabase.child("users").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val userProfile = snapshot.getValue<Friend>()
                    println(userProfile)
                    Glide.with(photo!!.context)
                        .load(userProfile?.profileImageUrl)
                        .apply(RequestOptions().circleCrop())
                        .into(photo)
                    email?.text = userProfile?.email
                    name?.text = userProfile?.name
                    grant?.text = userProfile?.gategrant
                }
            })
//            .child("gatePassID").child("11").child(uid).child("access")
        fireDatabase
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
//                    val userProfile2 = snapshot.getValue<Friend>()
//                    println(userProfile2)

                    var userProfile2 = snapshot.child("users").child(uid).getValue<Friend>()
                    var value : String? = snapshot.child("gatePassID").child("11").child(uid).child("access").getValue(String::class.java) as? String
                    var value1 : String? = snapshot.child("gatePassID").child("12").child(uid).child("access").getValue(String::class.java) as? String
                    var value2 : String? = snapshot.child("gatePassID").child("13").child(uid).child("access").getValue(String::class.java) as? String
                    println(value)
                    if ( value == "Approval") {
//gatePassID == userID &&

                        userProfile2?.gategrant = "11번 게이트 출입 승인"
                        grant?.text = userProfile2?.gategrant
//                        userProfile2?.gategrant = "승인 허가"
//                        grant?.text = userProfile2?.gategrant
                            println("okay")

                        fireDatabase.child("users").child(uid).child("gategrant")
                            .setValue(userProfile2?.gategrant) //저장
                    }else if(value != "Approval" && value1 != "Approval" && value2 != "Approval"){
                        userProfile2?.gategrant =  "출입 거부"
                        grant?.text = userProfile2?.gategrant
                        println("not okay")

                        fireDatabase.child("users").child(uid).child("gategrant").setValue(userProfile2?.gategrant)

                    }
                    if (  value1 == "Approval" ) {
//gatePassID == userID &&

                        userProfile2?.gategrant = "12번 게이트 출입 승인"
                        grant?.text = userProfile2?.gategrant
//                        userProfile2?.gategrant = "승인 허가"
//                        grant?.text = userProfile2?.gategrant
                        println("okay")

                        fireDatabase.child("users").child(uid).child("gategrant")
                            .setValue(userProfile2?.gategrant) //저장
                    }
                    if (  value2 == "Approval") {
//gatePassID == userID &&

                        userProfile2?.gategrant = "13번 게이트 출입 승인"
                        grant?.text = userProfile2?.gategrant
//                        userProfile2?.gategrant = "승인 허가"
//                        grant?.text = userProfile2?.gategrant
                        println("okay")

                        fireDatabase.child("users").child(uid).child("gategrant")
                            .setValue(userProfile2?.gategrant) //저장
                    }
                    if (  value == "Approval" && value1 == "Approval") {
//gatePassID == userID &&

                        userProfile2?.gategrant = "11번 12번 게이트 출입 승인"
                        grant?.text = userProfile2?.gategrant
//                        userProfile2?.gategrant = "승인 허가"
//                        grant?.text = userProfile2?.gategrant
                        println("okay")

                        fireDatabase.child("users").child(uid).child("gategrant")
                            .setValue(userProfile2?.gategrant) //저장
                    }
                    if (  value == "Approval" && value2 == "Approval") {
//gatePassID == userID &&

                        userProfile2?.gategrant = "11번 13번 게이트 출입 승인"
                        grant?.text = userProfile2?.gategrant
//                        userProfile2?.gategrant = "승인 허가"
//                        grant?.text = userProfile2?.gategrant
                        println("okay")

                        fireDatabase.child("users").child(uid).child("gategrant")
                            .setValue(userProfile2?.gategrant) //저장
                    }
                    if (  value1 == "Approval" && value2 == "Approval") {
//gatePassID == userID &&

                        userProfile2?.gategrant = "12번 13번 게이트 출입 승인"
                        grant?.text = userProfile2?.gategrant
//                        userProfile2?.gategrant = "승인 허가"
//                        grant?.text = userProfile2?.gategrant
                        println("okay")

                        fireDatabase.child("users").child(uid).child("gategrant")
                            .setValue(userProfile2?.gategrant) //저장
                    }
                    if (  value == "Approval" && value1 == "Approval" && value2 == "Approval") {
//gatePassID == userID &&

                        userProfile2?.gategrant = "11번 12번 13번 게이트 출입 승인"
                        grant?.text = userProfile2?.gategrant
//                        userProfile2?.gategrant = "승인 허가"
//                        grant?.text = userProfile2?.gategrant
                        println("okay")

                        fireDatabase.child("users").child(uid).child("gategrant")
                            .setValue(userProfile2?.gategrant) //저장
                    }

                }
                override fun onCancelled(databaseError: DatabaseError) {
                    //Log.e("MainActivity", String.valueOf(databaseError.toException()));
                }
            })
        //프로필 사진 변경
//        photoedit?.setOnClickListener {
//            val intentImage = Intent(Intent.ACTION_PICK)
//            intentImage.type = "image/*"
//            getContent.launch(intentImage)
//        }
        photoedit?.setOnClickListener {
            activity?.let {
                val intent = Intent(context, AddPhotoActivity::class.java)
                startActivity(intent)
            }
        }


        view?.findViewById<TextView>(R.id.signoutButton)?.setOnClickListener {
            activity?.let {
                FirebaseAuth.getInstance().signOut()

                var logoutintent = Intent(context, LoginActivity::class.java)
                logoutintent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent
                    .FLAG_ACTIVITY_NEW_TASK

                startActivity(logoutintent)
            }
        }

        //커스텀 다이얼로그 구현
        view?.findViewById<TextView>(R.id.nameEditText)?.setOnClickListener {
            activity?.let {
                val dialog = CustomDialog()
                dialog.setButtonClickListener(object : CustomDialog.OnButtonClickListener {
                    override fun onButton1Clicked() {
                        Toast.makeText(requireContext(), "취소했습니다.", Toast.LENGTH_SHORT)
                            .show()

                    }

                    override fun onButton2Clicked(nameEdit: String) {
                        name?.text = nameEdit
                        if (name?.text!!.isNotEmpty()) {
                            fireDatabase.child("users/${uid}/name").setValue(
                                name.text.toString()
                            )
                        }
                        name.clearFocus()
                        Toast.makeText(requireContext(), "이름이 변경되었습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
                dialog.show(requireActivity().getSupportFragmentManager(), "CustomDialog")
            }
        }


        //출입 신청서 작성
        view?.findViewById<TextView>(R.id.guest_signin)?.setOnClickListener {
            activity?.let {
                val intent = Intent(context, AddGuestFormActivity::class.java)
                startActivity(intent)
            }
        }
        //출입 신청서 작성
        view?.findViewById<TextView>(R.id.callendar_btn)?.setOnClickListener {
            activity?.let {
                val intent = Intent(context, CalendarActivity::class.java)
                startActivity(intent)
            }
        }
        //출입신청기록 확인
        view?.findViewById<TextView>(R.id.guest_state)?.setOnClickListener {
            activity?.let {
                val intent = Intent(context, GuestFormStateActivity::class.java)
                startActivity(intent)
            }
        }

        return view
    }


    override fun onResume() {
        super.onResume()

    }


}
