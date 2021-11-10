package com.gyeongsotone.danplay


import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gyeongsotone.danplay.databinding.ActivitySignupBinding
import com.gyeongsotone.danplay.model.UserDTO
import android.view.View
import android.widget.RadioGroup


class SignupActivity : AppCompatActivity(), View.OnClickListener {

    private var mBinding: ActivitySignupBinding? = null
    private val binding get() = mBinding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var userSex : String = ""

    private var preferButton : ArrayList<ToggleButton> = ArrayList()
    // preference 상태 저장하는 배열 (테니스, 축구, 야구, 족구 풋살 순)
    private var sportsList : ArrayList<String> = ArrayList()
    private var userPreference : ArrayList<String> = ArrayList()
    private var preferButtonState : ArrayList<Int> = ArrayList()
    private var matchIdInit : ArrayList<String> = ArrayList()

    private var signupIdState : String = ""

    private var wrongInput : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySignupBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        setContentView(binding.root)


        if (signupIdState == "wrong_format"){
            binding.idFail.setText("단국대 이메일을 입력해주세요.")
            binding.idFail.visibility = View.VISIBLE
        }
        else
            binding.idFail.visibility = View.INVISIBLE

        binding.userSex.setOnCheckedChangeListener(CheckboxListener())

        sportsList.add("테니스")
        sportsList.add("축구")
        sportsList.add("농구")
        sportsList.add("족구")
        sportsList.add("풋살")

        preferButton.add(binding.buttonTennis)
        preferButton.add(binding.buttonSoccer)
        preferButton.add(binding.buttonBasketball)
        preferButton.add(binding.buttonJokgoo)
        preferButton.add(binding.buttonFutsal)

        for (i in 0 until preferButton.size step (1)) {
            preferButtonState.add(0)
        }

        for (i in 0 until preferButton.size step (1)) {
            preferButton[i].setOnClickListener(this)
        }

        binding.buttonSignUp.setOnClickListener{
            signup()
        }
    }

    override fun onClick(v : View?) {
        for (i in 0 until preferButton.size step (1)){
            if(preferButton[i].isChecked == true) {
                preferButtonState.set(i, 1)
                preferButton[i].setBackground(ContextCompat.getDrawable(this, R.drawable.btn_event_stroke))
            }
            else
            {
                preferButtonState.set(i, 0)
                preferButton[i].setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
        }
    }

    inner class CheckboxListener : RadioGroup.OnCheckedChangeListener{
        override fun onCheckedChanged(group: RadioGroup?, checkdId: Int) {
            when(group?.id){
                R.id.user_sex ->
                    when(checkdId) {
                        R.id.checkbox_male -> userSex = "남성"
                        R.id.checkbox_female -> userSex = "여성"
                    }
            }
        }
    }

    private fun signup() {
        var userEmail = binding.emailEdittext.text.toString()
        var userPwd = binding.passwordEdittext.text.toString()
        var pwdCheck = binding.passwordCheckEdittext.text.toString()
        var userName = binding.usernameEdittext.text.toString()
        var userBirth = binding.birthEdittext.text.toString()
        var userInfo = arrayOf(userName, userBirth)

        // 입력 칸이 비어있다면 리턴
        if (TextUtils.isEmpty(userPwd)) {
            wrongInput = 1
            binding.signUpPasswordFail.setText("비밀번호를 입력해주세요.")
            binding.signUpPasswordFail.visibility = View.VISIBLE
            binding.signupPasswordCheckFail.setText("비밀번호를 입력해주세요.")
            binding.signupPasswordCheckFail.visibility = View.VISIBLE
        }else{
            binding.signUpPasswordFail.visibility = View.INVISIBLE
            binding.signupPasswordCheckFail.visibility = View.INVISIBLE
        }

        if (!TextUtils.equals(userPwd, pwdCheck)) {
            wrongInput = 1
            binding.signupPasswordCheckFail.visibility = View.VISIBLE
        }else if(TextUtils.isEmpty(pwdCheck)) {
            wrongInput = 1
            binding.signupPasswordCheckFail.setText("비밀번호를 입력해주세요.")
            binding.signupPasswordCheckFail.visibility = View.VISIBLE
        }
        else{
            binding.signupPasswordCheckFail.visibility = View.INVISIBLE
        }


        if (TextUtils.isEmpty(userName))  {
            wrongInput = 1
            binding.signupNameFail.visibility = View.VISIBLE
        }else{
            binding.signupNameFail.visibility = View.INVISIBLE
        }

        if (TextUtils.isEmpty(userBirth)) {
            wrongInput = 1
            binding.signupBirthFail.visibility = View.VISIBLE
        }else{
            binding.signupBirthFail.visibility = View.INVISIBLE

        }



        if (userSex.equals("")){
            wrongInput = 1
            binding.signupSexFail.visibility = View.VISIBLE

        }else{
            binding.signupSexFail.visibility = View.INVISIBLE
        }




        if ((preferButtonState[0] or preferButtonState[1] or preferButtonState[2] or preferButtonState[3] or preferButtonState[4]).equals(0)) {
            wrongInput = 1
            binding.signupPreferFail.visibility = View.VISIBLE


        }else{
            binding.signupPreferFail.visibility = View.INVISIBLE
        }

        // DKU 계정인지 확인
        var emailArr = userEmail.split("@")

        if (userEmail.contains("@")) {
            if (((emailArr[0].length == 8) and (emailArr[1].equals("dankook.ac.kr")))) {
                binding.idFail.visibility = View.INVISIBLE

                auth?.createUserWithEmailAndPassword(userEmail, userPwd)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // db에 저장
                            setUserData(task.result?.user, userInfo)
                            moveLoginPage(task.result?.user)
                        } else {
                            //if you have account move to login page
                            if (task.exception?.message.equals("The email address is already in use by another account.")) {
                                Toast.makeText(this, "이미 존재하는 ID 입니다.", Toast.LENGTH_LONG).show()
                            }
                            //Show the error message
                            else {
                                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }


            }
            else {
                wrongInput=1
                binding.idFail.setText("올바른 양식의 아이디를 입력해주세요.")
                binding.idFail.visibility = View.VISIBLE
            }
        }else {
            wrongInput= 1
            binding.idFail.setText("올바른 양식의 아이디를 입력해주세요.")
            binding.idFail.visibility = View.VISIBLE
        }

        if (wrongInput.equals(1)){
            return
        }
    }

    private fun setUserData(user: FirebaseUser?, userInfo: Array<String>) {
        var UserDTO = UserDTO()

        // uid, userId, name, time db에 저장
        if (user != null) {
            // Insert name
            UserDTO.name = userInfo[0]

            // Insert birth
            UserDTO.birth = userInfo[1]

            UserDTO.sex = userSex

            matchIdInit.add("-1")
            UserDTO.matchId = matchIdInit

            for (i in 0 until preferButton.size step (1)) {
                if(preferButtonState[i] == 1)
                    userPreference.add(sportsList[i])
            }

            UserDTO.preference = userPreference

            database.child("user").child(user.uid).setValue(UserDTO)

        }
    }
    private fun moveLoginPage(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this, "회원가입에 성공했습니다.", Toast.LENGTH_LONG).show()
            // 다음 페이지로 넘어가는 Intent
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }
}