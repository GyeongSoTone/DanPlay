package com.gyeongsotone.danplay.ui.main

import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gyeongsotone.danplay.R

class MainPageFragment : Fragment() {
    var viewGroup: ViewGroup? = null
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.fragment_mainpage, container, false) as ViewGroup
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!
        val myMatchList = ArrayList<String>()
        var temp: String
        val name = viewGroup!!.findViewById<View>(R.id.text_name) as TextView
        val myMatch = viewGroup!!.findViewById<View>(R.id.text_my_match) as TextView
        val textSports1 = viewGroup!!.findViewById<View>(R.id.text_sports1) as TextView
        val textSports2 = viewGroup!!.findViewById<View>(R.id.text_sports2) as TextView
        val textSports3 = viewGroup!!.findViewById<View>(R.id.text_sports3) as TextView
        val textSports4 = viewGroup!!.findViewById<View>(R.id.text_sports4) as TextView
        val btnSports1 = viewGroup!!.findViewById<View>(R.id.btn_sports1) as ImageButton
        val btnSports2 = viewGroup!!.findViewById<View>(R.id.btn_sports2) as ImageButton
        val btnSports3 = viewGroup!!.findViewById<View>(R.id.btn_sports3) as ImageButton
        val btnSports4 = viewGroup!!.findViewById<View>(R.id.btn_sports4) as ImageButton

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var i = 0
                for (snapshot in dataSnapshot.child("user").child(currentUser.uid).children) {
                    if (snapshot.key.equals("name")) {
                        name.text = "${snapshot.value}"
                    } else if (snapshot.key.equals("preference")) {
                        textSports1.text = snapshot.child("0").value.toString()
                        textSports2.text = snapshot.child("1").value.toString()
                        textSports3.text = snapshot.child("2").value.toString()
                        textSports4.text = snapshot.child("3").value.toString()
                        testNull(textSports1)
                        testNull(textSports2)
                        testNull(textSports3)
                        testNull(textSports4)
                        setImage(textSports1, btnSports1)
                        setImage(textSports2, btnSports2)
                        setImage(textSports3, btnSports3)
                        setImage(textSports4, btnSports4)
                    } else if (snapshot.key.equals("matchId")) {
                        i = 1
                        for (snapshot1 in dataSnapshot.child("user").child(currentUser.uid)
                            .child("matchId").children) {
                            val match = snapshot1.value.toString()
                            for (snapshot2 in dataSnapshot.child("match").children) {
                                if (snapshot2.key.equals(match)) {
                                    val matchTime = snapshot2.child("playTime").value.toString()
                                    val matchSports = snapshot2.child("sports").value.toString()
                                    temp = matchTime.slice(IntRange(5, 15))
                                    temp = "$temp $matchSports"
                                    myMatchList.add(temp)
                                }
                            }
                        }
                        myMatchList.sort()
                        if (myMatchList.count() == 0) {
                            myMatch.text = "예약된 매치 없음"
                        } else if (myMatchList.count() == 1) {
                            temp = myMatchList[0]
                            myMatch.text = temp
                        } else {
                            temp = myMatchList[0] + "\n" + myMatchList[1]
                            myMatch.text = temp
                        }
                    }
                }
                if (i == 0) {
                    myMatch.text = "예약된 매치 없음"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, "DB 읽기 실패", Toast.LENGTH_LONG).show()
            }
        })

        return viewGroup
    }

    fun testNull(textSports: TextView) {
        if (textSports.text.equals("null")) {
            textSports.text = ""
        }
    }

    fun setImage(textSports: TextView, btnSports: ImageButton) {
        when (textSports.text) {
            "테니스" -> btnSports.setImageResource(R.drawable.tennis)
            "축구" -> btnSports.setImageResource(R.drawable.soccer)
            "농구" -> btnSports.setImageResource(R.drawable.basket)
            "족구" -> btnSports.setImageResource(R.drawable.jokgoo)
            "풋살" -> btnSports.setImageResource(R.drawable.futsal)
            else -> null
        }
    }
}
