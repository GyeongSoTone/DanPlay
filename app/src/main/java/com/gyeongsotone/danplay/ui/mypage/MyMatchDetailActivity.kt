package com.gyeongsotone.danplay.ui.mypage

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.gyeongsotone.danplay.MainActivity
import com.gyeongsotone.danplay.R
import com.gyeongsotone.danplay.databinding.ActivityMyMatchDetailBinding
import com.gyeongsotone.danplay.model.ListViewModel

class MyMatchDetailActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var mBinding: ActivityMyMatchDetailBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMyMatchDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser!!
        val name = binding.textviewName
        val sports = binding.textviewSports
        val number = binding.textviewNumber
        val time = binding.textviewTime
        val place = binding.textviewPlace
        val content = binding.textviewContent
        val btnCancel = binding.buttonCancel
        val match = intent.getSerializableExtra("matchInfo") as ListViewModel
        val matchId = intent.getSerializableExtra("matchId") as String
        name.text = match.name
        val listStrTitle = match.title.split("|")
        sports.text = listStrTitle[0].trim()
        number.text = listStrTitle[4].trim()
        time.text = listStrTitle[1].trim() + " " + listStrTitle[2].trim()
        place.text = listStrTitle[3].trim()
        content.text = match.content

        btnCancel.setOnClickListener {
            val alertCancel = AlertDialog.Builder(this)
            alertCancel.setMessage("정말로 매치를 취소하시겠습니까?")
            // 확인버튼
            alertCancel.setPositiveButton("확인") { _, _ ->
                database.get().addOnSuccessListener {
                    deleteMatch(it, matchId, currentUser)
                }.addOnFailureListener {
                    Toast.makeText(this, "DB 읽기 실패", Toast.LENGTH_LONG).show()
                }
            }
            // 취소버튼
            alertCancel.setNegativeButton("취소", null)
            val alert = alertCancel.create()
            alert.setIcon(R.drawable.logo_danplay)
            alert.setTitle("매치 취소")
            alert.show()
        }
    }

    private fun deleteMatch(it: DataSnapshot, matchId: String, current_user: FirebaseUser) {
        val cancelMsg = binding.cancelBtnMsg
        var i = 0
        for (snapshot in it.child("match").child(matchId).child("registrant").children) {
            if (snapshot.value.toString() == current_user.uid) {
                if (i == 0) {
                    break
                }
                database.child("match").child(matchId).child("registrant")
                    .child(snapshot.key.toString()).removeValue()
            }
            i = 1
        }
        if (i == 0) {
            cancelMsg.text = "방장은 매치를 취소할 수 없습니다."
            cancelMsg.visibility = View.VISIBLE
        } else {
            for (snapshot in it.child("user").child(current_user.uid).child("matchId").children) {
                if (snapshot.value.toString() == matchId) {
                    if (it.child("user").child(current_user.uid)
                            .child("matchId").childrenCount.toString() == "1"
                    ) {
                        database.child("user").child(current_user.uid).child("matchId")
                            .child(snapshot.key.toString()).setValue("-1")
                    } else
                        database.child("user").child(current_user.uid).child("matchId")
                            .child(snapshot.key.toString()).removeValue()
                }
            }
            Toast.makeText(this, "매치취소 완료", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
