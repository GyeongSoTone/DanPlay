package com.gyeongsotone.danplay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment

class SearchFragment : Fragment() {
    // var viewGroup: ViewGroup? = null
    var listview : ListView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view:View = inflater.inflate(R.layout.fragment_search, container, false)
        listview = view.findViewById<ListView>(R.id.mainListView)

        return view
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val list_item = mutableListOf<ListViewModel>()
        list_item.add(ListViewModel("1", "풋살 | 10/05 | 16:00 | 단국대 풋살 경기장 | 5/8", "같이 풋살 하실 분 구합니다. 공은 저희가 가져오겠습니다."))
        list_item.add(ListViewModel("2", "풋살 | 10/06 | 17:00 | 단국대 풋살 경기장 | 4/8", "피파온라인하다가 그냥 갑자기 요를레히 합니다. 같이 풋살하실 단국 죽전인 여러분! 오늘도 화이팅!피파온라인하다가 그냥 갑자기 요를레히 합니다. 같이 풋살하실 단국 죽전인 여러분! 오늘도 화이팅!피파온라인하다가 그냥 갑자기 요를레히 합니다. 같이 풋살하실 단국 죽전인 여러분! 오늘도 화이팅!"))
        list_item.add(ListViewModel("3", "농구 | 10/07 | 14:00 | 평화의 광장 농구장 A | 7/10", "슬램덩크하다가 그냥 갑자기 농구하고 싶다.. 같이 농구하실 분 구합니다~"))
        list_item.add(ListViewModel("4", "농구 | 10/07 | 15:00 | 평화의 광장 농구장 B | 4/8", "농구할 사람 구합니다. 좀 잘하시는 분이면 환영합니다. 공은 저희가 챙겨오겠습니다."))
        list_item.add(ListViewModel("5", "테니스 | 10/10 | 10:00 | 단국대 테니스장 B | 1/2", "테니스의 왕자 보다가 그냥 갑자기 테니스하고 싶다.. 같이 테니스 치실 분 구합니다~"))
        list_item.add(ListViewModel("6", "풋살 | 10/05 | 16:00 | 단국대 풋살 경기장 | 5/8", "같이 풋살 하실 분 구합니다. 공은 저희가 가져오겠습니다."))
        list_item.add(ListViewModel("7", "풋살 | 10/06 | 17:00 | 단국대 풋살 경기장 | 4/8", "피파온라인하다가 그냥 갑자기 요를레히 합니다. 같이 풋살하실 단국 죽전인 여러분! 오늘도 화이팅!"))
        list_item.add(ListViewModel("8", "농구 | 10/07 | 14:00 | 평화의 광장 농구장 A | 7/10", "슬램덩크하다가 그냥 갑자기 농구하고 싶다.. 같이 농구하실 분 구합니다~"))
        list_item.add(ListViewModel("9", "농구 | 10/07 | 15:00 | 평화의 광장 농구장 B | 4/8", "농구할 사람 구합니다. 좀 잘하시는 분이면 환영합니다. 공은 저희가 챙겨오겠습니다."))
        list_item.add(ListViewModel("10", "테니스 | 10/10 | 10:00 | 단국대 테니스장 B | 1/2", "테니스의 왕자 보다가 그냥 갑자기 테니스하고 싶다.. 같이 테니스 치실 분 구합니다~"))
        list_item.add(ListViewModel("11", "풋살 | 10/05 | 16:00 | 단국대 풋살 경기장 | 5/8", "같이 풋살 하실 분 구합니다. 공은 저희가 가져오겠습니다."))
        list_item.add(ListViewModel("12", "풋살 | 10/06 | 17:00 | 단국대 풋살 경기장 | 4/8", "피파온라인하다가 그냥 갑자기 요를레히 합니다. 같이 풋살하실 단국 죽전인 여러분! 오늘도 화이팅!"))
        list_item.add(ListViewModel("13", "농구 | 10/07 | 14:00 | 평화의 광장 농구장 A | 7/10", "슬램덩크하다가 그냥 갑자기 농구하고 싶다.. 같이 농구하실 분 구합니다~"))
        list_item.add(ListViewModel("14", "농구 | 10/07 | 15:00 | 평화의 광장 농구장 B | 4/8", "농구할 사람 구합니다. 좀 잘하시는 분이면 환영합니다. 공은 저희가 챙겨오겠습니다."))
        list_item.add(ListViewModel("15", "테니스 | 10/10 | 10:00 | 단국대 테니스장 B | 1/2", "테니스의 왕자 보다가 그냥 갑자기 테니스하고 싶다.. 같이 테니스 치실 분 구합니다~"))

        val listviewAdapter = ListViewAdapter(list_item)
        listview?.adapter = listviewAdapter

        listview?.setOnItemClickListener{ parent, view, position, id ->
            //Toast.makeText(this, "상세 내용 페이지로 이동 예정!", Toast.LENGTH_LONG).show()
        }
    }
}