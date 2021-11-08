package com.gyeongsotone.danplay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gyeongsotone.danplay.databinding.ActivityDetailBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationBarView
import com.gyeongsotone.danplay.databinding.FragmentSearchBinding


class DetailActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var mBinding: ActivityDetailBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        mBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var current_user = auth.currentUser!!
        var participationMsg = binding.participationBtnMsg
        var name = binding.textviewName
        var sports = binding.textviewSports
        var number = binding.textviewNumber
        var time = binding.textviewTime
        var place = binding.textviewPlace
        var content = binding.textviewContent
        var button = binding.buttonParticipation
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
            if (number.text.split("/")[0] == number.text.split("/")[1]) {
                Toast.makeText(this, "매치 허용 인원이 가득 찼습니다.", Toast.LENGTH_LONG).show()
            }
            else {
                database.get().addOnSuccessListener {
                    var flag = 0
                    for (idx in 0 until it.child("match").child(matchId).child("registrant").childrenCount.toInt()) {
                        // 참여하려는 매치의 registrant에 자신이 이미 포함되어 있을 때
                        if ((it.child("match").child(matchId).child("registrant").child(idx.toString()).value) == current_user.uid) {
                            participationMsg.text = "이미 참여한 매치입니다."
                            participationMsg.visibility = View.VISIBLE
                            flag = 1
                            break
                        }
                    }
                    if (flag == 0) {
                        var arrayMyMatch = it.child("user").child(current_user.uid).child("matchId")
                        // 참여하려는 매치의 시간과 내 매치 시간이 동일한 것이 있을 때
                        for (userMatchId in arrayMyMatch.children) {
                            for (mId in it.child("match").children) {
                                if (mId.key.toString() == userMatchId.value.toString()) {
                                    if (it.child("match").child(mId.key.toString())
                                            .child("playTime").value.toString() ==
                                        it.child("match").child(matchId)
                                            .child("playTime").value.toString()
                                    ) {
                                        participationMsg.text = "동일한 시간에 예약이 존재합니다. 예약을 확인해주세요!"
                                        participationMsg.visibility = View.VISIBLE
                                        flag = 1
                                        break
                                    }
                                }
                            }
                            if (flag == 1)
                                break
                        }
                    }
                    if (flag == 0) {
                        writeNewUser(current_user, matchId)
                    }
                }
            }
        }

    }

    fun writeNewUser(user: FirebaseUser?, matchId: String) {

        if (user != null) {
            database.get().addOnSuccessListener {
                var tempMatchId = it.child("user").child(user!!.uid).child("matchId").value as ArrayList<String>
                var myMatchNum = it.child("user").child(user!!.uid).child("matchId").childrenCount
                var matchUserNum = it.child("match").child(matchId).child("registrant").childrenCount
                if (tempMatchId[0].equals("-1")) {
                    database.child("user").child(user!!.uid).child("matchId")
                        .child("0").setValue(matchId)
                    database.child("match").child(matchId).child("registrant")
                        .child(matchUserNum.toString()).setValue(user!!.uid)
                }
                else {
                    database.child("user").child(user!!.uid).child("matchId").child(myMatchNum.toString()).setValue(matchId)
                    database.child("match").child(matchId).child("registrant").child(matchUserNum.toString()).setValue(user!!.uid)
                }
                Toast.makeText(this,"매치 참여 성공!", Toast.LENGTH_LONG).show()
                // fragment로 화면전환
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.addOnFailureListener{
                Toast.makeText(this, "DB 읽기 실패", Toast.LENGTH_LONG).show()
            }
        }
    }
}