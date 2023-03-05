package com.example.friendlynotes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView

class MainAdapter(val repository:Repository): RecyclerView.Adapter<ViewHolder>() {
    var list: ArrayList<Friend> = repository.getList()
    var listFiltered: ArrayList<Friend> = list

    private lateinit var clickListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_card, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.firstname.text = listFiltered.get(position).firstname
        holder.lastname.text = listFiltered.get(position).lastname
        val img = listFiltered.get(position).photo?.decodeBase64Image()
        println(img.toString()+" hello")
        if (img != null) holder.profilePicture.setImageBitmap(img)
    }

    override fun getItemCount() = listFiltered.size


    public fun updateRecyclerView()
    {
        list = repository.getList()
        listFiltered = list
        repository.sort()
        notifyDataSetChanged()
    }

    public fun filter(constraint: CharSequence?)
    {
        listFiltered = if(constraint==null) list
        else {
            val charString = constraint.toString().lowercase().trim()
            val filteredListFilled = ArrayList<Friend>()
            list.filter {
                (it.firstname.lowercase().contains(charString)) or
                        (it.lastname != null && it.lastname!!.lowercase().contains(charString))
            }
                .forEach {
                    filteredListFilled.add(it)
                    println(filteredListFilled)
                }
            filteredListFilled
        }

        notifyDataSetChanged()
    }
}

