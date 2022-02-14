package com.gyeongsotone.danplay.model

import java.io.Serializable

data class ListViewModel(
    val name: String,
    var title: String,
    val content: String,
    val matchId: String,
) : Serializable {}
