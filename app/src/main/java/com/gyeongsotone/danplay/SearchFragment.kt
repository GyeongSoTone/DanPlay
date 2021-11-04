package com.gyeongsotone.danplay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gyeongsotone.danplay.adapter.ListViewAdapter
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {
    var viewGroup: ViewGroup? = null
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.fragment_search, container, false) as ViewGroup
        val btnFilter = viewGroup!!.findViewById<View>(R.id.btn_filter) as ImageView
        btnFilter.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext, FilterActivity::class.java)
            startActivity(intent)
        }
        database = Firebase.database.reference

        val matchDb = database.child("match")
        var MapItem = mutableMapOf<String, ListViewModel>()
        val listItem = arrayListOf<ListViewModel>()
        var sports : String
        var totalNum : String
        var currentNum : String
        var playTimeDate : String
        var place : String
        var content : String
        var applyTime : String
        var playTime : String
        var playDate : String
        var registrant : String
        var title : String
        matchDb.get().addOnSuccessListener {
            for (mId in it.children) {
                currentNum = it.child(mId.key.toString()).child("currentNum").value.toString()
                totalNum = it.child(mId.key.toString()).child("totalNum").value.toString()
                if (totalNum == currentNum) {
                    continue
                }
                sports = it.child(mId.key.toString()).child("sports").value.toString()
                playTimeDate = it.child(mId.key.toString()).child("playTime").value.toString()
                if (playTimeDate.split(" ").size == 2) {
                    playDate = playTimeDate.split(" ")[0]
                    playTime = playTimeDate.split(" ")[1].substring(0,5)
                } else {
                    playDate = "0"
                    playTime = "0"
                }
                applyTime = it.child(mId.key.toString()).child("applyTime").value.toString()
                place = it.child(mId.key.toString()).child("place").value.toString()
                content = it.child(mId.key.toString()).child("content").value.toString()
                registrant = it.child(mId.key.toString()).child("registrant").value.toString()
                title = sports.plus(" | ${playDate} | ${playTime} | ${place} | ${currentNum}/${totalNum}")
                MapItem.put(playTimeDate, ListViewModel(registrant, title, content))
//                listItem.add(0,ListViewModel(registrant, title, content))
            }
//            var sortedByValue = MapItem.toList().sortedWith(compareBy({it.first})).toMap()
            MapItem = MapItem.toSortedMap(reverseOrder())
            var i = 0
            for (value in MapItem.values) {
                Log.i("소재헌+_+", value.toString())
                listItem.add(0, value)
            }


            // 전체 리스트뷰 보여주기
            getListView(listItem)
        }.addOnFailureListener{
            Toast.makeText(context, "실패", Toast.LENGTH_LONG).show()
        }

        // 날짜변환
        val calendarView = viewGroup!!.findViewById<View>(R.id.calendarView) as CalendarView
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val day: String
            val strMonth : String
            val strDay : String
            if (month+1 < 10) {
                strMonth = "0" + (month+1).toString()
            } else {
                strMonth = (month+1).toString()
            }
            if (dayOfMonth < 10) {
                strDay = "0" + dayOfMonth.toString()
            } else {
                strDay = dayOfMonth.toString()
            }
            day = year.toString() + "-" + strMonth + "-" + strDay
            showList(listItem, day)
        }

        return viewGroup
    }

    fun showList(listItem: ArrayList<ListViewModel>, day:String) {
        val selectedListItem = arrayListOf<ListViewModel>()
        var listStrTitle : List<String>
        var listTitle : String
        var listDate : String
        val pickedDay = day.substring(0)
        var i = 0
        for (item in listItem) {
            listTitle = item.title
            listStrTitle = listTitle.split("|")
            listDate = listStrTitle[1].trim()
            if (listDate == pickedDay) {
                selectedListItem.add(i, item)
                i++
            }
        }
        getListView(selectedListItem)
    }

    fun getListView(listItem: ArrayList<ListViewModel>) {

        val listview = viewGroup!!.findViewById<ListView>(R.id.mainListView)
        val listviewAdapter = ListViewAdapter(listItem)
        listview.adapter = listviewAdapter
        listview.setOnItemClickListener{ parent, view, position, id ->
            val clickedList = listItem[position]
            val intent = Intent(requireActivity().applicationContext, DetailActivity::class.java)
            intent.putExtra("matchInfo", clickedList)
            startActivity(intent)
        }
    }
}