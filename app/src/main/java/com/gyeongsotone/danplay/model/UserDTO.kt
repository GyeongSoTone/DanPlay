package com.gyeongsotone.danplay.model

data class UserDTO (var uid : String? = null,
               var name : String? = null,
               var birth : String? = null,
               var sex : String? = null,
               var preference : ArrayList<String>? = null,
               var matchId : ArrayList<String>? = null,) {

}

