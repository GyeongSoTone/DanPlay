package com.gyeongsotone.danplay

import android.app.AlertDialog
import android.content.Intent
import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyinfoFragment : Fragment() {
    var viewGroup: ViewGroup? = null
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.fragment_myinfo, container, false) as ViewGroup
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        var current_user = auth.currentUser!!
        var btn_signout = viewGroup!!.findViewById<View>(R.id.signout) as Button
        var btn_logout = viewGroup!!.findViewById<View>(R.id.logout) as Button
        var btn_contact = viewGroup!!.findViewById<View>(R.id.contact) as Button
        //var btn_profile_change = viewGroup!!.findViewById<View>(R.id.profile_change) as Button

        // 회원탈퇴
        btn_signout.setOnClickListener {
            val alert_signout = AlertDialog.Builder(getActivity())
            alert_signout.setMessage("정말로 회원탈퇴 하시겠습니까?")
            // 확인버튼
            alert_signout.setPositiveButton("확인") { dialog, which ->
                database.child("user").child(current_user.uid).removeValue()
                    .addOnSuccessListener {
                        // Write was successful!
                        // ..
                    }
                    .addOnFailureListener {
                        // Write failed
                        // ..
                    }
                current_user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //Log.d(TAG, "User account deleted.")
                            Toast.makeText(getActivity(), "회원탈퇴 완료.", Toast.LENGTH_LONG).show()
                            val intent = Intent(requireActivity().applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
            }
            // 취소버튼
            alert_signout.setNegativeButton("취소", null)
            val alert = alert_signout.create()
            alert.setIcon(R.drawable.logo_danplay)
            alert.setTitle("회원탈퇴")
            alert.show()
        }
        // 로그아웃
        btn_logout.setOnClickListener {
            val alert_logout = AlertDialog.Builder(getActivity())
            alert_logout.setMessage("정말로 로그아웃 하시겠습니까?")
            // 확인버튼
            alert_logout.setPositiveButton("확인") { dialog, which ->
                Toast.makeText(getActivity(), "로그아웃 완료.", Toast.LENGTH_LONG).show()
                val intent = Intent(requireActivity().applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
            // 취소버튼
            alert_logout.setNegativeButton("취소", null)
            val alert = alert_logout.create()
            alert.setIcon(R.drawable.logo_danplay)
            alert.setTitle("로그아웃")
            alert.show()
        }
        // 문의하기
        btn_contact.setOnClickListener {
            val alert_contact = AlertDialog.Builder(getActivity())
            alert_contact.setMessage("yunx2@naver.com으로 문의해주세요.")
            alert_contact.setPositiveButton("확인", null)
            val alert = alert_contact.create()
            alert.setIcon(R.drawable.logo_danplay)
            alert.setTitle("문의하기")
            alert.show()
        }
        return viewGroup
    }
}