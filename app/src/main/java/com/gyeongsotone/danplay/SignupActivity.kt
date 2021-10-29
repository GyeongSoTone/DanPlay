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
import android.widget.ToggleButton
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

    private var prefer_button : ArrayList<ToggleButton> = ArrayList()
    // preference 상태 저장하는 배열 (테니스, 축구, 야구, 족구 풋살 순)
    private var prefer_button_state = Array<Int>(5,{0})


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySignupBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        setContentView(binding.root)


        prefer_button[0] = binding.buttonTennis
        prefer_button[1] = binding.buttonSoccer
        prefer_button[2] = binding.buttonBasketball
        prefer_button[3] = binding.buttonJokgoo
        prefer_button[4] = binding.buttonFutsal

        /*for (i in 0 until 4){
            prefer_button[i].setOnClickListener{
                if(prefer_button[i].isChecked == true) {
                    prefer_button_state[i] = 1
                    prefer_button[i].setBackground(ContextCompat.getDrawable(this, R.drawable.btn_event_stroke));
                } else {
                    prefer_button_state[i] = 0
                    prefer_button[i].setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
            }

        }*/

        prefer_button[0].setOnClickListener {
            if(prefer_button[0].isChecked == true) {
                prefer_button[0].setBackground(ContextCompat.getDrawable(this, R.drawable.btn_event_stroke));
                prefer_button_state[0] = 1

            }
            else
            {
                prefer_button[0].setBackgroundColor(Color.parseColor("#FFFFFF"))
                prefer_button_state[0] = 0
            }
        }

        prefer_button[1].setOnClickListener {
            if(prefer_button[1].isChecked == true) {
                prefer_button[1].setBackground(ContextCompat.getDrawable(this, R.drawable.btn_event_stroke));
                prefer_button_state[1] = 1

            }
            else
            {
                prefer_button[1].setBackgroundColor(Color.parseColor("#FFFFFF"))
                prefer_button_state[1] = 0
            }
        }

        prefer_button[2].setOnClickListener {
            if(prefer_button[2].isChecked == true) {
                prefer_button[2].setBackground(ContextCompat.getDrawable(this, R.drawable.btn_event_stroke));
                prefer_button_state[2] = 1
            }
            else
            {
                prefer_button[2].setBackgroundColor(Color.parseColor("#FFFFFF"))
                prefer_button_state[2] = 0
            }
        }

        prefer_button[3].setOnClickListener {
            if(prefer_button[3].isChecked == true) {
                prefer_button[3].setBackground(ContextCompat.getDrawable(this, R.drawable.btn_event_stroke));
                prefer_button_state[3] = 1
            }
            else
            {
                prefer_button[3].setBackgroundColor(Color.parseColor("#FFFFFF"))
                prefer_button_state[3] = 0
            }
        }

        prefer_button[4].setOnClickListener {
            if(prefer_button[4].isChecked == true) {
                prefer_button[4].setBackground(ContextCompat.getDrawable(this, R.drawable.btn_event_stroke));
                prefer_button_state[4] = 1
            }
            else
            {
                prefer_button[4].setBackgroundColor(Color.parseColor("#FFFFFF"))
                prefer_button_state[4] = 0
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