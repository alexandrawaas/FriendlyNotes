package com.example.friendlynotes

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.friendlynotes.databinding.ActivityShowFriendBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import java.util.*

class ShowFriendActivity : AppCompatActivity() {

    @SuppressLint("SuspiciousIndentation")
    private var getContentLauncherEdit=registerForActivityResult(EditFriendContract())
    { result ->
        if(result!=null)
        {
            val repository=LocalRepository(this.baseContext)
            repository.deleteFriend(result.ID)
            repository.addFriend(result)

            val gson = Gson()
            val intentRefresh: Intent = Intent(this@ShowFriendActivity, ShowFriendActivity::class.java)
                intentRefresh.putExtra("friend", gson.toJson(result))
                finish()
                startActivity(intentRefresh)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityShowFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gson = Gson()
        val friendString: String? = intent!!.getStringExtra("friend")
        val friend: Friend = gson.fromJson(friendString, Friend::class.java)

        val bitmap: Bitmap? = friend.photo?.decodeBase64Image()
        if (bitmap != null) binding.imageViewPhotoProfile.setImageBitmap(bitmap)

        binding.textFirstname.text = friend.firstname
        binding.textLastname.text = friend.lastname
        binding.labelBirthday.hideIfNull(friend.birthday?.toString(), this, findViewById(R.id.linearLayoutBirthday))
        binding.labelAddress.hideIfNull(friend.address, this, findViewById(R.id.linearLayoutAddress))
        binding.labelPhoneNumber.hideIfNull(friend.phoneNumber, this, findViewById(R.id.linearLayoutPhoneNumber))
        binding.labelOccupation.hideIfNull(friend.occupation, this)
        binding.labelHobbies.hideIfNull(friend.hobbies, this)
        binding.labelLikes.hideIfNull(friend.likes, this)
        binding.labelDislikes.hideIfNull(friend.dislikes, this)
        binding.labelNotes.hideIfNull(friend.notes, this)
        binding.imageViewPhotoProfile.drawable

        binding.buttonEdit.setOnClickListener {
            getContentLauncherEdit.launch(friend)
        }


        val dialogDelete = MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.dialogue_title_delete))
            .setMessage(resources.getString(R.string.dialogue_text_delete))

            .setNegativeButton(resources.getString(R.string.dialogue_button_cancel)) { dialog, which ->

            }
            .setPositiveButton(resources.getString(R.string.dialogue_button_delete)) { dialog, which ->
                val repository=LocalRepository(this.baseContext)
                repository.deleteFriend(friend.ID)
                finish()
            }


        binding.buttonDelete.setOnClickListener {
            dialogDelete.show()
        }

        binding.imageViewCall.setOnClickListener {
            val implicitIntentPhoneNumber: Intent = Intent(ACTION_DIAL, Uri.parse("tel:"+binding.textPhoneNumber.text.toString()))
            startActivity(implicitIntentPhoneNumber)
        }

        binding.imageViewLocation.setOnClickListener {
            val implicitIntentAddress = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+binding.textAddress.text.toString()))
            startActivity(implicitIntentAddress)
        }

        if(friend.birthday == null) binding.imageViewCalendar.visibility = View.GONE
        if(friend.address=="") binding.imageViewLocation.visibility = View.GONE
        if(friend.phoneNumber=="") binding.imageViewCall.visibility = View.GONE

        binding.imageViewCalendar.setOnClickListener {


            if(friend.birthday!=null)
            {
                var year: Int = Calendar.getInstance().get(Calendar.YEAR)
                if(Calendar.getInstance().get(Calendar.MONTH) > friend.birthday!!.month ||
                    Calendar.getInstance().get(Calendar.MONTH)==friend.birthday!!.month &&
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH) > friend.birthday!!.day) year++

                val implicitIntentBirthday = Intent(Intent.ACTION_INSERT).apply {
                    data = CalendarContract.Events.CONTENT_URI
                    putExtra(CalendarContract.Events.TITLE, binding.textFirstname.text.toString()+" "+binding.textLastname.text.toString()+"'s Birthday")
                    putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                    putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY")
                    putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, Calendar.getInstance().apply{ set(year, friend.birthday!!.month-1, friend.birthday!!.day)}.timeInMillis)
                }

                startActivity(implicitIntentBirthday)
            }
        }
    }
}