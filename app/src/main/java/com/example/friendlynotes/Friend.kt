package com.example.friendlynotes

import java.util.*

data class Friend (var firstname: String,
                   var lastname: String? = null,
                   var birthday: Birthday? = null,
                   var address: String? = null,
                   var phoneNumber: String? = null,
                   var occupation: String? = null,
                   var hobbies: String?=null,
                   var likes: String? = null,
                   var dislikes: String? = null,
                   var notes: String? = null,
                   var photo: String? = null)
{
    val ID:UUID = UUID.randomUUID()

    override fun toString(): String {
        return "$firstname $lastname $ID"
    }
}
