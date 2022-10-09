package com.example.friendlynotes

data class Birthday(var day: Int, var month: Int, var year: Int? = null)
{
    override fun toString(): String {
            val zeroDay:String = if(day!! <10) "0" else ""
            val zeroMonth:String = if(month!!<10) "0" else ""
            val yearString: String = if (year!=null) "$year" else ""
            return "$zeroDay$day.$zeroMonth$month.$yearString"
    }
}