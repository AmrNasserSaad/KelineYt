package com.training.kelineyt.activity.data

data class User(

    val fristName: String,
    val lastName: String,
    val email: String,
    var imagePath: String = " "

) {
    constructor() : this("", "", "", "")

}
