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
import android.view.View
import android.widget.Toast
import com.c22027.nyoombang.data.model.EventDataClass
import com.c22027.nyoombang.databinding.ActivityAddEventBinding
import com.c22027.nyoombang.data.local.SharedPreferencesHelper
import com.c22027.nyoombang.ui.dashboard.DashboardCommunity

import com.c22027.nyoombang.utils.Utilization.uriToFile

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddEventActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAddEventBinding.inflate(layoutInflater) }
    private val fireStore by lazy { Firebase.firestore}
    private val sharedPreferences by lazy { SharedPreferencesHelper(this) }

    private val addEventViewModel by viewModels<AddEventViewModel>()
    private var getFile: Uri? = null


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }
    private fun generateKeywords(name: String): List<String> {
        val keywords = mutableListOf<String>()
        for (i in name.indices) {
            for (j in (i+1)..name.length) {
                keywords.add(name.slice(i until j))
            }
        }
        return keywords
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


    private fun addEvent() {
        binding.apply {
            val userId = sharedPreferences.prefUid
            val eventName = edtName.text.toString()
            val descriptionEvent = edtDescription.text.toString()
            val endOfDate = addEventViewModel.date.value.toString()
            val key = fireStore.collection("Event").document().id

            val uploadRef = FirebaseStorage.getInstance().getReference("/EventPicture/${getFile?.lastPathSegment}")
            uploadRef.putFile(getFile!!).addOnSuccessListener {
                uploadRef.downloadUrl.addOnSuccessListener {
                    val eventDataClass = EventDataClass(
                        eventId = key,
                        userId = userId!!,
                        userName = sharedPreferences.prefUsername.toString(),
                        eventName = eventName,
                        eventPicture = it.toString(),
                        eventDescription = descriptionEvent,
                        endOfDate = endOfDate,
                        totalAmount = 0,
                        keyword = generateKeywords(eventName)
                    )
                    fireStore.collection("Event")
                        .document(key)
                        .set(eventDataClass)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                showLoading(true)
                                startActivity(Intent(this@AddEventActivity,DashboardCommunity::class.java))
                                    finish()
                                Toast.makeText(
                                    this@AddEventActivity,
                                    "Event Berhasil Ditambahkan",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                            /*.add(eventDataClass)
                            .addOnSuccessListener {
                                Toast.makeText(this@AddEventActivity, "Campaign berhasil ditambahkan! ", Toast.LENGTH_SHORT).show()
                                //finish()
                            }*/

                    }
            }.addOnFailureListener {
                Toast.makeText(this@AddEventActivity, it.message.toString(), Toast.LENGTH_SHORT).show()
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
    private fun showLoading(isLoading: Boolean) {
        binding.pgLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}