package com.example.mobi

data class GuestSignModel(
    val signId: String,
    val title: String,
    val createdAt: Long,
    val contents: String,
    val deviceID : String,

) {

    constructor() : this("", "",0, "","")

}
