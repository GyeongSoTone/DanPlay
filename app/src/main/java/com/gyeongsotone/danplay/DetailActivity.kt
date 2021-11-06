package com.gyeongsotone.danplay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gyeongsotone.danplay.databinding.ActivityDetailBinding
import com.gyeongsotone.danplay.databinding.ActivitySignupBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        var current_user = auth.currentUser!!

        var name = findViewById<View>(R.id.textview_name) as TextView
        var sports = findViewById<View>(R.id.textview_sports) as TextView
        var number = findViewById<View>(R.id.textview_number) as TextView
        var time = findViewById<View>(R.id.textview_time) as TextView
        var place = findViewById<View>(R.id.textview_place) as TextView
        var content = findViewById<View>(R.id.textview_content) as TextView
        var button = findViewById<Button>(R.id.button_participation)
        val match = intent.getSerializableExtra("matchInfo") as ListViewModel
        val matchId = intent.getSerializableExtra("matchId") as String
        name.text = match.name
        var listStrTitle = match.title.split("|")
        sports.text = listStrTitle[0].trim()
        number.text = listStrTitle[4].trim()
        time.text = listStrTitle[1].trim() + " " + listStrTitle[2].trim()
        place.text = listStrTitle[3].trim()
        content.text = match.content

        button.setOnClickListener {
            /*
             참여하기 버튼 클릭 시
             if (현재 num == 토탈 num) => 토스트메시지: 가득찼다.
             else =>
                if (유저의 matchId 중 신청하려는 match의 시간 and 장소가 동일하다면) => 토스트메시지: 중복이다.
                else =>
                    1. 현재 유저의 matchId에 저장 (추가)
                    2. 해당 match의 current멤버에 저장 (추가)

             이전 페이지로 이동 (인원수 업데이트 되어야 함)
            */
            writeNewUser(current_user, matchId)

        }

    }

    fun writeNewUser(user: FirebaseUser?, matchId: String) {

        if (user != null) {
            database.get().addOnSuccessListener {
                var tempMatchId = it.child("user").child(user!!.uid).child("matchId").value as ArrayList<String>
                var myMatchNum = it.child("user").child(user!!.uid).child("matchId").childrenCount
                var matchUserNum = it.child("match").child(matchId).child("registrant").childrenCount
                if (tempMatchId[0].equals("-1"))
                    Toast.makeText(this, "잘못된 매치입니다.", Toast.LENGTH_LONG).show()
                else {
                    database.child("user").child(user!!.uid).child("matchId").child(myMatchNum.toString()).setValue(matchId)
                    database.child("match").child(matchId).child("registrant").child(matchUserNum.toString()).setValue(user!!.uid)
                }
                // fragment로 화면전환
                val intent = Intent(SearchFragment().context, MainActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener{
                Toast.makeText(this, "DB 읽기 실패", Toast.LENGTH_LONG).show()
            }
        }
    }
}