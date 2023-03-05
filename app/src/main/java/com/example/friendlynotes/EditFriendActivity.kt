package com.example.friendlynotes

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.friendlynotes.databinding.ActivityEditFriendBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import kotlin.properties.Delegates


class EditFriendActivity : AppCompatActivity() {

    private lateinit var binding:ActivityEditFriendBinding
    private var currentBitmap:Bitmap? by Delegates.observable(null) {
        property, old, new ->
        binding.imageViewPhotoProfile.setImageBitmap(new)
    }

    private var getContentLauncherEdit = registerForActivityResult(PickPhotoContract())
    { result: Uri? ->
        if (result != null) {
            val bitmap: Bitmap? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(this@EditFriendActivity.contentResolver, result))
            }
            else
            {
                MediaStore.Images.Media.getBitmap(this@EditFriendActivity.contentResolver, result)
            }

            if(bitmap==null)
            {
                //TODO: Fehlermeldung
                println("FEHLER")
                return@registerForActivityResult;
            }

            currentBitmap = bitmap.encodeBase64()?.decodeBase64Image()!!
        }
        else
        {
            println("FEHLER") //TODO: Fehlermeldung
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setResult(RESULT_CANCELED)

        binding = ActivityEditFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var friendString = intent!!.getStringExtra("friend")
        val gson: Gson = Gson()
        var friend: Friend? = gson.fromJson(friendString, Friend::class.java)

        if (friend != null) {
            currentBitmap=friend.photo?.decodeBase64Image()
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


        binding.cardViewForImage.setOnClickListener {
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

                friend.photo=currentBitmap?.encodeBase64()
                if (currentBitmap==null) currentBitmap = ResourcesCompat.getDrawable(resources, R.drawable.user, null)?.toBitmap()
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