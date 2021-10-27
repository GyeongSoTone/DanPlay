package com.gyeongsotone.danplay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gyeongsotone.danplay.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginActivity : AppCompatActivity() {

    private var mBinding: ActivityLoginBinding? = null
    private val binding get() = mBinding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        /* 편의를 위해 로고 클릭 시 메인 이동 활성화 */
        binding.logoDanplay.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.signUp.setOnClickListener{
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

        binding.login.setOnClickListener{
            signinEmail()
        }
    }

    fun signinEmail() {
        var loginEmail = binding.loginIdTextedit.text.toString()
        var loginPwd = binding.loginPasswordTextedit.text.toString()
        if (loginEmail.equals("") or loginPwd.equals("")) {
            Toast.makeText(this, "정보를 바르게 입력해주세요", Toast.LENGTH_LONG).show()
            return
        }
        auth?.signInWithEmailAndPassword(loginEmail, loginPwd)
            ?.addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_LONG).show()
                    moveMainPage(task.result?.user)
                } else {
                    //Show the error message
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            // 다음 페이지로 넘어가는 Intent
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
