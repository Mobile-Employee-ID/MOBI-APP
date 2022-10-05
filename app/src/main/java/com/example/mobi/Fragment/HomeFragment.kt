package com.example.mobi.Fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mobi.Activity.BluetoothActivity2
import com.example.mobi.Activity.CalendarActivity
import com.example.mobi.Friend
import com.example.mobi.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_home.*
private val calendarView: LottieAnimationView? = null
class HomeFragment : Fragment(R.layout.fragment_home) {
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

    //메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //프레그먼트를 포함하고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //view 선언을 안하고 return에 바로 적용시키면 glide가 작동을 안함
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val photo = view?.findViewById<ImageView>(R.id.profile_imageview)

        val email = view?.findViewById<TextView>(R.id.profile_textview_email)
        val name = view?.findViewById<TextView>(R.id.profile_textview_name)
        val calendar = view?.findViewById<LottieAnimationView>(R.id.calenderButton)
        val startText = view?.findViewById<TextView>(R.id.startText)
        val AccessButton = view?.findViewById<LottieAnimationView>(R.id.accessButton)



//        startText?.bringToFront()
        //프로필 구현
        fireDatabase.child("users").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val userProfile = snapshot.getValue<Friend>()
                    println(userProfile)
                    Glide.with(requireContext()).load(userProfile?.profileImageUrl)
                        .apply(RequestOptions().circleCrop())
                        .into(photo!!)
                    email?.text = userProfile?.email
                    name?.text = userProfile?.name
                }
            })

        //캘린더 가는 버튼
        calendar?.setOnClickListener {
            context?.let {


                if (auth.currentUser != null) {
                    val intent = Intent(requireContext(), CalendarActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                } else {
                    Snackbar.make(view, "로그인 후 캘린더를 이용해 주세요", Snackbar.LENGTH_LONG).show()
                }


            }
        }
        //블루투스 실행 버튼
        startText?.setOnClickListener {
            context?.let {
                if (auth.currentUser != null) {
                    val intent = Intent(requireContext(), BluetoothActivity2::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }

        }

                return view
            }

}