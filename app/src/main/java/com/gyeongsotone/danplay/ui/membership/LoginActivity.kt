package com.gyeongsotone.danplay.ui.membership

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.gyeongsotone.danplay.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gyeongsotone.danplay.MainActivity

class LoginActivity : AppCompatActivity() {

    private var backBtnDoubleClick = false
    private lateinit var database: DatabaseReference
    private var mBinding: ActivityLoginBinding? = null
    private val binding get() = mBinding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // init
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        setContentView(binding.root)

        binding.signUp.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

        binding.login.setOnClickListener {
            signInEmail()
        }
    }

    private fun signInEmail() {
        val loginEmail = binding.loginIdTextedit.text.toString()
        val loginPwd = binding.loginPasswordTextedit.text.toString()
        if ((loginEmail == "") or (loginPwd == "")) {
            binding.loginFail.visibility = View.VISIBLE
            binding.loginFail.text = "아이디와 비밀번호를 입력해주세요"
            return
        }
        auth.signInWithEmailAndPassword(loginEmail, loginPwd)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    database.child("user").child(task.result?.user!!.uid).get()
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "${it.child("name").value}님 환영합니다!",
                                Toast.LENGTH_LONG
                            ).show()
                        }.addOnFailureListener {
                            binding.loginFail.visibility = View.VISIBLE
                        }
                    moveMainPage(task.result?.user)
                } else {
                    //Show the error message
                    binding.loginFail.text = "아이디와 비밀번호를 확인해주세요"
                    binding.loginFail.visibility = View.VISIBLE
                }
            }
    }

    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            // 다음 페이지로 넘어가는 Intent
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        if (backBtnDoubleClick)
            finish()
        backBtnDoubleClick = true
        Toast.makeText(this, "종료하시려면 더블 클릭 하세요.", Toast.LENGTH_LONG).show()
        Handler().postDelayed(Runnable {
            backBtnDoubleClick = false
        }, 2000)
    }
}
