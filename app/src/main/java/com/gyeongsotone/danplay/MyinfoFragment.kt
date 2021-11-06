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
        //var listItem = arrayListOf<ListViewModel>()  // 여기
        var current_user = auth.currentUser!!
        var btn_signout = viewGroup!!.findViewById<View>(R.id.signout) as Button
        var btn_logout = viewGroup!!.findViewById<View>(R.id.logout) as Button
        var btn_contact = viewGroup!!.findViewById<View>(R.id.contact) as Button
        //var btn_profile_change = viewGroup!!.findViewById<View>(R.id.profile_change) as Button

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var i = 0
                var parent = dataSnapshot
                for(snapshot in dataSnapshot.child("user").child(current_user.uid).child("matchId").children){
                    var my_match = snapshot.value.toString()
                    for(snapshot in dataSnapshot.child("match").children){
                        i = 1
                        if(snapshot.key.equals(my_match)){
                            listItem = getMatchDb(snapshot, parent) // 여기
                            //getListView(listItem)
                        }
                    }
                }
                if(i == 0){
                    listItem.clear()
                    listItem.add(0, ListViewModel("", "", "예약된 매치 없음"))
                }
                getListView(listItem)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(getActivity(), "DB 읽기 실패", Toast.LENGTH_LONG).show()
            }
        })


        // 회원탈퇴
        btn_signout.setOnClickListener {
            val alert_signout = AlertDialog.Builder(getActivity())
            alert_signout.setMessage("정말로 회원탈퇴 하시겠습니까?")
            // 확인버튼
            alert_signout.setPositiveButton("확인") { dialog, which ->
                database.child("user").child(current_user.uid).removeValue()
                    .addOnSuccessListener {
                        // Write was successful!
                        // ..
                    }
                    .addOnFailureListener {
                        // Write failed
                        // ..
                    }
                current_user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //Log.d(TAG, "User account deleted.")
                            Toast.makeText(getActivity(), "회원탈퇴 완료.", Toast.LENGTH_LONG).show()
                            val intent = Intent(requireActivity().applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
            }
            // 취소버튼
            alert_signout.setNegativeButton("취소", null)
            val alert = alert_signout.create()
            alert.setIcon(R.drawable.logo_danplay)
            alert.setTitle("회원탈퇴")
            alert.show()
        }
        // 로그아웃
        btn_logout.setOnClickListener {
            val alert_logout = AlertDialog.Builder(getActivity())
            alert_logout.setMessage("정말로 로그아웃 하시겠습니까?")
            // 확인버튼
            alert_logout.setPositiveButton("확인") { dialog, which ->
                Toast.makeText(getActivity(), "로그아웃 완료.", Toast.LENGTH_LONG).show()
                val intent = Intent(requireActivity().applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
            // 취소버튼
            alert_logout.setNegativeButton("취소", null)
            val alert = alert_logout.create()
            alert.setIcon(R.drawable.logo_danplay)
            alert.setTitle("로그아웃")
            alert.show()
        }
        // 문의하기
        btn_contact.setOnClickListener {
            val alert_contact = AlertDialog.Builder(getActivity())
            alert_contact.setMessage("yunx2@naver.com으로 문의해주세요.")
            alert_contact.setPositiveButton("확인", null)
            val alert = alert_contact.create()
            alert.setIcon(R.drawable.logo_danplay)
            alert.setTitle("문의하기")
            alert.show()
        }
        return viewGroup
    }

    fun getMatchDb(it : DataSnapshot, parent: DataSnapshot) : ArrayList<ListViewModel> {
        val local_listItem = arrayListOf<ListViewModel>()
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
        var name : String

        currentNum = it.child("registrant").childrenCount.toString()
        totalNum = it.child("totalNum").value.toString()
        sports = it.child("sports").value.toString()
        playTimeDate = it.child("playTime").value.toString()
        if (playTimeDate.split(" ").size == 2) {
            playDate = playTimeDate.split(" ")[0]
            playTime = playTimeDate.split(" ")[1].substring(0,5)
        } else {
            playDate = "0"
            playTime = "0"
        }
        applyTime = it.child("applyTime").value.toString()
        place = it.child("place").value.toString()
        content = it.child("content").value.toString()
        registrant = it.child("registrant").child("0").value.toString()
        name = parent.child("user").child(registrant).child("name").value.toString()
        title = sports.plus(" | ${playDate} | ${playTime} | ${place} | ${currentNum}/${totalNum}")
        mapItem.put(playTimeDate, ListViewModel(name, title, content))

        mapItem = mapItem.toSortedMap(reverseOrder())
        for (value in mapItem.values)
            local_listItem.add(0, value)
        return local_listItem
    }

    fun getListView(listItem: ArrayList<ListViewModel>) {
        val listview = viewGroup!!.findViewById<ListView>(R.id.mymatchListView)
        val listviewAdapter = ListViewAdapter(listItem)
        listview.adapter = listviewAdapter
    }
}