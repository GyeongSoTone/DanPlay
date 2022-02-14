package com.gyeongsotone.danplay.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.gyeongsotone.danplay.model.ListViewModel
import com.gyeongsotone.danplay.R

class ListViewAdapter(private val List: MutableList<ListViewModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return List.size
    }

    override fun getItem(position: Int): Any {
        return List[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView1 = convertView
        if (convertView1 == null) {
            convertView1 =
                LayoutInflater.from(parent?.context).inflate(R.layout.listview_item, parent, false)
        }
        val name = convertView1!!.findViewById<TextView>(R.id.listviewItemName)
        val title = convertView1.findViewById<TextView>(R.id.listviewItemTitle)
        val content = convertView1.findViewById<TextView>(R.id.listviewItemContent)
        name.text = List[position].name
        title.text = List[position].title
        content.text = List[position].content
        return convertView1
    }
}
