package com.gyeongsotone.danplay

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class MyMatchDetailActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    var fragment1: MyinfoFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_match_detail)

        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()

        var current_user = auth.currentUser!!
        //var registrant = database.child("registrant").child("0").get()
        var name = findViewById<View>(R.id.textview_name) as TextView
        var sports = findViewById<View>(R.id.textview_sports) as TextView
        var number = findViewById<View>(R.id.textview_number) as TextView
        var time = findViewById<View>(R.id.textview_time) as TextView
        var place = findViewById<View>(R.id.textview_place) as TextView
        var content = findViewById<View>(R.id.textview_content) as TextView
        var btn_cancel = findViewById<Button>(R.id.button_cancel) as Button
        val match = intent.getSerializableExtra("matchInfo") as ListViewModel
        val matchId = intent.getSerializableExtra("matchId") as String
        name.text = match.name
        var listStrTitle = match.title.split("|")
        sports.text = listStrTitle[0].trim()
        number.text = listStrTitle[4].trim()
        time.text = listStrTitle[1].trim() + " " + listStrTitle[2].trim()
        place.text = listStrTitle[3].trim()
        content.text = match.content

        btn_cancel.setOnClickListener {
            val alert_cancel = AlertDialog.Builder(this)
            alert_cancel.setMessage("정말로 매치를 취소하시겠습니까?")
            // 확인버튼
            alert_cancel.setPositiveButton("확인") { dialog, which ->
                database.get().addOnSuccessListener {
                    deleteMatch(it, matchId, current_user)
                }.addOnFailureListener{
                    Toast.makeText(this, "DB 읽기 실패", Toast.LENGTH_LONG).show()
                }
            }
            // 취소버튼
            alert_cancel.setNegativeButton("취소", null)
            val alert = alert_cancel.create()
            alert.setIcon(R.drawable.logo_danplay)
            alert.setTitle("매치취소")
            alert.show()
        }
    }

    fun deleteMatch(it: DataSnapshot, matchId: String, current_user: FirebaseUser){
        //database = Firebase.database.reference
        var i = 0
        for(snapshot in it.child("match").child(matchId).child("registrant").children){
            Toast.makeText(this, "for문 들어옴", Toast.LENGTH_LONG).show()
            if(snapshot.value.toString().equals(current_user.uid)){
                Toast.makeText(this, "첫번째 if문 들옴", Toast.LENGTH_LONG).show()
                if(i == 0){
                    Toast.makeText(this, "i=0 break", Toast.LENGTH_LONG).show()
                    break
                }
                database.child("match").child(matchId).child("registrant").child(snapshot.key.toString()).removeValue()
            }
            i = 1
        }
        if(i == 0){
            Toast.makeText(this, "방장은 매치를 취소할 수 없습니다.", Toast.LENGTH_LONG).show()
        }
        else{
            for(snapshot in it.child("user").child(current_user.uid).child("matchId").children) {
                if(snapshot.value.toString().equals(matchId)){
                    database.child("user").child(current_user.uid).child("matchId").child(snapshot.key.toString()).removeValue()
                }
            }
            Toast.makeText(this, "매치취소 완료", Toast.LENGTH_LONG).show()
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragment1 = MyinfoFragment() // 객체 생성
            transaction.replace(R.id.my_match_detail_layout, fragment1!!) //layout, 교체될 layout
            transaction.commit() //commit으로 저장 하지 않으면 화면 전환이 되지 않음
        }
    }
}