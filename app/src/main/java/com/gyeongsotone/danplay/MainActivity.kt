package com.gyeongsotone.danplay

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    private var backBtnDoubleClick = false
    var bottomNav: BottomNavigationView? = null
    var fragment1: MainpageFragment? = null
    var fragment2: MyinfoFragment? = null
    var fragment3: SearchFragment? = null
    var fragment4: ApplyFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.bottomNavigationView)
        //프래그먼트 생성
        fragment1 = MainpageFragment()
        fragment2 = MyinfoFragment()
        fragment3 = SearchFragment()
        fragment4 = ApplyFragment()

        //제일 처음 띄워줄 뷰를 세팅해줍니다. commit();까지 해줘야 합니다.
        supportFragmentManager.beginTransaction().replace(R.id.main_layout, fragment1!!)
            .commitAllowingStateLoss()
        //bottomnavigationview의 아이콘을 선택 했을때 원하는 프래그먼트가 띄워질 수 있도록 리스너를 추가합니다.
        bottomNav!!.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeItem -> {
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
                R.id.searchItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout, fragment3!!).commitAllowingStateLoss()
                    true
                }
                R.id.applyItem -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_layout, fragment4!!).commitAllowingStateLoss()
                    true
                }
                else -> false
            }
        }
        )

    }
    override fun onBackPressed() {
        if (backBtnDoubleClick)
            finish()
        backBtnDoubleClick = true

        Toast.makeText(this, "종료하시려면 더블 클릭 하세요.", Toast.LENGTH_LONG).show()

        Handler().postDelayed(Runnable{
            backBtnDoubleClick = false
        },2000)
    }
}