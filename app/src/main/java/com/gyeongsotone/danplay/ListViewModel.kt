package com.gyeongsotone.danplay

import java.io.Serializable

data class ListViewModel (
    val name : String,
    var title : String,
    val content : String,
    val matchId : String,
):Serializable {

}