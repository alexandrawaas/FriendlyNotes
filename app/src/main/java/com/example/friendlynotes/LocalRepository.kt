package com.example.friendlynotes

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

class LocalRepository(context: Context):Repository
{
    private val gson = Gson()
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("friendStorage", AppCompatActivity.MODE_PRIVATE)
    private val sharedPreferencesEditor: SharedPreferences.Editor =sharedPreferences.edit()

    override fun getList(): ArrayList<Friend> {
        val stringifiedList: String = sharedPreferences.getString("friendsList", null) ?: gson.toJson(emptyArray<Any>())
        val array: Array<Friend> = gson.fromJson(stringifiedList, Array<Friend>::class.java)

        return ArrayList<Friend>(Arrays.asList(*array))
    }

    override fun sort() {
        val list:ArrayList<Friend> = getList()
        list.sortBy { friend -> friend.firstname }
        sharedPreferencesEditor.putString("friendsList", gson.toJson(list.toArray()))
        sharedPreferencesEditor.commit()
    }

    override fun addFriend(newValue: Friend) {
        val list:ArrayList<Friend> = getList()
        list.add(newValue)
        sharedPreferencesEditor.putString("friendsList", gson.toJson(list.toArray()))
        sharedPreferencesEditor.commit()
    }

    override fun deleteFriend(ID: UUID) {
        val list = getList()
        println(list)
        val friend = list.find { friend -> friend.ID == ID }
        println("Removed" + friend +"? " +list.remove(friend))
        sharedPreferencesEditor.putString("friendsList", gson.toJson(list.toArray()))
        sharedPreferencesEditor.commit()
    }

    override fun getFriend(position: Int): Friend = getList().get(position)

    override fun getSize() : Int = getList().size
}