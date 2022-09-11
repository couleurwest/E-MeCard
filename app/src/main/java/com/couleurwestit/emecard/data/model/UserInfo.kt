package com.couleurwestit.emecard.data.model

data class UserInfo(
    val firstname: String,
    val lastname: String,
    val corp: String,
    val email: String,
    val website: String,
    val phone_1: String,
    val phone_2: String,
    val userId: String = "vcard",
    val is_set: Boolean = false
)