package com.example.mobi.Fragment

import android.annotation.SuppressLint
import android.app.ProgressDialog.show
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mobi.*
import com.example.mobi.Activity.MessageActivity
import com.example.mobi.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_setting.*
import java.lang.Exception

class FriendFragment : Fragment() {
    companion object {
        fun newInstance(): FriendFragment {
            return FriendFragment()
        }
    }

    private lateinit var database: DatabaseReference
    private var friend: ArrayList<Friend> = arrayListOf()
    //메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    //프레그먼트를 포함하고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    //뷰가 생성되었을 때
    //프레그먼트와 레이아웃을 연결시켜주는 부분
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        database = Firebase.database.reference
        val view = inflater.inflate(R.layout.fragment_friend, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.friendRecyclerView)
        //this는 액티비티에서 사용가능, 프래그먼트는 requireContext()로 context 가져오기
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = RecyclerViewAdapter()

        return view
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>() {

        init {
            val myUid = Firebase.auth.currentUser?.uid.toString()
            FirebaseDatabase.getInstance().reference.child("users").addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    friend.clear()
                    for (data in snapshot.children) {
                        val item = data.getValue<Friend>()
                        if (item?.uid.equals(myUid)) {
                            continue
                        } // 본인은 친구창에서 제외
                        friend.add(item!!)
                    }
                    notifyDataSetChanged()
                }
            })
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            return CustomViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.item_friend, parent,
                    false
                )
            )
        }

        inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun from(friend: Friend) {

            }

            val imageView: ImageView = itemView.findViewById(R.id.friend_item_iv)
            val textView: TextView = itemView.findViewById(R.id.friend_item_tv)
            val textViewEmail: TextView = itemView.findViewById(R.id.friend_item_email)
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            Glide.with(holder.itemView.context).load(friend[position].profileImageUrl)
                .apply(RequestOptions().circleCrop())
                .into(holder.imageView)
            holder.textView.text = friend[position].name
            holder.textViewEmail.text = friend[position].email

            val holderPhoto = friend[position].profileImageUrl
            val holdername = friend[position].name
            val holderEmail = friend[position].email
            holder.from(friend[position])
            val adapteruid = friend[position].uid

            holder.itemView.setOnClickListener {
//                val intent = Intent(context, MessageActivity::class.java)
//                intent.putExtra("destinationUid", friend[position].uid)
//                context?.startActivity(intent)
                activity?.let {
                    //val dialog = ProfileDialog()
                    val coolDialog = CoolDialog(requireContext())
                    coolDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    coolDialog.requestContentView()
                    coolDialog.setCanceledOnTouchOutside(false)
                    val dialogWindow = coolDialog.window
                    dialogWindow?.setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT)

                    coolDialog.setTextOnFirstTextView(holdername.toString())
                    coolDialog.setTextOnSecondTextView(holderEmail.toString())
                    //coolDialog.setTextToAll("Edin Hasanović", "edin.hasanovic@sourecode.ba", " +
                    // ""+38762484877", "Slavinovići bb")
                    coolDialog.setImageSize(150)
                    coolDialog.setImageResource(holderPhoto.toString())

                    coolDialog.setCallButtonIconResource(R.drawable.ic_baseline_chat_24)
                    coolDialog.setCallButtonIconColor(android.R.color.holo_blue_bright)
                    coolDialog.setCallButtonText("Send Message")

                    coolDialog.setCallButtonOnClickListener()  {
                        val intent = Intent(context, MessageActivity::class.java)
                        intent.putExtra("destinationUid", adapteruid)
                        context?.startActivity(intent)
                    }
                    coolDialog.setCancelButtonOnClickListener() {
                        Toast.makeText(requireContext(), "취소했습니다.", Toast.LENGTH_SHORT).show()
                        coolDialog.dismiss()
                    }

                    coolDialog.show()

//                    dialog.setButtonClickListener(object : ProfileDialog.OnButtonClickListener {
//                        override fun onButton1Clicked() {
//                            Toast.makeText(requireContext(), "취소했습니다.", Toast.LENGTH_SHORT)
//                                .show()
//
//                        }
//
//                        override fun onButton2Clicked() {
//                            val intent = Intent(context, MessageActivity::class.java)
//                            intent.putExtra("destinationUid", adapteruid)
//                            context?.startActivity(intent)
//                        }
//                    })
//                    dialog.show(requireActivity().getSupportFragmentManager(), "CustomDialog")
                }
            }
        }

        override fun getItemCount(): Int {
            return friend.size
        }
    }
}

