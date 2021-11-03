package com.gyeongsotone.danplay

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import android.service.autofill.FieldClassification
import android.view.View
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import com.google.android.gms.common.util.Base64Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gyeongsotone.danplay.databinding.ActivitySignupBinding
import com.gyeongsotone.danplay.databinding.FragmentApplyBinding
import com.gyeongsotone.danplay.model.MatchDTO
import java.security.DigestException
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDate.*
import java.util.*


class ApplyFragment : Fragment() {
    var viewGroup: ViewGroup? = null

    private var g_sport: String? = null
    private var g_people: Int? = null
    private var g_place: String? = null
    private var g_time: String? = null
    private var g_content: String? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

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
        var edittext_content = viewGroup!!.findViewById<View>(R.id.edittext_content) as EditText

        var btn_apply = viewGroup!!.findViewById<View>(R.id.btn_apply) as Button

        val list = arrayOf<String>()

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        var current_user = auth.currentUser!!

        btn_event.setOnClickListener {
            //Toast.makeText(getActivity(), "~", Toast.LENGTH_LONG).show()
            val sports = arrayOf("테니스", "축구", "농구", "족구", "풋살")
            var selectedItem: String? = null

            val builder = AlertDialog.Builder(activity)
                .setTitle("종목을 선택하세요")
                .setSingleChoiceItems(sports, -1) { dialog, which ->
                    selectedItem = sports[which]
                }
                .setPositiveButton("확인") { dialog, which ->
                    g_sport = selectedItem.toString()
                    //Toast.makeText(getActivity(),g_sport, Toast.LENGTH_SHORT).show()
                    btn_event.text = g_sport
                }
                .show()
        }

        btn_people.setOnClickListener {
            //val people = arrayOf("테니스", "축구", "농구", "족구", "풋살")
            val people = arrayOf<String>("2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22")
            var selectedItem: String? = null

            val builder = AlertDialog.Builder(activity)
                .setTitle("인원수를 선택하세요")
                .setSingleChoiceItems(people, -1) { dialog, which ->
                    selectedItem = people[which]
                }
                .setPositiveButton("확인") { dialog, which ->
                    btn_people.text = selectedItem
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

            val builder = AlertDialog.Builder(activity)
                .setTitle("장소를 선택하세요")
                .setSingleChoiceItems(place, -1) { dialog, which ->
                    selectedItem = place[which]
                }
                .setPositiveButton("확인") { dialog, which ->
                    g_place = selectedItem.toString()
                    //Toast.makeText(getActivity(),g_sport, Toast.LENGTH_SHORT).show()
                    btn_place.text = g_place
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

                var listener2 = TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                    hour = i
                    minute = i2

                    g_time = "%d-%02d-%02d %02d:%02d:00".format(year, month, day, i, i2)
                    btn_time.text = g_time
                }
                var picker2 = TimePickerDialog(activity, listener2, hour, minute, false ) // true하면 24시간 제
                picker2.show()
            }
            var picker1 = activity?.let { it1 -> DatePickerDialog(it1, listener1, year, month, day) }
            picker1!!.show()
        }

        btn_apply.setOnClickListener {
            //Toast.makeText(activity,currentTime.toString(), Toast.LENGTH_SHORT).show()

            g_content = edittext_content.getText().toString()
            setMatchData(current_user)
        }


        return viewGroup
    }

    private val digits = "0123456789ABCDEF"

    fun bytesToHex(byteArray: ByteArray): String {
        val hexChars = CharArray(byteArray.size * 2)
        for (i in byteArray.indices) {
            val v = byteArray[i].toInt() and 0xff
            hexChars[i * 2] = digits[v shr 4]
            hexChars[i * 2 + 1] = digits[v and 0xf]
        }
        return String(hexChars)
    }

    fun hashSHA256(msg: String): String {
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

    fun writeNewUser(user: FirebaseUser?, matchId: String) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (user != null) {
                    for(snapshot in dataSnapshot.child("user").child(user.uid).child("matchId").children){

                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(getActivity(), "DB 읽기 실패", Toast.LENGTH_LONG).show()
            }
        })

        database.child("user").child(userId).child("matchId").setValue(matchId)
    }

    private fun setMatchData(user: FirebaseUser?) {
        var MatchDTO = MatchDTO()
        val currentTime = System.currentTimeMillis()
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        val dataFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = dataFormat.format(currentTime)
        val time = timeFormat.format(currentTime)

        if (user != null) {
            MatchDTO.sports = g_sport
            MatchDTO.totalNum = g_people
            MatchDTO.place = g_place
            MatchDTO.content = g_content

            MatchDTO.registrant?.add(user.uid.toString())

            MatchDTO.playTime = g_time
            MatchDTO.applyTime = date.toString() + ' ' + time.toString()
            val temp = hashSHA256(g_sport.plus(g_time.plus(g_place)))

            Toast.makeText(activity,temp, Toast.LENGTH_SHORT).show()

            database.child("match").child(temp).setValue(MatchDTO)

            moveMainPage(user, temp)
        }
    }

    private fun moveMainPage(user: FirebaseUser?, matchId: String) {
        if (user != null) {
            Toast.makeText(activity, "매치 등록 완료되었습니다.", Toast.LENGTH_SHORT).show()
            writeNewUser(user.uid, matchId)
            // 다음 페이지로 넘어가는 Intent
        }
    }
}
