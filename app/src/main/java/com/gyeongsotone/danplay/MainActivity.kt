package com.gyeongsotone.danplay

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.os.Bundle
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    var bottomNav: BottomNavigationView? = null
    var fragment1: Mainpage? = null
    var fragment2: Myinfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.bottomNavigationView)
        //프래그먼트 생성
        fragment1 = Mainpage()
        fragment2 = Myinfo()

        //제일 처음 띄워줄 뷰를 세팅해줍니다. commit();까지 해줘야 합니다.
        supportFragmentManager.beginTransaction().replace(R.id.main_layout, fragment1!!)
            .commitAllowingStateLoss()
        //bottomnavigationview의 아이콘을 선택 했을때 원하는 프래그먼트가 띄워질 수 있도록 리스너를 추가합니다.
        bottomNav!!.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.searchItem -> {
                    supportFragmentManager
                        .beginTransaction().replace(R.id.main_layout, fragment1!!)
                        .commitAllowingStateLoss()
                    true
                }
                R.id.infoItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout, fragment2!!).commitAllowingStateLoss()
                    true
                }
                else -> false
            }
        }
        )
    }
}