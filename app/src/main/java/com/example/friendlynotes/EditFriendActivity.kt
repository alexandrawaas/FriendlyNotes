package com.example.friendlynotes

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.friendlynotes.databinding.ActivityEditFriendBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import java.lang.NumberFormatException

class EditFriendActivity : AppCompatActivity() {

    private var getContentLauncherEdit = registerForActivityResult(PickPhotoContract())
    { result: String? ->
        if (result != null) {
            println("Hello Image!")
            
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setResult(RESULT_CANCELED)

        val binding = ActivityEditFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var friendString = intent!!.getStringExtra("friend")
        val gson: Gson = Gson()
        var friend: Friend? = gson.fromJson(friendString, Friend::class.java)

        if (friend != null) {
            binding.textfieldAddFirstname.editText?.setText(friend.firstname)
            binding.textfieldAddLastname.editText?.setText(friend.lastname.nullToString())
            if (friend.birthday != null) {
                binding.textfieldAddDay.editText?.setText(friend.birthday?.day?.toString())
                binding.textfieldAddMonth.editText?.setText(friend.birthday?.month?.toString())
                binding.textfieldAddYear.editText?.setText(friend.birthday?.year?.toString())
            }
            binding.textfieldAddAddress.editText?.setText(friend.address.nullToString())
            binding.textfieldAddPhoneNumber.editText?.setText(friend.phoneNumber.nullToString())
            binding.textfieldAddOccupation.editText?.setText(friend.occupation.nullToString())
            binding.textfieldAddHobbies.editText?.setText(friend.hobbies.nullToString())
            binding.textfieldAddLikes.editText?.setText(friend.likes.nullToString())
            binding.textfieldAddDislikes.editText?.setText(friend.dislikes.nullToString())
            binding.textfieldAddNotes.editText?.setText(friend.notes.nullToString())
        } else friend = Friend("", "")

        val dayPicker: MutableList<String> = (1..31).toList().map { it.toString() }.toMutableList()
        dayPicker.add(0, "-")
        val dayAdapter = ArrayAdapter(this, R.layout.list_item, dayPicker)
        (binding.textfieldAddDay.editText as? AutoCompleteTextView)?.setAdapter(dayAdapter)

        val monthPicker: MutableList<String> =
            (1..12).toList().map { it.toString() }.toMutableList()
        monthPicker.add(0, "-")
        val monthAdapter = ArrayAdapter(this, R.layout.list_item, monthPicker)
        (binding.textfieldAddMonth.editText as? AutoCompleteTextView)?.setAdapter(monthAdapter)


        binding.buttonAddPhoto.setOnClickListener {
            getContentLauncherEdit.launch(null)
        }


        val dialogFirstname = MaterialAlertDialogBuilder(this)
            .setTitle("First name is missing")
            .setMessage("Please insert your friend's first name.")

            .setPositiveButton("OK") { dialog, which ->
                //binding.textfieldAddFirstname.boxStrokeColor= getColor(R.color.purple_500)
            }

        binding.buttonSave.setOnClickListener {

            if (binding.textfieldAddFirstname.editText?.text.toString() == "") {
                dialogFirstname.show()
            } else {
                val intent = Intent()

                friend.firstname = binding.textfieldAddFirstname.editText?.text.toString().trim()
                friend.lastname = binding.textfieldAddLastname.editText?.text.toString().trim()

                try {
                    friend.birthday = Birthday(
                        binding.textfieldAddDay.editText?.text.toString().toInt(),
                        binding.textfieldAddMonth.editText?.text.toString().toInt(),
                        binding.textfieldAddYear.editText?.text.toString().toIntOrNull()
                    )
                } catch (e: NumberFormatException) {
                    friend.birthday = null
                }

                friend.address =
                    binding.textfieldAddAddress.editText?.text.toString().trim().stringToNull()
                friend.phoneNumber =
                    binding.textfieldAddPhoneNumber.editText?.text.toString().trim().stringToNull()
                friend.occupation =
                    binding.textfieldAddOccupation.editText?.text.toString().trim().stringToNull()
                friend.hobbies =
                    binding.textfieldAddHobbies.editText?.text.toString().trim().stringToNull()
                friend.likes =
                    binding.textfieldAddLikes.editText?.text.toString().trim().stringToNull()
                friend.dislikes =
                    binding.textfieldAddDislikes.editText?.text.toString().trim().stringToNull()
                friend.notes =
                    binding.textfieldAddNotes.editText?.text.toString().trim().stringToNull()

                friendString = gson.toJson(friend)
                intent.putExtra("friend", friendString)

                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onBackPressed() {

        val dialogCancel = MaterialAlertDialogBuilder(this)
            .setTitle("Cancel")
            .setMessage("Do you really want to return? Your edits will not be saved.")

            .setPositiveButton("Discard") { dialog, which ->
                super.onBackPressed()
            }

            .setNegativeButton("Stay") { dialog, which ->
            }

        dialogCancel.show()
    }
}

    /*
    fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val thumbnail: Bitmap = data.getParcelableExtra("data")
            val fullPhotoUri: Uri = data.data
            // Do work with photo saved at fullPhotoUri
            ...
        }
    }
    */