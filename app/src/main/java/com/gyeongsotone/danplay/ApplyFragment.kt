package com.gyeongsotone.danplay

import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
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
    private lateinit var applyButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.fragment_apply, container, false) as ViewGroup

        applyButton = viewGroup!!.findViewById(R.id.button_filterapply)
        applyButton.setOnClickListener{
            Toast.makeText(getActivity(), "DB에 저장 성공~", Toast.LENGTH_LONG).show()
            test()
        }

        return viewGroup
    }


    fun test() {
        database = Firebase.database.reference
        var matchDTO = MatchDTO()


        // uid, userId, name, time db에 저장
        // Insert userId
        matchDTO.matchId = "2"
        matchDTO.sports = "tennis"
        matchDTO.participants = "4"
        matchDTO.place = "단국대 테니스장2"
        matchDTO.content = "테니스 칠 사람 모여요~"
        matchDTO.time = "2021-10-25 10:00:00"
        matchDTO.applytime = System.currentTimeMillis()

        database.child("match").child("123456").setValue(matchDTO)
    }
}