package com.gyeongsotone.danplay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.gyeongsotone.danplay.databinding.ActivityDetailBinding
import com.gyeongsotone.danplay.databinding.ActivitySignupBinding

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var name = findViewById<View>(R.id.textview_name) as TextView
        var sports = findViewById<View>(R.id.textview_sports) as TextView
        var number = findViewById<View>(R.id.textview_number) as TextView
        var time = findViewById<View>(R.id.textview_time) as TextView
        var place = findViewById<View>(R.id.textview_place) as TextView
        var content = findViewById<View>(R.id.textview_content) as TextView
        var button = findViewById<Button>(R.id.button_participation)
        val match = intent.getSerializableExtra("matchInfo") as ListViewModel
        name.text = match.name
        var listStrTitle = match.title.split("|")
        sports.text = listStrTitle[0].trim()
        number.text = listStrTitle[4].trim()
        time.text = listStrTitle[1].trim() + " " + listStrTitle[2].trim()
        place.text = listStrTitle[3].trim()
        content.text = match.content

        button.setOnClickListener {
            /*
             참여하기 버튼 클릭 시
             if (현재 num == 토탈 num) => 토스트메시지: 가득찼다.
             else =>
                if (유저의 matchId 중 신청하려는 match의 시간 and 장소가 동일하다면) => 토스트메시지: 중복이다.
                else =>
                    1. 현재 유저의 matchId에 저장 (추가)
                    2. 해당 match의 current멤버에 저장 (추가)

             이전 페이지로 이동 (인원수 업데이트 되어야 함)
            */
            Toast.makeText(this, "참여버튼 기능 추가하기", Toast.LENGTH_LONG).show()

        }

    }

}