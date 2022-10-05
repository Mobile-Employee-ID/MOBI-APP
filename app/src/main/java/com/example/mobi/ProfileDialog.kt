package com.example.mobi

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.mobi.databinding.CustomDialogBinding
import com.example.mobi.databinding.ProfileDialogBinding
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class ProfileDialog : DialogFragment() {

    private var _binding: ProfileDialogBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null
    private val fireStorage = FirebaseStorage.getInstance().reference
    private val fireDatabase = FirebaseDatabase.getInstance().reference
    private val user = Firebase.auth.currentUser
    private val uid = user?.uid.toString()
    // private val edit = dialog!!.findViewById<EditText>(R.id.name_edit)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ProfileDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        // dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val cancel = view?.findViewById<MaterialButton>(R.id.btn_cancel)
        val message = view?.findViewById<MaterialButton>(R.id.btn_call)

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
        cancel?.setOnClickListener {
            buttonClickListener.onButton1Clicked()
            dismiss()    // 대화상자를 닫는 함수
        }
        message?.setOnClickListener {
            buttonClickListener.onButton2Clicked()
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
        fun onButton2Clicked()
    }
    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }
    // 클릭 이벤트 실행
    private lateinit var buttonClickListener: OnButtonClickListener
}
