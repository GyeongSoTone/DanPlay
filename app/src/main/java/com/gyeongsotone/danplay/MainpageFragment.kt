package com.gyeongsotone.danplay

import android.view.ViewGroup
import android.view.LayoutInflater
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class MainpageFragment : Fragment() {
    var viewGroup: ViewGroup? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewGroup = inflater.inflate(R.layout.fragment_mainpage, container, false) as ViewGroup
        return viewGroup
    }
}