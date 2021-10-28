package com.gyeongsotone.danplay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.gyeongsotone.danplay.databinding.ActivityLoginBinding
import com.gyeongsotone.danplay.databinding.ActivitySignupBinding


class SignupActivity : AppCompatActivity() {

    private var mBinding: ActivitySignupBinding? = null
    private val binding get() = mBinding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySignupBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        binding.buttonSignUp.setOnClickListener{
            signup()
        }
    }

    fun signup() {
        var userEmail = binding.emailEdittext.text.toString()
        var userPwd = binding.passwordEdittext.text.toString()
        var pwdCheck = binding.passwordCheckEdittext.text.toString()
        var userName = binding.textUsername.text.toString()
        var userBirth = binding.birthEdittext.text.toString()


        // 입력 칸이 비어있다면 리턴
        if (TextUtils.isEmpty(userEmail) or TextUtils.isEmpty(userPwd) or TextUtils.isEmpty(userName) or TextUtils.isEmpty(userBirth)) {
            Toast.makeText(this, "정보를 바르게 입력해주세요", Toast.LENGTH_LONG).show()
            return
        }
        if (!TextUtils.equals(userPwd, pwdCheck)) {
            Toast.makeText(this, "비밀번호와 비밀번호 재확인이 일치하지 않습니다.", Toast.LENGTH_LONG).show()
            return
        }

        // DKU 계정인지 확인
        var emailArr = userEmail.split("@")
        if ((emailArr[0].length == 8) and (emailArr[1].equals("dankook.ac.kr"))) {

            // 회원가입한 결과값을 받아오기 위해서 addOnCompleteListener {  }
            auth?.createUserWithEmailAndPassword(userEmail, userPwd)?.addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    moveLoginPage(task.result?.user)
                } else {
                    //if you have account move to login page
                    if (task.exception?.message.equals("The email address is already in use by another account.")) {
                        Toast.makeText(this, "이미 존재하는 이메일입니다.", Toast.LENGTH_LONG).show()
                    }
                    //Show the error message
                    else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        else {
            Toast.makeText(this, "알맞은 형식의 이메일이 아닙니다.", Toast.LENGTH_LONG).show()
            return
        }
    }

    fun moveLoginPage(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, "회원가입에 성공했습니다.", Toast.LENGTH_LONG).show()
            // 다음 페이지로 넘어가는 Intent
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }
}