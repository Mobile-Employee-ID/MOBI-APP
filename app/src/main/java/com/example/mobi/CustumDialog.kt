package com.example.mobi

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mobi.Fragment.HomeFragment
import com.example.mobi.Fragment.SettingFragment
import com.example.mobi.databinding.CustomDialogBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.custom_dialog.*
import kotlinx.android.synthetic.main.fragment_setting.*

class CustomDialog : DialogFragment() {
    private var _binding: CustomDialogBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null
    private val fireStorage = FirebaseStorage.getInstance().reference
    private val fireDatabase = FirebaseDatabase.getInstance().reference
    private val user = Firebase.auth.currentUser
    private val uid = user?.uid.toString()
   // private val edit = dialog!!.findViewById<EditText>(R.id.name_edit)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = CustomDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
       // dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val name = view?.findViewById<TextView>(R.id.name_edit)
        fireDatabase.child("users").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val userProfile = snapshot.getValue<Friend>()
                    println(userProfile)
                    name?.text = userProfile?.name
                }
            })
        binding.cancelButton.setOnClickListener {
            buttonClickListener.onButton1Clicked()
            dismiss()    // 대화상자를 닫는 함수
        }
        binding.finishButton.setOnClickListener {
            buttonClickListener.onButton2Clicked(name?.text.toString())
            dismiss()
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnButtonClickListener {
        fun onButton1Clicked()
        fun onButton2Clicked(nameEdit:String)
    }
    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }
    // 클릭 이벤트 실행
    private lateinit var buttonClickListener: OnButtonClickListener
}
