package com.example.mobi

data class Friend(
    val email: String? = null,
    val name: String? = null,
    val profileImageUrl: String? = null,
    val uid: String? = null,
    val deviceId: String? = null,
    var gategrant: String? = null,
) {

    constructor() : this("", "", "", "","","")

}