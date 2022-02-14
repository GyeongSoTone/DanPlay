package com.gyeongsotone.danplay.ui.matchsearch

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gyeongsotone.danplay.MainActivity
import com.gyeongsotone.danplay.R
import com.gyeongsotone.danplay.databinding.ActivityDetailBinding
import com.gyeongsotone.danplay.model.ListViewModel

class ParticipationActivity : AppCompatActivity() {
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

        val participationMsg = binding.participationBtnMsg
        val name = binding.textviewName
        val sports = binding.textviewSports
        val number = binding.textviewNumber
        val time = binding.textviewTime
        val place = binding.textviewPlace
        val content = binding.textviewContent
        val participationButton = binding.buttonParticipation
        val match = intent.getSerializableExtra("matchInfo") as ListViewModel
        name.text = match.name
        val listStrTitle = match.title.split("|")
        sports.text = listStrTitle[0].trim()
        number.text = listStrTitle[4].trim()
        time.text = listStrTitle[1].trim() + " " + listStrTitle[2].trim()
        place.text = listStrTitle[3].trim()
        content.text = match.content

        participationButton.setOnClickListener {
            val alertCancel = AlertDialog.Builder(this)
            alertCancel.setMessage("정말로 매치에 참가하시겠습니까?")
            // 확인버튼
            alertCancel.setPositiveButton("확인") { dialog, which ->
                database.get().addOnSuccessListener {
                    if (number.text.split("/")[0] == number.text.split("/")[1]) {
                        participationMsg.text = "매치 허용 인원이 가득 찼습니다."
                        participationMsg.visibility = View.VISIBLE
                    } else {
                        database.get().addOnSuccessListener {
                            participateMatch(it, participationMsg)
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "DB 읽기 실패", Toast.LENGTH_LONG).show()
                }
            }
            // 취소버튼
            alertCancel.setNegativeButton("취소", null)
            val alert = alertCancel.create()
            alert.setIcon(R.drawable.logo_danplay)
            alert.setTitle("매치 참가")
            alert.show()
        }
    }

    private fun participateMatch(it: DataSnapshot, participationMsg: TextView) {
        val matchId = intent.getSerializableExtra("matchId") as String
        val currentUser = auth.currentUser!!
        var flag = 0
        for (mId in it.child("match").child(matchId).child("registrant").children) {
            if (mId.value.toString() == currentUser.uid) {
                participationMsg.text = "이미 참여한 매치입니다."
                participationMsg.visibility = View.VISIBLE
                flag = 1
                break
            }
        }
        if (flag == 0) {
            val arrayMyMatch = it.child("user").child(currentUser.uid).child("matchId")
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
            writeNewUser(currentUser, matchId)
        }
    }

    private fun writeNewUser(user: FirebaseUser?, matchId: String) {

        if (user != null) {
            database.get().addOnSuccessListener {
                val tempMatchId =
                    it.child("user").child(user!!.uid).child("matchId").value as ArrayList<String>
                val myMatchNum = it.child("user").child(user!!.uid).child("matchId").childrenCount
                val matchUserNum =
                    it.child("match").child(matchId).child("registrant").childrenCount
                if (tempMatchId[0] == "-1") {
                    database.child("user").child(user!!.uid).child("matchId")
                        .child("0").setValue(matchId)
                    database.child("match").child(matchId).child("registrant")
                        .child(matchUserNum.toString()).setValue(user!!.uid)
                } else {
                    database.child("user").child(user!!.uid).child("matchId")
                        .child(myMatchNum.toString()).setValue(matchId)
                    database.child("match").child(matchId).child("registrant")
                        .child(matchUserNum.toString()).setValue(user!!.uid)
                }
                Toast.makeText(this, "매치 참여 성공!", Toast.LENGTH_LONG).show()
                // fragment로 화면전환
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "DB 읽기 실패", Toast.LENGTH_LONG).show()
            }
        }
    }
}
