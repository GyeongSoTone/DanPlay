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
        var button = findViewById<Button>(R.id.button_participation) as Button
        val match = intent.getSerializableExtra("matchInfo") as ListViewModel
        name.text = match.name
        var listStrTitle = match.title.split("|")
        sports.text = listStrTitle[0].trim()
        number.text = listStrTitle[4].trim()
        time.text = listStrTitle[1].trim() + " " + listStrTitle[2].trim()
        place.text = listStrTitle[3].trim()
        content.text = match.content

        button.setOnClickListener {
            Toast.makeText(this, "참여버튼 기능 추가하기", Toast.LENGTH_LONG).show()
        }

    }

}