package com.example.mobi.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobi.Activity.AddPhotoActivity
import com.example.mobi.Activity.GuestFormStateActivity
import com.example.mobi.Activity.MessageActivity
import com.example.mobi.Activity.NoticeDetailActivity
import com.example.mobi.Adapter.SignFormAdapter.Companion.diffUtil
import com.example.mobi.ArticleModel
import com.example.mobi.databinding.ItemArticleBinding
import com.google.firebase.database.ChildEventListener
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter(private val itemClickedlistener : (ArticleModel) -> Unit ) :
    ListAdapter<ArticleModel,
            ArticleAdapter
            .ViewHolder>
        (diffUtil) {

    inner class ViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(articleModel: ArticleModel) {

            val format = SimpleDateFormat("MM월 dd일") //날짜를 가지고 오기 위한 포맷
            val date = Date(articleModel.createdAt) //날짜로 데이터타입 변경

//            binding.root.setOnClickListener {
//                itemClickedlistener(articleModel)
//            }
            binding.titleTextView.text = articleModel.title
            binding.dateTextView.text = format.format(date).toString()
            binding.contentsTextView.text = articleModel.contents

            if (articleModel.imageUrl.isNotEmpty()) {
                Glide.with(binding.thumbnailImageView)
                    .load(articleModel.imageUrl)
                    .into(binding.thumbnailImageView)
            }

            binding.root.setOnClickListener {
                itemClickedlistener(articleModel)
            }


        }

    }

    //아티클 어댑터 뷰홀더 생성함수 오버라이드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    // 바인드 뷰홀더 함수 오버라이드
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])

        }


    //데이터가 변경되면  새로운 아이템 콜백 요청
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem.createdAt == newItem.createdAt
            }

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}