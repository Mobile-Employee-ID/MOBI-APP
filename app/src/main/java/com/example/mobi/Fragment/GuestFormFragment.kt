package com.example.mobi.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mobi.Activity.AddArticleActivity
import com.example.mobi.Activity.AddGuestFormActivity
import com.example.mobi.Activity.MessageActivity
import com.example.mobi.Adapter.ArticleAdapter
import com.example.mobi.Adapter.SignFormAdapter
import com.example.mobi.ArticleModel
import com.example.mobi.DBkey
import com.example.mobi.GuestSignModel
import com.example.mobi.R
import com.example.mobi.databinding.FragmentGuestFormBinding
import com.example.mobi.databinding.FragmentNoticeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.core.RepoManager.clear
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class GuestFormFragment : Fragment() {

    companion object {
        fun newInstance(): GuestFormFragment {
            return GuestFormFragment()
        }
    }

    private lateinit var database: DatabaseReference
    private var guestform: ArrayList<GuestSignModel> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val createdAt = System.currentTimeMillis()
//        val dateFormat = SimpleDateFormat("MM월dd일 hh:mm")
//        val curTime = dateFormat.format(Date(createdAt)).toString()




        database = Firebase.database.reference
        val view = inflater.inflate(R.layout.fragment_guest_form, container, false)
        val recyclerView =
            view.findViewById<RecyclerView>(R.id.guestformRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = RecyclerViewAdapter()
        (recyclerView.layoutManager as LinearLayoutManager).reverseLayout = true
        (recyclerView.layoutManager as LinearLayoutManager).stackFromEnd = true


        return view
    }

    inner class RecyclerViewAdapter :
        RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder>() {

        init {
            val myUid = Firebase.auth.currentUser?.uid.toString()

            FirebaseDatabase.getInstance().reference.child("Profiles")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        guestform.clear()
                        for (data in snapshot.children) {
                            val item = data.getValue<GuestSignModel>()
                            guestform.add(item!!)
                        }
                        notifyDataSetChanged()
                    }
                })
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RecyclerViewAdapter.CustomViewHolder {

            return CustomViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.item_guestform,
                    parent, false
                )
            )
        }

        inner class CustomViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {


            val title: TextView = itemView.findViewById(R.id.titleText)
            val date: TextView = itemView.findViewById(R.id.dateText)
            val contents: TextView = itemView.findViewById(R.id.contentsText)
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

           val createdAt = guestform[position].createdAt
           // val createdAt = System.currentTimeMillis()
//        val dateFormat = SimpleDateFormat("MM월dd일 hh:mm")
//        val curTime = dateFormat.format(Date(createdAt)).toString()


            val dateformat = SimpleDateFormat("MM월dd일 hh:mm:ss") //날짜를 가지고 오기 위한 포맷
            val date = Date(createdAt)

            holder.title.text = guestform[position].title
            holder.date.text = dateformat.format(createdAt)
            holder.contents.text = guestform[position].contents


        }

        override fun getItemCount(): Int {
            return guestform.size
        }

    }
}



