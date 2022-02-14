package com.gyeongsotone.danplay

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.gyeongsotone.danplay.ui.matchapply.ApplyFragment
import com.gyeongsotone.danplay.ui.main.MainPageFragment
import com.gyeongsotone.danplay.ui.mypage.MyInfoFragment
import com.gyeongsotone.danplay.ui.matchsearch.SearchFragment

class MainActivity : AppCompatActivity() {

    private var backBtnDoubleClick = false
    private var bottomNav: BottomNavigationView? = null

    //프래그먼트 생성
    private val mainFragment = MainPageFragment()
    private val myInfoFragment = MyInfoFragment()
    private val searchFragment = SearchFragment()
    private val applyFragment = ApplyFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //제일 처음 띄워줄 뷰를 세팅한다. - mainFragment
        supportFragmentManager.beginTransaction().replace(R.id.main_layout, mainFragment)
            .commitAllowingStateLoss()
        //bottomNavigationView 아이콘을 선택 했을때 원하는 프래그먼트가 띄워질 수 있도록 리스너를 추가합니다.
        fragmentListener()
    }

    private fun fragmentListener() {
        bottomNav = findViewById(R.id.bottomNavigationView)
        bottomNav!!.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeItem -> {
                    supportFragmentManager
                        .beginTransaction().replace(R.id.main_layout, mainFragment)
                        .commitAllowingStateLoss()
                    true
                }
                R.id.infoItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout, myInfoFragment).commitAllowingStateLoss()
                    true
                }
                R.id.searchItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout, searchFragment).commitAllowingStateLoss()
                    true
                }
                R.id.applyItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout, applyFragment).commitAllowingStateLoss()
                    true
                }
                else -> false
            }
        }
    }

    override fun onBackPressed() {
        if (backBtnDoubleClick)
            finish()
        backBtnDoubleClick = true
        Toast.makeText(this, "종료하시려면 더블 클릭 하세요.", Toast.LENGTH_LONG).show()
        Handler(Looper.getMainLooper()).postDelayed({
            backBtnDoubleClick = false
        }, 2000)
    }
}
