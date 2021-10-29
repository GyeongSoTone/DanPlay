package com.gyeongsotone.danplay

import android.content.Intent
import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainpageFragment : Fragment() {
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
        var current_user = auth.currentUser!!
        var name = viewGroup!!.findViewById<View>(R.id.text_name) as TextView

        database.child("user").child(current_user.uid).get().addOnSuccessListener {
            //Toast.makeText(this, "${it.child("name").value}님 환영합니다!", Toast.LENGTH_LONG).show()
            name.setText("${it.child("name").value}")
        }.addOnFailureListener{
            //Toast.makeText(this, "실패~", Toast.LENGTH_LONG).show()
        }

        var btn_sports1 = viewGroup!!.findViewById<View>(R.id.button_sports1) as ImageButton
        btn_sports1.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext, SignupActivity::class.java)
            startActivity(intent)
        }

        var btn_sports2 = viewGroup!!.findViewById<View>(R.id.button_sports2) as ImageButton
        btn_sports2.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

        return viewGroup
    }
}