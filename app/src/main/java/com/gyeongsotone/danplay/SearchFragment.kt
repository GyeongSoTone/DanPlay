package com.gyeongsotone.danplay

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {
    var viewGroup: ViewGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.fragment_search, container, false) as ViewGroup
        var btnFilter = viewGroup!!.findViewById<View>(R.id.btn_filter) as ImageView
        btnFilter.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext, FilterActivity::class.java)
            startActivity(intent)
        }

        val listItem = arrayListOf<ListViewModel>()
        listItem.add(ListViewModel("1", "풋살 | 9/1 | 16:00 | 단국대 풋살 경기장 | 5/8", "같이 풋살 하실 분 구합니다. 공은 저희가 가져오겠습니다."))
        listItem.add(ListViewModel("2", "풋살 | 10/6 | 17:00 | 단국대 풋살 경기장 | 4/8", "피파온라인하다가 그냥 갑자기 요를레히 합니다. 같이 풋살하실 단국 죽전인 여러분! 오늘도 화이팅!피파온라인하다가 그냥 갑자기 요를레히 합니다. 같이 풋살하실 단국 죽전인 여러분! 오늘도 화이팅!피파온라인하다가 그냥 갑자기 요를레히 합니다. 같이 풋살하실 단국 죽전인 여러분! 오늘도 화이팅!"))
        listItem.add(ListViewModel("3", "농구 | 10/7 | 14:00 | 평화의 광장 농구장 A | 7/10", "슬램덩크하다가 그냥 갑자기 농구하고 싶다.. 같이 농구하실 분 구합니다~"))
        listItem.add(ListViewModel("4", "농구 | 10/7 | 15:00 | 평화의 광장 농구장 B | 4/8", "농구할 사람 구합니다. 좀 잘하시는 분이면 환영합니다. 공은 저희가 챙겨오겠습니다."))
        listItem.add(ListViewModel("5", "테니스 | 11/1 | 10:00 | 단국대 테니스장 B | 1/2", "테니스의 왕자 보다가 그냥 갑자기 테니스하고 싶다.. 같이 테니스 치실 분 구합니다~"))
        listItem.add(ListViewModel("6", "풋살 | 10/5 | 16:00 | 단국대 풋살 경기장 | 5/8", "같이 풋살 하실 분 구합니다. 공은 저희가 가져오겠습니다."))
        listItem.add(ListViewModel("7", "풋살 | 10/6 | 17:00 | 단국대 풋살 경기장 | 4/8", "피파온라인하다가 그냥 갑자기 요를레히 합니다. 같이 풋살하실 단국 죽전인 여러분! 오늘도 화이팅!"))
        listItem.add(ListViewModel("8", "농구 | 10/7 | 14:00 | 평화의 광장 농구장 A | 7/10", "슬램덩크하다가 그냥 갑자기 농구하고 싶다.. 같이 농구하실 분 구합니다~"))
        listItem.add(ListViewModel("9", "농구 | 10/7 | 15:00 | 평화의 광장 농구장 B | 4/8", "농구할 사람 구합니다. 좀 잘하시는 분이면 환영합니다. 공은 저희가 챙겨오겠습니다."))
        listItem.add(ListViewModel("10", "테니스 | 10/10 | 10:00 | 단국대 테니스장 B | 1/2", "테니스의 왕자 보다가 그냥 갑자기 테니스하고 싶다.. 같이 테니스 치실 분 구합니다~"))
        listItem.add(ListViewModel("11", "풋살 | 10/5 | 16:00 | 단국대 풋살 경기장 | 5/8", "같이 풋살 하실 분 구합니다. 공은 저희가 가져오겠습니다."))
        listItem.add(ListViewModel("12", "풋살 | 10/6 | 17:00 | 단국대 풋살 경기장 | 4/8", "피파온라인하다가 그냥 갑자기 요를레히 합니다. 같이 풋살하실 단국 죽전인 여러분! 오늘도 화이팅!"))
        listItem.add(ListViewModel("13", "농구 | 10/7 | 14:00 | 평화의 광장 농구장 A | 7/10", "슬램덩크하다가 그냥 갑자기 농구하고 싶다.. 같이 농구하실 분 구합니다~"))
        listItem.add(ListViewModel("14", "농구 | 10/7 | 15:00 | 평화의 광장 농구장 B | 4/8", "농구할 사람 구합니다. 좀 잘하시는 분이면 환영합니다. 공은 저희가 챙겨오겠습니다."))
        listItem.add(ListViewModel("15", "테니스 | 10/10 | 10:00 | 단국대 테니스장 B | 1/2", "테니스의 왕자 보다가 그냥 갑자기 테니스하고 싶다.. 같이 테니스 치실 분 구합니다~"))

        // 전체 리스트뷰 보여주기
        getListView(listItem)

        // 날짜변환
        var calendarView = viewGroup!!.findViewById<View>(R.id.calendarView) as CalendarView
        calendarView!!.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            val day: String
            day = year.toString() + "년" + (month + 1) + "월" + dayOfMonth + "일"
            showList(listItem, day)
        }

        return viewGroup
    }

    fun showList(listItem: ArrayList<ListViewModel>, day:String) {
        var i = 0
        val selectedListItem = arrayListOf<ListViewModel>()
        var listStrTitle : List<String>
        var listTitle : String
        var listDate : String
        var pickedDay = day.substring(5)
        pickedDay = pickedDay.replace("월", "/")
        pickedDay = pickedDay.replace("일", "")
        while (i < listItem.size) {
            listTitle = listItem[i].title
            listStrTitle = listTitle.split("|")
            listDate = listStrTitle[1].trim()
            if (listDate == pickedDay) {
                selectedListItem.add(listItem[i])
            }
            i++
        }
        getListView(selectedListItem)
    }

    fun getListView(listItem: ArrayList<ListViewModel>) {
        val listview = viewGroup!!.findViewById<ListView>(R.id.mainListView)
        val listviewAdapter = ListViewAdapter(listItem)
        listview.adapter = listviewAdapter
        listview.setOnItemClickListener{ parent, view, position, id ->
            Toast.makeText(getActivity(), "상세 내용 페이지로 이동 예정!", Toast.LENGTH_LONG).show()
        }
    }
}