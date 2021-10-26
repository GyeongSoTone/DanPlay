package com.gyeongsotone.danplay.model

data class MatchDTO(var matchId : String? = null,
                    var sports : String? = null,
                    var participants : String? = null,
                    var place : String? = null,
                    var content : String? = null,
                    var time : Long? = null,
                    var applytime : Long? = null,) {
}
