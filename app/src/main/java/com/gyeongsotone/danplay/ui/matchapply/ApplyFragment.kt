package com.gyeongsotone.danplay.ui.matchapply

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gyeongsotone.danplay.MainActivity
import com.gyeongsotone.danplay.R
import com.gyeongsotone.danplay.model.MatchDTO
import java.security.DigestException
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ApplyFragment : Fragment() {
    var viewGroup: ViewGroup? = null
    private var gSport: String? = "종목 선택"
    private var gPeople: Int? = null
    private var gPlace: String? = "장소 선택"
    private var gPlayTime: String? = "시간 선택"
    private var gContent: String? = null
    private var gMatchId: String? = null
    private var selectedItem: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.fragment_apply, container, false) as ViewGroup

        val btnEvent = viewGroup!!.findViewById<View>(R.id.btn_event) as Button
        val btnPeople = viewGroup!!.findViewById<View>(R.id.btn_people) as Button
        val btnPlace = viewGroup!!.findViewById<View>(R.id.btn_where) as Button
        val btnTime = viewGroup!!.findViewById<View>(R.id.btn_time) as Button
        val edittextContent = viewGroup!!.findViewById<View>(R.id.edittext_content) as EditText
        val sportsEmpty = viewGroup!!.findViewById<View>(R.id.apply_sports_empty) as TextView
        val playtimeEmpty = viewGroup!!.findViewById<View>(R.id.apply_playtime_empty) as TextView
        val totalNumEmpty = viewGroup!!.findViewById<View>(R.id.apply_totalNum_empty) as TextView
        val placeEmpty = viewGroup!!.findViewById<View>(R.id.apply_place_empty) as TextView
        val url = viewGroup!!.findViewById<View>(R.id.url) as TextView
        val applyMsg = viewGroup!!.findViewById<View>(R.id.apply_btn_msg) as TextView
        val btnApply = viewGroup!!.findViewById<View>(R.id.btn_apply) as Button

        url.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://webinfo.dankook.ac.kr/tiad/admi/faci/usem/views/findFacsUseApWeblList.do?_view=ok")
            )
            startActivity(intent)
        }
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        val currentUser = auth.currentUser!!
        btnEvent.text = (gSport)
        if (gPeople != null) {
            btnPeople.text = gPeople.toString()
        }
        btnPlace.text = gPlace
        btnTime.text = gPlayTime
        if (gContent != null)
            edittextContent.setText(gContent)
        btnEvent.setOnClickListener {
            val sports = arrayOf("테니스", "축구", "농구", "족구", "풋살")
            val builder = AlertDialog.Builder(activity)
                .setTitle("종목을 선택하세요")
                .setSingleChoiceItems(sports, -1) { dialog, which ->
                    selectedItem = sports[which]
                }
                .setPositiveButton("확인") { dialog, which ->
                    gSport = selectedItem.toString()
                    if (gSport == "null") {
                        gSport = "종목 선택"
                    }
                    btnEvent.text = gSport
                }
                .show()
        }

        btnPeople.setOnClickListener {
            val people = arrayOf<String>(
                "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"
            )
            var selectedItem: String? = null


            val builder = AlertDialog.Builder(activity)
                .setTitle("인원수를 선택하세요")
                .setSingleChoiceItems(people, -1) { dialog, which ->
                    selectedItem = people[which]
                }
                .setPositiveButton("확인") { dialog, which ->

                    if (selectedItem != null) {
                        gPeople = selectedItem?.toInt()
                    } else {
                        gPeople = null
                        selectedItem = "인원 선택"
                    }
                    btnPeople.text = selectedItem
                }
                .show()
        }

        btnPlace.setOnClickListener {
            val place = arrayOf<String>(
                "노천마당", "대운동장", "인문관소극장", "족구장", "테니스장A",
                "테니스장C", "평화의광장 농구장A", "평화의광장 농구장B", "평화의광장 농구장C", "폭포공원",
                "풋살경기장", "혜당관(학생극장 2층로비)", "혜당관(학생회관)212(학생극장)"
            )
            var selectedItem: String? = null

            val builder = AlertDialog.Builder(activity)
                .setTitle("장소를 선택하세요")
                .setSingleChoiceItems(place, -1) { dialog, which ->
                    selectedItem = place[which]
                }
                .setPositiveButton("확인") { dialog, which ->
                    gPlace = selectedItem.toString()
                    if (gPlace == "null") {
                        gPlace = "종목 선택"
                    }
                    btnPlace.text = gPlace
                }
                .show()
        }

        btnTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            var year = calendar.get(Calendar.YEAR)
            var month = calendar.get(Calendar.MONTH)
            var day = calendar.get(Calendar.DAY_OF_MONTH)
            var hour = calendar.get(Calendar.HOUR)
            var minute = calendar.get(Calendar.MINUTE)

            val listener1 = DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                year = i
                month = i2 + 1
                day = i3
                val listener2 = TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                    hour = i
                    minute = i2
                    gPlayTime = "%d-%02d-%02d %02d:%02d:00".format(year, month, day, i, i2)
                    btnTime.text = gPlayTime
                }
                val picker2 =
                    TimePickerDialog(activity, listener2, hour, minute, false) // true하면 24시간 제
                picker2.show()
            }
            val picker1 =
                activity?.let { it1 -> DatePickerDialog(it1, listener1, year, month, day) }
            picker1!!.show()
        }

        btnApply.setOnClickListener {
            val alertCancel = AlertDialog.Builder(activity)
            alertCancel.setMessage("정말로 매치를 등록하시겠습니까?\n* 방장은 매치를 취소할 수 없습니다.")
            // 확인버튼
            alertCancel.setPositiveButton("확인") { dialog, which ->
                database.get().addOnSuccessListener {
                    var wrongInput = 0
                    if (gSport == "종목 선택") {
                        wrongInput = 1
                        sportsEmpty.visibility = View.VISIBLE
                    } else {
                        sportsEmpty.visibility = View.INVISIBLE
                    }
                    if (gPeople == null) {
                        wrongInput = 1
                        totalNumEmpty.visibility = View.VISIBLE
                    } else {
                        totalNumEmpty.visibility = View.INVISIBLE
                    }
                    if (gPlayTime == "시간 선택") {
                        wrongInput = 1
                        playtimeEmpty.visibility = View.VISIBLE
                    } else {
                        playtimeEmpty.visibility = View.INVISIBLE
                    }
                    if (gPlace == "장소 선택") {
                        wrongInput = 1
                        placeEmpty.visibility = View.VISIBLE
                    } else {
                        placeEmpty.visibility = View.INVISIBLE
                    }
                    gContent = edittextContent.text.toString()
                    if (gContent == "") {
                        wrongInput = 1
                        applyMsg.text = "내용을 입력해주세요"
                        applyMsg.visibility = View.VISIBLE
                    } else {
                        applyMsg.visibility = View.INVISIBLE
                    }
                    if (wrongInput == 0) {
                        checkSameMatch(currentUser)
                    }
                }.addOnFailureListener {
                    Toast.makeText(activity, "DB 읽기 실패", Toast.LENGTH_LONG).show()
                }
            }
            // 취소버튼
            alertCancel.setNegativeButton("취소", null)
            val alert = alertCancel.create()
            alert.setIcon(R.drawable.logo_danplay)
            alert.setTitle("매치 등록")
            alert.show()
        }
        return viewGroup
    }

    private val digits = "0123456789ABCDEF"
    private fun bytesToHex(byteArray: ByteArray): String {
        val hexChars = CharArray(byteArray.size * 2)
        for (i in byteArray.indices) {
            val v = byteArray[i].toInt() and 0xff
            hexChars[i * 2] = digits[v shr 4]
            hexChars[i * 2 + 1] = digits[v and 0xf]
        }
        return String(hexChars)
    }

    private fun hashSHA256(msg: String): String {
        val hash: ByteArray
        try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(msg.toByteArray())
            hash = md.digest()
        } catch (e: CloneNotSupportedException) {
            throw DigestException("couldn't make digest of partial content");
        }
        return bytesToHex(hash)
    }

    private fun writeNewUser(user: FirebaseUser?, matchId: String) {
        if (user != null) {
            database.child("user").child(user.uid).child("matchId").get().addOnSuccessListener {
                var tempMatchId = ArrayList<String>()
                tempMatchId = it.value as ArrayList<String>
                if (tempMatchId[0].equals("-1")) {
                    tempMatchId.clear()
                    tempMatchId.add(matchId)
                } else {
                    tempMatchId.add(matchId)
                }
                database.child("user").child(user!!.uid).child("matchId").setValue(null)
                database.child("user").child(user!!.uid).child("matchId").setValue(tempMatchId)
                tempMatchId.clear()
            }.addOnFailureListener {
                Toast.makeText(activity, "DB 읽기 실패", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setMatchData(user: FirebaseUser?) {
        val matchDTO = MatchDTO()
        val currentTime = System.currentTimeMillis()
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        val dataFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = dataFormat.format(currentTime)
        val time = timeFormat.format(currentTime)

        if (user != null) {
            matchDTO.sports = gSport
            matchDTO.totalNum = gPeople
            matchDTO.place = gPlace
            matchDTO.content = gContent

            val tempUserId = ArrayList<String>()
            tempUserId.add(user.uid)
            matchDTO.registrant = tempUserId
            matchDTO.playTime = gPlayTime
            matchDTO.applyTime = "$date $time"
            gMatchId = hashSHA256(gSport.plus(gPlayTime.plus(gPlace)))
            database.child("match").child(gMatchId.toString()).setValue(matchDTO)
            writeNewUser(user, gMatchId!!)
            Toast.makeText(activity, "매치가 등록되었습니다!", Toast.LENGTH_SHORT).show()
            moveMainPage()
        }
    }

    private fun moveMainPage() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun checkSameMatch(user: FirebaseUser?) {
        if (user != null) {
            database.get().addOnSuccessListener { it ->
                var sign = false
                val applyMsg = viewGroup!!.findViewById<View>(R.id.apply_btn_msg) as TextView
                for (match in it.child("match").children) {
                    val sports = match.child("sports").value.toString()
                    val playTime = match.child("playTime").value.toString()
                    val place = match.child("place").value.toString()
                    if (gSport.equals(sports) and gPlayTime.equals(playTime) and gPlace.equals(place)) {
                        sign = true
                        break
                    }
                }
                if (sign) {
                    applyMsg.text = "동일한 조건의 매치가 존재합니다. 조회를 통해 예약해주세요!"
                    applyMsg.visibility = View.VISIBLE
                } else {
                    var matchId = ArrayList<String>()
                    matchId =
                        it.child("user").child(user.uid).child("matchId").value as ArrayList<String>
                    for (current_matchId in matchId) {
                        if (current_matchId == "-1") {
                            break
                        } else if (it.child("match").child(current_matchId)
                                .child("playTime").value.toString() == gPlayTime
                        ) {
                            sign = true
                            break
                        }
                    }
                    if (sign) {
                        applyMsg.text = "동일한 시간에 예약이 존재합니다. 예약을 확인해주세요!"
                        applyMsg.visibility = View.VISIBLE
                    } else {
                        setMatchData(user)
                        applyMsg.visibility = View.INVISIBLE
                    }
                }
            }.addOnFailureListener {}
        }
    }
}
