package com.gyeongsotone.danplay

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gyeongsotone.danplay.databinding.ActivityLoginBinding
import com.gyeongsotone.danplay.databinding.ActivitySignupBinding
import com.gyeongsotone.danplay.model.UserDTO


class SignupActivity : AppCompatActivity() {

    private var mBinding: ActivitySignupBinding? = null
    private val binding get() = mBinding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private var prefer_tennis = 0
    private var prefer_soccer = 0
    private var prefer_basketball = 0
    private var prefer_jokgoo = 0
    private var prefer_futsal = 0






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySignupBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        setContentView(binding.root)

        binding.buttonTennis.setOnClickListener {
            if(binding.buttonTennis.isChecked == true) {
                binding.buttonTennis.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_event_stroke));
                prefer_tennis = 1

            }
            else
            {
                binding.buttonTennis.setBackgroundColor(Color.parseColor("#FFFFFF"))
                prefer_tennis = 0
            }
        }

        binding.buttonSoccer.setOnClickListener {
            if(binding.buttonSoccer.isChecked == true) {
                binding.buttonSoccer.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_event_stroke));
                prefer_soccer = 1

            }
            else
            {
                binding.buttonSoccer.setBackgroundColor(Color.parseColor("#FFFFFF"))
                prefer_soccer = 0
            }
        }

        binding.buttonBasketball.setOnClickListener {
            if(binding.buttonBasketball.isChecked == true) {
                binding.buttonBasketball.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_event_stroke));
                prefer_basketball = 1
            }
            else
            {
                binding.buttonBasketball.setBackgroundColor(Color.parseColor("#FFFFFF"))
                prefer_basketball = 0
            }
        }

        binding.buttonJokgoo.setOnClickListener {
            if(binding.buttonJokgoo.isChecked == true) {
                binding.buttonJokgoo.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_event_stroke));
                prefer_jokgoo = 1
            }
            else
            {
                binding.buttonJokgoo.setBackgroundColor(Color.parseColor("#FFFFFF"))
                prefer_jokgoo = 0
            }
        }

        binding.buttonFutsal.setOnClickListener {
            if(binding.buttonFutsal.isChecked == true) {
                binding.buttonFutsal.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_event_stroke));
                prefer_futsal = 1
            }
            else
            {
                binding.buttonFutsal.setBackgroundColor(Color.parseColor("#FFFFFF"))
                prefer_futsal = 0
            }
        }

        binding.buttonSignUp.setOnClickListener{
            signup()
        }



    }

    fun signup() {
        var userEmail = binding.emailEdittext.text.toString()
        var userPwd = binding.passwordEdittext.text.toString()
        var pwdCheck = binding.passwordCheckEdittext.text.toString()
        var userName = binding.usernameEdittext.text.toString()
        var userBirth = binding.birthEdittext.text.toString()
        var userInfo = arrayOf(userName, userBirth)


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
                    // db에 저장
                    setUserData(task.result?.user, userInfo)
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

    fun setUserData(user: FirebaseUser?, userInfo: Array<String>) {
        var UserDTO = UserDTO()

        // uid, userId, name, time db에 저장
        if (user != null) {
            // Insert name
            UserDTO.name = userInfo[0]

            // Insert birth
            UserDTO.birth = userInfo[1]

            // Insert timeStamp
            //UserDTO.timestamp = System.currentTimeMillis()

            database.child("user").child(user.uid).setValue(UserDTO)

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