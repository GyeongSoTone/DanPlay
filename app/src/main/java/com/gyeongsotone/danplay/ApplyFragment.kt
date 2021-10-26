package com.gyeongsotone.danplay

import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gyeongsotone.danplay.databinding.ActivitySignupBinding
import com.gyeongsotone.danplay.databinding.FragmentApplyBinding
import com.gyeongsotone.danplay.model.MatchDTO


class ApplyFragment : Fragment() {
    var viewGroup: ViewGroup? = null
    private lateinit var database: DatabaseReference


    override fun onCreateView(


        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentApplyBinding.inflate(inflater, container, false)
        binding.buttonEvent.setOnClickListener{
            Toast.makeText(getActivity(), "This email is not a valid DKU email", Toast.LENGTH_LONG).show()
        }
        viewGroup = inflater.inflate(R.layout.fragment_apply, container, false) as ViewGroup
        return viewGroup
    }

    fun test() {
        database = Firebase.database.reference
        var matchDTO = MatchDTO()


        // uid, userId, name, time db에 저장
        // Insert userId
        matchDTO.matchId = "1234"
        matchDTO.sports = null
        matchDTO.participants = null
        matchDTO.place = null
        matchDTO.content = null
        matchDTO.time = null
        matchDTO.applytime = System.currentTimeMillis()

            database.child("match").child("12345").setValue(matchDTO)
    }
}