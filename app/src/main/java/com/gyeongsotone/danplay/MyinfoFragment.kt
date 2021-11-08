package com.gyeongsotone.danplay

import android.app.AlertDialog
import android.content.Intent
import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gyeongsotone.danplay.adapter.ListViewAdapter
import kotlin.collections.ArrayList

class MyinfoFragment : Fragment() {
    var viewGroup: ViewGroup? = null
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var listItem = arrayListOf<ListViewModel>()
    private var mapItem = mutableMapOf<String, ListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.fragment_myinfo, container, false) as ViewGroup
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser!!
        var btnSignout = viewGroup!!.findViewById<View>(R.id.signout) as Button
        var btnLogout = viewGroup!!.findViewById<View>(R.id.logout) as Button
        var btnContact = viewGroup!!.findViewById<View>(R.id.contact) as Button

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var i = 0
                var parent = dataSnapshot
                for(snapshot in dataSnapshot.child("user").child(currentUser.uid).child("matchId").children){
                    var myMatch = snapshot.value.toString()
                    for(snapshot in dataSnapshot.child("match").children){
                        if(snapshot.key.equals(myMatch)){
                            i = 1
                            listItem = getMatchDb(snapshot, parent) // 여기
                        }
                    }
                }
                if(i == 0){
                    listItem.clear()
                    listItem.add(0, ListViewModel("", "", "예약된 매치 없음", ""))
                }
                getListView(listItem)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(getActivity(), "DB 읽기 실패", Toast.LENGTH_LONG).show()
            }
        })


        // 회원탈퇴
        btnSignout.setOnClickListener {
            val alertSignout = AlertDialog.Builder(getActivity())
            alertSignout.setMessage("정말로 회원탈퇴 하시겠습니까?")
            // 확인버튼
            alertSignout.setPositiveButton("확인") { dialog, which ->
                database.child("user").child(currentUser.uid).removeValue()
                    .addOnSuccessListener {
                        // Write was successful!
                        // ..
                    }
                    .addOnFailureListener {
                        // Write failed
                        // ..
                    }
                currentUser.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(getActivity(), "회원탈퇴 완료.", Toast.LENGTH_LONG).show()
                            val intent = Intent(requireActivity().applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
            }
            // 취소버튼
            alertSignout.setNegativeButton("취소", null)
            val alert = alertSignout.create()
            alert.setIcon(R.drawable.logo_danplay)
            alert.setTitle("회원탈퇴")
            alert.show()
        }
        // 로그아웃
        btnLogout.setOnClickListener {
            val alertLogout = AlertDialog.Builder(getActivity())
            alertLogout.setMessage("정말로 로그아웃 하시겠습니까?")
            // 확인버튼
            alertLogout.setPositiveButton("확인") { dialog, which ->
                Toast.makeText(getActivity(), "로그아웃 완료.", Toast.LENGTH_LONG).show()
                val intent = Intent(requireActivity().applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
            // 취소버튼
            alertLogout.setNegativeButton("취소", null)
            val alert = alertLogout.create()
            alert.setIcon(R.drawable.logo_danplay)
            alert.setTitle("로그아웃")
            alert.show()
        }
        // 문의하기
        btnContact.setOnClickListener {
            val alertContact = AlertDialog.Builder(getActivity())
            alertContact.setMessage("yunx2@naver.com으로 문의해주세요.")
            alertContact.setPositiveButton("확인", null)
            val alert = alertContact.create()
            alert.setIcon(R.drawable.logo_danplay)
            alert.setTitle("문의하기")
            alert.show()
        }
        return viewGroup
    }

    fun getMatchDb(it : DataSnapshot, parent: DataSnapshot) : ArrayList<ListViewModel> {
        val localListItem = arrayListOf<ListViewModel>()
        var applyTime : String
        var playTime : String
        var playDate : String
        var title : String

        var currentNum : String = it.child("registrant").childrenCount.toString()
        var totalNum : String = it.child("totalNum").value.toString()
        var sports : String = it.child("sports").value.toString()
        var playTimeDate : String = it.child("playTime").value.toString()
        if (playTimeDate.split(" ").size == 2) {
            playDate = playTimeDate.split(" ")[0]
            playTime = playTimeDate.split(" ")[1].substring(0,5)
        } else {
            playDate = "0"
            playTime = "0"
        }
        applyTime = it.child("applyTime").value.toString()
        var place : String = it.child("place").value.toString()
        var content : String = it.child("content").value.toString()
        var registrant : String = it.child("registrant").child("0").value.toString()
        var name : String = parent.child("user").child(registrant).child("name").value.toString()
        title = sports.plus(" | ${playDate} | ${playTime} | ${place} | ${currentNum}/${totalNum}")
        mapItem.put(playTimeDate, ListViewModel(name, title, content, it.key.toString()))

        mapItem = mapItem.toSortedMap(reverseOrder())
        for (value in mapItem.values)
            localListItem.add(0, value)
        return localListItem
    }

    fun getListView(listItem: ArrayList<ListViewModel>) {
        val listview = viewGroup!!.findViewById<ListView>(R.id.mymatchListView)
        val listviewAdapter = ListViewAdapter(listItem)
        listview.adapter = listviewAdapter
        listview.setOnItemClickListener{ parent, view, position, id ->
            val clickedList = listItem[position]
            val intent = Intent(requireActivity().applicationContext, MyMatchDetailActivity::class.java)
            intent.putExtra("matchInfo", clickedList)
            intent.putExtra("matchId", clickedList.matchId)
            startActivity(intent)
        }
    }
}