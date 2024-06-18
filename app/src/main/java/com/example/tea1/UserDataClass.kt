package com.example.tea1

class User {
    var id:String=""
    var pseudo:String=""
}

data class UserDataClass(
    val version: Double = 1.2,
    var success: Boolean = false,
    var status: Int = 0,
    var apiname: String = "todo",
    var lists: List<User> = emptyList()
)