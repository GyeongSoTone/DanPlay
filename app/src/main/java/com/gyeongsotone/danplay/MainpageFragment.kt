package com.gyeongsotone.danplay

import android.content.Intent
import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class MainpageFragment : Fragment() {
    var viewGroup: ViewGroup? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.fragment_mainpage, container, false) as ViewGroup
        var btn_sports1 = viewGroup!!.findViewById<View>(R.id.button_sports1) as ImageButton
        btn_sports1.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext, SignupActivity::class.java)
            startActivity(intent)
        }

        var btn_sports2 = viewGroup!!.findViewById<View>(R.id.button_sports2) as ImageButton
        btn_sports2.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

        return viewGroup
    }
}