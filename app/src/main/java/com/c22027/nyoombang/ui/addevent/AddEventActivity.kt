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
import com.c22027.nyoombang.databinding.ActivityAddEventBinding
import com.c22027.nyoombang.utils.uriToFile
import java.io.File
import java.util.*

class AddEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEventBinding
    private val addEventViewModel by viewModels<AddEventViewModel>()
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            val myFile = uriToFile(selectedImg, this@AddEventActivity)

            getFile = myFile

            binding.imgPreviewImageView.setImageURI(selectedImg)
            addEventViewModel.statePhoto(getFile)
            Log.d("test","$getFile dan ${addEventViewModel.photo.value.toString()}")
        }
    }

    private fun setButton(state : Boolean){
        with(binding){
            btnAdd.isEnabled = state
        }
    }
}