package com.gyeongsotone.danplay.model

data class MatchDTO(var matchId : Long? = null,
                    var sports : String? = null,
                    var totalNum : Int? = null,
                    var currentNum : Int? = null,
                    var place : String? = null,
                    var content : String? = null,
                    var playTime : String? = null,
                    var applyTime : Long? = null,) {
}