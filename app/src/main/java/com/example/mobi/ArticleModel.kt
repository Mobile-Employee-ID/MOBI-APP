package com.example.mobi

data class ArticleModel(
    val noticeId: String,
    val title: String,
    val createdAt: Long,
    val contents: String,
    val imageUrl: String,
){

    constructor(): this("", "", 0, "", "")

}

