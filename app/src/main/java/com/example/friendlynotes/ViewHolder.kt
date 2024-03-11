package com.example.friendlynotes

import android.view.View
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(view: View, listener: OnItemClickListener): RecyclerView.ViewHolder(view)
{
    val firstname: TextView
    val lastname: TextView
    val profilePicture: ImageView

    init {
        view.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }

        firstname = view.findViewById(R.id.firstname)
        lastname = view.findViewById(R.id.lastname)
        profilePicture = view.findViewById(R.id.image_view_photo_profile)
    }

}