package com.c22027.nyoombang.ui.addevent

import android.app.DatePickerDialog
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.databinding.ActivityAddEventBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper

import com.c22027.nyoombang.utils.Utilization.uriToFile

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEventBinding
    private val addEventViewModel by viewModels<AddEventViewModel>()
    private var getFile: Uri? = null
    private lateinit var reference: DatabaseReference
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferencesHelper= SharedPreferencesHelper(this)
        reference = FirebaseDatabase.getInstance().getReference("Event")
        init()
    }

    private fun init(){

        with(binding){

            addEventViewModel.btnAdd.observe(this@AddEventActivity){
                setButton(it)
            }

            addEventViewModel.photo.observe(this@AddEventActivity){
                addEventViewModel.stateCheckBtnAdd()
            }

            addEventViewModel.date.observe(this@AddEventActivity){
                addEventViewModel.stateCheckBtnAdd()
            }

            imgPreviewImageView.setOnClickListener {
                startGallery()
            }
            btnAdd.setOnClickListener{
                addEvent()
            }

            edtName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    addEventViewModel.stateName(s.toString())
                    addEventViewModel.stateCheckBtnAdd()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            edtDescription.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    addEventViewModel.stateDescription(s.toString())
                    addEventViewModel.stateCheckBtnAdd()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            val myCalendar = Calendar.getInstance()
            val year = myCalendar.get(Calendar.YEAR)
            val month = myCalendar.get(Calendar.MONTH)
            val day= myCalendar.get(Calendar.DAY_OF_MONTH)
            edtDatePicker.isClickable = true

            edtDatePicker.setOnClickListener {
                val datePicker = DatePickerDialog(this@AddEventActivity, DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    val temp = "${day}/${month}/${year}"
                    edtDatePicker.setText(temp)
                    addEventViewModel.stateDate(temp)
                    Log.d("test1","$temp dan ${addEventViewModel.date.value.toString()}")
                },year, month, day).show()
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }


    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            getFile = selectedImg
            val myFile = uriToFile(selectedImg, this@AddEventActivity)
            binding.imgPreviewImageView.setImageURI(selectedImg)
            addEventViewModel.statePhoto(myFile)
            Log.d("test","$getFile dan ${addEventViewModel.photo.value.toString()}")
        }
    }

    private fun addEvent() {
        binding.apply {
            val userId = sharedPreferencesHelper.prefUid
            val eventName = edtName.text.toString()
            val descriptionEvent = edtDescription.text.toString()
            val endOfDate = addEventViewModel.date.value.toString()


            val uploadRef =
                FirebaseStorage.getInstance().getReference("/EventPicture/${getFile?.lastPathSegment}")
            uploadRef.putFile(getFile!!).addOnSuccessListener {
                uploadRef.downloadUrl.addOnSuccessListener {
                    val eventDataClass = EventDataClass(
                        event_id = reference.push().key.toString(),
                        userId,
                        eventName,
                        it.toString(),
                        descriptionEvent,
                        endOfDate,
                        "0"
                    )

                    reference.child(eventDataClass.event_id.toString()).setValue(eventDataClass)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@AddEventActivity,
                                "Berhasil di Menambahkan event",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }

            }.addOnFailureListener {
                Toast.makeText(this@AddEventActivity, it.message.toString(), Toast.LENGTH_SHORT)
                    .show()
                Log.d("failed", "failed${getFile?.path} ")
                Log.d("failed", it.message.toString())

            }

        }
    }

    private fun setButton(state : Boolean){
        with(binding){
            btnAdd.isEnabled = state
        }
    }
}