package com.c22027.nyoombang.ui.auth.register


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.c22027.nyoombang.MainActivity
import com.c22027.nyoombang.R
import com.c22027.nyoombang.data.model.UserDataClass
import com.c22027.nyoombang.data.model.UserResponse
import com.c22027.nyoombang.databinding.ActivityRegisterBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper
import com.c22027.nyoombang.ui.addevent.AddEventActivity
import com.c22027.nyoombang.ui.auth.login.LoginActivity
import com.c22027.nyoombang.ui.profile.community.CommunityProfileActivity
import com.c22027.nyoombang.ui.profile.user.UserProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    lateinit var ref: DatabaseReference

    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        registerViewModel.toastObserverMessage.observe(this){
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
        }




        init()
    }

    private fun init(){

        Log.d("masuk",binding.edtDropdownInputRole.text.toString())
        val items = listOf("Community", "User")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)


        ref = FirebaseDatabase.getInstance().getReference("UsersProfile")
        ( binding.edtDropdownInputRole as? AutoCompleteTextView)?.setAdapter(adapter)

        with(binding){

            btnRegister.setOnClickListener {

                registerUser()

                //condition-already-enabled

            }

            (edtDropdownInputRole).onItemClickListener =
                OnItemClickListener { _, _, position, _ ->
                    registerViewModel.stateRole(adapter.getItem(position))
                    registerViewModel.stateCheckBtnRegister()
                }

            registerViewModel.btnRegister.observe(this@RegisterActivity){
                setButton(it)
            }

            cbtnTermsRoles.setOnCheckedChangeListener { _, isChecked ->
                registerViewModel.stateCheckbox(isChecked)
                registerViewModel.stateCheckBtnRegister()
            }

            edtName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    registerViewModel.stateName(s.toString())
                    registerViewModel.stateCheckBtnRegister()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            edtEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    registerViewModel.stateEmail(s.toString())
                    registerViewModel.stateCheckBtnRegister()
                }

                override fun afterTextChanged(s: Editable?) {
                    edtLayoutEmail.error = registerViewModel.validEmail()
                }
            })

            edtPhoneNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    registerViewModel.stateUsername(s.toString())
                    registerViewModel.stateCheckBtnRegister()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            edtPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    registerViewModel.statePassword(s.toString())
                    registerViewModel.stateCheckBtnRegister()
                }

                override fun afterTextChanged(s: Editable?) {
                    edtLayoutPassword.error = registerViewModel.validPassword()
                }
            })

            edtConfirmPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    registerViewModel.stateConfirmPassword(s.toString())
                    registerViewModel.stateCheckBtnRegister()
                    edtLayoutConfirmPassword.error = registerViewModel.validConfirmPassword()
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun registration(response: UserResponse){
        val email = binding.edtEmail.text.toString()
        val phoneNumber = binding.edtPhoneNumber.text.toString()
        val password = binding.edtPassword.text.toString()
        val name = binding.edtName.text.toString()
        val role = binding.edtDropdownInputRole.text.toString()
        response.items?.let { users ->
            if (users.isEmpty()) {
                ref = FirebaseDatabase.getInstance().getReference("UsersProfile")
                val userId = ref.push().key.toString()
                val userProfile = UserDataClass(
                    userId,
                    email,
                    phoneNumber,
                    password,
                    role,
                    name,
                    "",
                    "",
                    "",
                    "",
                    ""
                )
                ref.child(userId).setValue(userProfile).addOnSuccessListener {
                    Toast.makeText(this, "Registration Success", Toast.LENGTH_SHORT).show()
                    intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                    finish()

                }.addOnFailureListener {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()

                }

        }else {
                    Toast.makeText(this, "Email or username Already used", Toast.LENGTH_SHORT)
                        .show()

                }

        }


    }


    private fun registerUser(){
        val email = binding.edtEmail.text.toString()
        registerViewModel.register(email).observe(this){
            registration(it)

        }

        }

    private fun setButton(state : Boolean){
        with(binding){
            btnRegister.isEnabled = state
        }
    }
}