package com.gyeongsotone.danplay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ListView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var backBtnDoubleClick = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list_item = mutableListOf<ListViewModel>()
        list_item.add(ListViewModel("조윤근", "풋살 | 10/05 | 16:00 | 단국대 풋살 경기장 | 5/8", "같이 풋살 하실 분 구합니다. 공은 저희가 가져오겠습니다."))
        list_item.add(ListViewModel("조태규", "풋살 | 10/06 | 17:00 | 단국대 풋살 경기장 | 4/8", "피파온라인하다가 그냥 갑자기 요를레히 합니다. 같이 풋살하실 단국 죽전인 여러분! 오늘도 화이팅!"))
        list_item.add(ListViewModel("김홍덕", "농구 | 10/07 | 14:00 | 평화의 광장 농구장 A | 7/10", "슬램덩크하다가 그냥 갑자기 농구하고 싶다.. 같이 농구하실 분 구합니다~"))
        list_item.add(ListViewModel("소재헌", "농구 | 10/07 | 15:00 | 평화의 광장 농구장 B | 4/8", "농구할 사람 구합니다. 좀 잘하시는 분이면 환영합니다. 공은 저희가 챙겨오겠습니다."))
        list_item.add(ListViewModel("홍경현", "테니스 | 10/10 | 10:00 | 단국대 테니스장 B | 1/2", "테니스의 왕자 보다가 그냥 갑자기 테니스하고 싶다.. 같이 테니스 치실 분 구합니다~"))

        val listview = findViewById<ListView>(R.id.mainListView)

        val listviewAdapter = ListViewAdapter(list_item)
        listview.adapter = listviewAdapter

        listview.setOnItemClickListener{ parent, view, position, id ->
            Toast.makeText(this, "상세 내용 페이지로 이동 예정!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        if (backBtnDoubleClick == true)
            finish()
        backBtnDoubleClick = true

        Toast.makeText(this, "종료하시려면 더블 클릭 하세요.", Toast.LENGTH_LONG).show()

        Handler().postDelayed(Runnable{
            backBtnDoubleClick = false
        },2000)
    }
}