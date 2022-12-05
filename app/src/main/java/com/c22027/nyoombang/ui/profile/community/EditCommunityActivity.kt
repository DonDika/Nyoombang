package com.c22027.nyoombang.ui.profile.community

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.c22027.nyoombang.R
import com.c22027.nyoombang.databinding.ActivityEditCommunityBinding
import com.c22027.nyoombang.databinding.ActivityEditUserBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper
import com.c22027.nyoombang.ui.profile.user.UserProfileActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_donation.*
import java.text.SimpleDateFormat
import java.util.*

class EditCommunityActivity : AppCompatActivity() {
    private lateinit var reference: DatabaseReference
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var binding: ActivityEditCommunityBinding
    private var currentDate: String? = null
    private var filePath: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        reference = FirebaseDatabase.getInstance().reference
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        val sdf = SimpleDateFormat("ddMyyyyhh:mm:ss")
        currentDate = sdf.format(Date())
        binding.civProfile.setOnClickListener {
            startGallery()
        }
        displayData()
        binding.btnSave.setOnClickListener {
            editAndUploadData()
        }
        binding.apply {
            if (edtDescription.text.equals("null") && edtFb.text.equals("null") && edtIg.text.equals("null") && edtPhoneNumber.text.equals("null") && edtTwitter.text.equals("null")){
                edtDescription.hint = "Insert Your Community Description"
            }
        }

    }

    private fun displayData(){

        val user = sharedPreferencesHelper.prefUid.toString()
        reference.child("UsersProfile").child(user).get().addOnCompleteListener {

            if(it.isSuccessful){
                val snapshot = it.result
                val name = snapshot.child("name").value
                val phoneNumber = snapshot.child("phoneNumber").value
                val picture = snapshot.child("picture").value
                val description = snapshot.child("description").value
                val instagram = snapshot.child("instagram").value
                val facebook = snapshot.child("facebook").value
                val twitter = snapshot.child("twitter").value


            binding.apply {
                edtName.setText(name.toString())
                edtPhoneNumber.setText(phoneNumber.toString())
                edtFb.setText(facebook.toString())
                edtDescription.setText(description.toString())
                edtIg.setText(instagram.toString())
                edtTwitter.setText(twitter.toString())

            }

                Glide.with(this).load(picture).into(binding.civProfile)



            }
        }
    }


    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            val selectedImage: Uri = it.data?.data as Uri
            filePath = selectedImage
            Glide.with(this).load(selectedImage).centerCrop().into(binding.civProfile)
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
        val phoneNumber = binding.edtPhoneNumber.text.toString()
        val description = binding.edtDescription.text.toString()
        val instagram = binding.edtIg.text.toString()
        val twitter = binding.edtTwitter.text.toString()
        val facebook = binding.edtFb.text.toString()
        val date = currentDate.toString()
        val uploadRef = FirebaseStorage.getInstance().getReference("/UserPhotoPicture/$user+_+$date")
        uploadRef.putFile(filePath!!).addOnSuccessListener {
            uploadRef.downloadUrl.addOnSuccessListener {
                val updateUser = mapOf<String,String>(
                    "name" to name,
                    "phoneNumber" to phoneNumber,
                    "description" to description,
                    "instagram" to instagram,
                    "twitter" to twitter,
                    "facebook" to facebook,
                    "picture" to it.toString()
                )

                reference.child("UsersProfile").child(user).updateChildren(updateUser).addOnSuccessListener {
                    Toast.makeText(this@EditCommunityActivity,"Berhasil di update", Toast.LENGTH_SHORT).show()
                    intent = Intent(this@EditCommunityActivity, CommunityProfileActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            }

        }.addOnFailureListener{
            Toast.makeText(this@EditCommunityActivity, it.message.toString() , Toast.LENGTH_SHORT).show()
            Log.d("failed","failed${filePath?.path} ")
            Log.d("failed",it.message.toString())

        }
    }
}