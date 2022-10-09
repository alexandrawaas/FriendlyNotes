package com.example.friendlynotes

import java.util.*
import kotlin.collections.ArrayList

interface Repository
{
    fun addFriend(newValue:Friend)
    fun getFriend(position:Int):Friend
    fun deleteFriend(ID: UUID)
    fun getSize():Int
    fun sort()
    fun getList(): ArrayList<Friend>
}