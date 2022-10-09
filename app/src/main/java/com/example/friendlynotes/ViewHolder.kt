package com.example.friendlynotes

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(view: View, listener: OnItemClickListener): RecyclerView.ViewHolder(view)
{
    val firstname: TextView
    val lastname: TextView

    init {
        view.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
        firstname = view.findViewById(R.id.firstname)
        lastname = view.findViewById(R.id.lastname)
    }

}