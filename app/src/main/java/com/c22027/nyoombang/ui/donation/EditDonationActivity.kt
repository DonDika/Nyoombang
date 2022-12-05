package com.c22027.nyoombang.ui.donation


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.c22027.nyoombang.databinding.ActivityEditDonationBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

import java.text.SimpleDateFormat
import java.util.*

class EditDonationActivity : AppCompatActivity() {
    private lateinit var reference: DatabaseReference
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var binding: ActivityEditDonationBinding
    private var currentDate: String? = null
    private var filePath: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        reference = FirebaseDatabase.getInstance().reference
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        val sdf = SimpleDateFormat("ddMyyyyhh:mm:ss")
        currentDate = sdf.format(Date())
        binding.circleImageView.setOnClickListener {
            startGallery()
        }
        displayData()
        binding.edit.setOnClickListener {
            editAndUploadData()
        }

    }

    private fun displayData(){

        val user = sharedPreferencesHelper.prefUid.toString()
        reference.child("UsersProfile").child(user).get().addOnCompleteListener {

            if(it.isSuccessful){
                val snapshot = it.result
                val name = snapshot.child("name").value

                binding.edtName.setText(name.toString())



            }
        }
    }


    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            val selectedImage: Uri = it.data?.data as Uri
            filePath = selectedImage
            binding.circleImageView.setImageURI(selectedImage)
        }
    }

    private fun startGallery(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a picture")
        launcherIntentGallery.launch(chooser)
    }





    private fun editAndUploadData(){
        val user = sharedPreferencesHelper.prefUid.toString()
        val name = binding.edtName.text.toString()
            val date = currentDate.toString()
            val uploadRef = FirebaseStorage.getInstance().getReference("/UserPhotoPicture/$user+_+$date")
            uploadRef.putFile(filePath!!).addOnSuccessListener {
                uploadRef.downloadUrl.addOnSuccessListener {
                    val updateUser = mapOf<String,String>(
                        "name" to name,
                        "picture" to it.toString()
                    )

                    reference.child("UsersProfile").child(user).updateChildren(updateUser).addOnSuccessListener {
                        Toast.makeText(this@EditDonationActivity,"Berhasil di update",Toast.LENGTH_SHORT).show()
                    }
                }

            }.addOnFailureListener{
                Toast.makeText(this@EditDonationActivity, it.message.toString() ,Toast.LENGTH_SHORT).show()
                Log.d("failed","failed${filePath?.path} ")
                Log.d("failed",it.message.toString())

            }
        }












}