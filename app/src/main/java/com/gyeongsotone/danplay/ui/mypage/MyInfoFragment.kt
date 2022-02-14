package com.gyeongsotone.danplay.ui.mypage

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
import com.gyeongsotone.danplay.R
import com.gyeongsotone.danplay.adapter.ListViewAdapter
import com.gyeongsotone.danplay.model.ListViewModel
import com.gyeongsotone.danplay.ui.membership.LoginActivity
import kotlin.collections.ArrayList

class MyInfoFragment : Fragment() {
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
        val currentUser = auth.currentUser!!
        val btnSignOut = viewGroup!!.findViewById<View>(R.id.signout) as Button
        val btnLogout = viewGroup!!.findViewById<View>(R.id.logout) as Button
        val btnContact = viewGroup!!.findViewById<View>(R.id.contact) as Button

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var i = 0
                val parent = dataSnapshot
                for (snapshot in dataSnapshot.child("user").child(currentUser.uid)
                    .child("matchId").children) {
                    val myMatch = snapshot.value.toString()
                    for (snapshot1 in dataSnapshot.child("match").children) {
                        if (snapshot1.key.equals(myMatch)) {
                            i = 1
                            listItem = getMatchDb(snapshot1, parent) // 여기
                        }
                    }
                }
                if (i == 0) {
                    listItem.clear()
                    listItem.add(0, ListViewModel("", "", "예약된 매치 없음", ""))
                }
                getListView(listItem)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, "DB 읽기 실패", Toast.LENGTH_LONG).show()
            }
        })

        // 회원탈퇴
        btnSignOut.setOnClickListener {
            val alertSignOut = AlertDialog.Builder(activity)
            alertSignOut.setMessage("정말로 회원탈퇴 하시겠습니까?")
            // 확인버튼
            alertSignOut.setPositiveButton("확인") { dialog, which ->
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
                            Toast.makeText(activity, "회원탈퇴 완료.", Toast.LENGTH_LONG).show()
                            val intent = Intent(
                                requireActivity().applicationContext,
                                LoginActivity::class.java
                            )
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            activity?.finish()
                        }
                    }
            }
            // 취소버튼
            alertSignOut.setNegativeButton("취소", null)
            val alert = alertSignOut.create()
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
                Toast.makeText(activity, "로그아웃 완료.", Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(requireActivity().applicationContext, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity?.finish()
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

    fun getMatchDb(it: DataSnapshot, parent: DataSnapshot): ArrayList<ListViewModel> {
        val localListItem = arrayListOf<ListViewModel>()
        val applyTime: String
        val playTime: String
        val playDate: String
        val title: String
        val currentNum: String = it.child("registrant").childrenCount.toString()
        val totalNum: String = it.child("totalNum").value.toString()
        val sports: String = it.child("sports").value.toString()
        val playTimeDate: String = it.child("playTime").value.toString()
        if (playTimeDate.split(" ").size == 2) {
            playDate = playTimeDate.split(" ")[0]
            playTime = playTimeDate.split(" ")[1].substring(0, 5)
        } else {
            playDate = "0"
            playTime = "0"
        }
        applyTime = it.child("applyTime").value.toString()
        val place: String = it.child("place").value.toString()
        val content: String = it.child("content").value.toString()
        val registrant: String = it.child("registrant").child("0").value.toString()
        val name: String = parent.child("user").child(registrant).child("name").value.toString()
        title = sports.plus(" | $playDate | $playTime | $place | ${currentNum}/${totalNum}")
        mapItem[playTimeDate] = ListViewModel(name, title, content, it.key.toString())
        mapItem = mapItem.toSortedMap(reverseOrder())
        for (value in mapItem.values)
            localListItem.add(0, value)
        return localListItem
    }

    fun getListView(listItem: ArrayList<ListViewModel>) {
        val listview = viewGroup!!.findViewById<ListView>(R.id.mymatchListView)
        val listviewAdapter = ListViewAdapter(listItem)
        listview.adapter = listviewAdapter
        listview.setOnItemClickListener { parent, view, position, id ->
            val clickedList = listItem[position]
            val intent =
                Intent(requireActivity().applicationContext, MyMatchDetailActivity::class.java)
            intent.putExtra("matchInfo", clickedList)
            intent.putExtra("matchId", clickedList.matchId)
            startActivity(intent)
        }
    }
}
