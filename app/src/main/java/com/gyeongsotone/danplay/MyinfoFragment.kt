package com.gyeongsotone.danplay

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
import com.google.firebase.database.DatabaseReference
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

        btn_signout.setOnClickListener {
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
        btn_logout.setOnClickListener {
            Toast.makeText(getActivity(), "로그아웃 완료.", Toast.LENGTH_LONG).show()
            val intent = Intent(requireActivity().applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

        return viewGroup
    }
}