package com.gyeongsotone.danplay

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gyeongsotone.danplay.databinding.ActivitySignupBinding
import com.gyeongsotone.danplay.databinding.FragmentApplyBinding
import com.gyeongsotone.danplay.model.MatchDTO
import java.util.*


class ApplyFragment : Fragment() {
    var viewGroup: ViewGroup? = null
    private lateinit var database: DatabaseReference
    var g_sport: String? = null
    var g_people: Int? = null
    var g_place: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.fragment_apply, container, false) as ViewGroup

        var btn_event = viewGroup!!.findViewById<View>(R.id.btn_event) as Button
        var btn_people = viewGroup!!.findViewById<View>(R.id.btn_people) as Button
        var btn_place = viewGroup!!.findViewById<View>(R.id.btn_where) as Button
        var btn_time = viewGroup!!.findViewById<View>(R.id.btn_time) as Button

        val list = arrayOf<String>()

        btn_event.setOnClickListener {
            //Toast.makeText(getActivity(), "~", Toast.LENGTH_LONG).show()
            val sports = arrayOf("테니스", "축구", "농구", "족구", "풋살")
            var selectedItem: String? = null

            val builder = AlertDialog.Builder(getActivity())
                .setTitle("종목을 선택하세요")
                .setSingleChoiceItems(sports, -1) { dialog, which ->
                    selectedItem = sports[which]
                }
                .setPositiveButton("확인") { dialog, which ->
                    g_sport = selectedItem.toString()
                    //Toast.makeText(getActivity(),g_sport, Toast.LENGTH_SHORT).show()
                    btn_event.setText(g_sport)
                }
                .show()
        }

        btn_people.setOnClickListener {
            //val people = arrayOf("테니스", "축구", "농구", "족구", "풋살")
            val people = arrayOf<String>("2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22")
            var selectedItem: String? = null

            val builder = AlertDialog.Builder(getActivity())
                .setTitle("인원수를 선택하세요")
                .setSingleChoiceItems(people, -1) { dialog, which ->
                    selectedItem = people[which]
                }
                .setPositiveButton("확인") { dialog, which ->
                    btn_people.setText(selectedItem)
                    g_people = selectedItem!!.toInt()
                    //Toast.makeText(getActivity(),g_people.toString(), Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        btn_place.setOnClickListener {
            val place = arrayOf<String>("노천마당", "대운동장", "인문관소극장", "족구장", "테니스장A",
                "테니스장C", "평화의광장 농구장A", "평화의광장 농구장B", "평화의광장 농구장C", "폭포공원",
                "풋살경기장", "혜당관(학생극장 2층로비)", "혜당관(학생회관)212(학생극장)")
            var selectedItem: String? = null

            val builder = AlertDialog.Builder(getActivity())
                .setTitle("장소를 선택하세요")
                .setSingleChoiceItems(place, -1) { dialog, which ->
                    selectedItem = place[which]
                }
                .setPositiveButton("확인") { dialog, which ->
                    g_place = selectedItem.toString()
                    //Toast.makeText(getActivity(),g_sport, Toast.LENGTH_SHORT).show()
                    btn_place.setText(g_place)
                }
                .show()
        }

        btn_place.setOnClickListener {
            val place = arrayOf<String>("노천마당", "대운동장", "인문관소극장", "족구장", "테니스장A",
                "테니스장C", "평화의광장 농구장A", "평화의광장 농구장B", "평화의광장 농구장C", "폭포공원",
                "풋살경기장", "혜당관(학생극장 2층로비)", "혜당관(학생회관)212(학생극장)")
            var selectedItem: String? = null

            val builder = AlertDialog.Builder(getActivity())
                .setTitle("장소를 선택하세요")
                .setSingleChoiceItems(place, -1) { dialog, which ->
                    selectedItem = place[which]
                }
                .setPositiveButton("확인") { dialog, which ->
                    g_place = selectedItem.toString()
                    //Toast.makeText(getActivity(),g_sport, Toast.LENGTH_SHORT).show()
                    btn_place.setText(g_place)
                }
                .show()
        }

        btn_time.setOnClickListener {
            var calendar = Calendar.getInstance()
            var year = calendar.get(Calendar.YEAR)
            var month = calendar.get(Calendar.MONTH)
            var day = calendar.get(Calendar.DAY_OF_MONTH)
            var hour = calendar.get(Calendar.HOUR)
            var minute = calendar.get(Calendar.MINUTE)

            var listener1 = DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                year = i
                month = i2
                day = i3
                btn_time.setText("${year}년 ${month}월 ${day}일")
            }
            var picker1 = getActivity()?.let { it1 -> DatePickerDialog(it1, listener1, year, month, day) }
            picker1!!.show()

            var listener2 = TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                btn_time.setText("${i}시 ${i2}분")
            }
            var picker2 = TimePickerDialog(getActivity(), listener2, hour, minute, false ) // true하면 24시간 제
            picker2.show()
        }
        return viewGroup
    }
}
