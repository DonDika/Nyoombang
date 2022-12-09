package com.c22027.nyoombang.ui.profile.user

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.c22027.nyoombang.databinding.ActivityEditUserBinding
import com.c22027.nyoombang.data.local.SharedPreferencesHelper
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_donation.*
import java.text.SimpleDateFormat
import java.util.*

class EditUserActivity : AppCompatActivity() {
    private lateinit var reference: DatabaseReference
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var binding: ActivityEditUserBinding
    private var currentDate: String? = null
    private val db by lazy { Firebase.firestore}
    private var filePath: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        reference = FirebaseDatabase.getInstance().reference
        sharedPreferencesHelper = SharedPreferencesHelper(this)
        val sdf = SimpleDateFormat("ddMyyyyhh:mm:ss")
        currentDate = sdf.format(Date())
        binding.circleImageView.setOnClickListener {
            startGallery()
        }
        displayData()
        binding.btnSave.setOnClickListener {
            editAndUploadData()
        }

    }

    private fun displayData(){

        val user = sharedPreferencesHelper.prefUid.toString()
        db.collection("UsersProfile").document(user).get().addOnCompleteListener { it ->
            if (it.isSuccessful){
                val snapshot = it.result
                val name = snapshot.data!!["name"].toString()
                val phoneNumber = snapshot.data!!["phoneNumber"].toString()
                val picture = snapshot.data!!["picture"].toString()
                binding.edtName.setText(name)
                binding.edtPhoneNumber.setText(phoneNumber)
                Glide.with(this).load(picture).into(circleImageView)

            }
        }
    }
//        reference.data["Usersrofile").data[user).et().addOnCompleteListener {
//
//            if(it.isSuccessful){
//                val snapshot = it.result
//                val name = snapshot.data["name".value
//                val phoneNumber = snapshot.data["phoneumber").value
//                val picture = snapshot.data["pictue")
//
//                binding.edtName.setText(name.toString())
//                binding.edtPhoneNumber.setText(phoneNumber.toString())
//                Glide.with(this).load(picture).into(circleImageView)
//
//
//
//            }
//        }



    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            val selectedImage: Uri = it.data?.data as Uri
            filePath = selectedImage
            Glide.with(this).load(selectedImage).centerCrop().into(binding.circleImageView)
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
        val date = currentDate.toString()
        if (filePath ==null){
            val updateUser = mapOf<String,String>(
                "name" to name,
                "phoneNumber" to phoneNumber
            )

            db.collection("UsersProfile").document(user).update(updateUser).addOnSuccessListener {
                Toast.makeText(this@EditUserActivity,"Berhasil di update", Toast.LENGTH_SHORT).show()
                intent = Intent(this@EditUserActivity, UserProfileActivity::class.java)
                startActivity(intent)
                finish()

            }
        }else{
            val uploadRef = FirebaseStorage.getInstance().getReference("/UserPhotoPicture/$user+_+$date")
            uploadRef.putFile(filePath!!).addOnSuccessListener {
                uploadRef.downloadUrl.addOnSuccessListener {
                    val updateUser = mapOf<String,String>(
                        "name" to name,
                        "picture" to it.toString(),
                        "phoneNumber" to phoneNumber
                    )

                    db.collection("UsersProfile").document(user).update(updateUser).addOnSuccessListener {
                        Toast.makeText(this@EditUserActivity,"Berhasil di update", Toast.LENGTH_SHORT).show()
                        intent = Intent(this@EditUserActivity, UserProfileActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                }

            }.addOnFailureListener{
                Toast.makeText(this@EditUserActivity, it.message.toString() , Toast.LENGTH_SHORT).show()
                Log.d("failed","failed${filePath?.path} ")
                Log.d("failed",it.message.toString())

            }
        }

    }
}