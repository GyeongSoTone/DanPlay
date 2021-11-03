package com.gyeongsotone.danplay.model

data class MatchDTO(var sports : String? = null,
                    var totalNum : Int? = null,
                    var registrant : ArrayList<String>? = null,
                    var place : String? = null,
                    var content : String? = null,
                    var playTime : String? = null,
                    var applyTime : String? = null,) {
}