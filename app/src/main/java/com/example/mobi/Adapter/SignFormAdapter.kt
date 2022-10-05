package com.example.mobi.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobi.ArticleModel
import com.example.mobi.GuestSignModel
import com.example.mobi.R
import com.example.mobi.databinding.ItemArticleBinding
import com.example.mobi.databinding.ItemGuestformBinding
import java.text.SimpleDateFormat
import java.util.*

class SignFormAdapter : ListAdapter<GuestSignModel, SignFormAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemGuestformBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(guestsignModel: GuestSignModel) {

            val format = SimpleDateFormat("MM월 dd일 ss초") //날짜를 가지고 오기 위한 포맷
            val date = Date(guestsignModel.createdAt) //날짜로 데이터타입 변경

            binding.titleText.text = guestsignModel.title
            binding.dateText.text = format.format(date).toString()
            binding.contentsText.text = guestsignModel.contents


        }

    }

    //아티클 어댑터 뷰홀더 생성함수 오버라이드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemGuestformBinding.inflate(
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


        val diffUtil = object : DiffUtil.ItemCallback<GuestSignModel>() {
            override fun areItemsTheSame(oldItem: GuestSignModel, newItem: GuestSignModel): Boolean {
                return oldItem.createdAt == newItem.createdAt
            }

            override fun areContentsTheSame(oldItem: GuestSignModel, newItem: GuestSignModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}