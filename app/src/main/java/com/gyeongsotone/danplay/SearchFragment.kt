package com.gyeongsotone.danplay

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment

class SearchFragment : Fragment() {
    var viewGroup: ViewGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.fragment_search, container, false) as ViewGroup
        var btn_filter = viewGroup!!.findViewById<View>(R.id.btn_filter) as ImageView
        btn_filter.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext, FilterActivity::class.java)
            startActivity(intent)
        }
        return viewGroup
    }

}