package com.gyeongsotone.danplay

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SearchFragment : Fragment() {
    var viewGroup: ViewGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.fragment_search, container, false) as ViewGroup
        var btn_filter = viewGroup!!.findViewById<View>(R.id.btn_filter) as ImageView
        btn_filter.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext, FilterActivity::class.java)
            startActivity(intent)
        }

        // 날짜변환
        var calendarView = viewGroup!!.findViewById<View>(R.id.calendarView) as CalendarView
        var today = viewGroup!!.findViewById<View>(R.id.today) as TextView
        val formatter: DateFormat = SimpleDateFormat("yyyy년MM월dd일")
        val date = Date(calendarView!!.date)
        today!!.text = formatter.format(date)
        calendarView!!.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            val day: String
            day = year.toString() + "년" + (month + 1) + "월" + dayOfMonth + "일"
            today!!.text = day
        }


        return viewGroup
    }
}