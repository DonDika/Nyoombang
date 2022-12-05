package com.c22027.nyoombang.ui.addevent

import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class AddEventViewModel : ViewModel() {

    private val _btnAdd = MutableLiveData<Boolean>()
    val btnAdd: LiveData<Boolean> = _btnAdd

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _photo = MutableLiveData<File?>()
    val photo: LiveData<File?> = _photo

    init {
        _btnAdd.value = false
        _description.value = ""
        _name.value = ""
        _date.value = ""
        _photo.value = null
    }

    fun stateDescription(value : String){
        _description.value = value
    }

    fun statePhoto(value: File){
        _photo.value = value
    }

    fun stateName(value : String){
        _name.value = value
    }

    fun stateDate(value : String){
        _date.value = value
    }


    fun stateCheckBtnAdd(){
        _btnAdd.value = (_name.value.toString().isNotBlank() && _description.value.toString().isNotBlank() && _date.value.toString().isNotBlank() && _photo.value?.equals(null) != true)
    }
}