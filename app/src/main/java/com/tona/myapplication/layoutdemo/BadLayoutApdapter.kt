package com.tona.myapplication.layoutdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tona.myapplication.R

val demoData = List(200) { "Item #$it" }

class BadLayoutAdapter :
    RecyclerView.Adapter<BadLayoutAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bad_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = demoData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = demoData[position]

        // Ép layout lại (KHÔNG nên làm trong app thật)
        holder.itemView.requestLayout()

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txtName)
    }
}