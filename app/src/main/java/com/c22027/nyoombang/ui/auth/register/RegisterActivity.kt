package com.c22027.nyoombang.ui.auth.register


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.c22027.nyoombang.R
import com.c22027.nyoombang.data.model.UserDataClass
import com.c22027.nyoombang.databinding.ActivityRegisterBinding
import com.c22027.nyoombang.data.local.SharedPreferencesHelper
import com.c22027.nyoombang.ui.auth.login.LoginActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val sharedPreferences by lazy { SharedPreferencesHelper(this) }

    private val fireStore by lazy { Firebase.firestore }

    lateinit var ref: DatabaseReference


    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

       /* registerViewModel.toastObserverMessage.observe(this){
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
        }*/

        setupView()
        init()
        showLoading(false)
    }

    private fun init(){
        Log.d("masuk",binding.edtDropdownInputRole.text.toString())
        val items = listOf("Community", "User")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)


        ref = FirebaseDatabase.getInstance().getReference("UsersProfile")
        ( binding.edtDropdownInputRole as? AutoCompleteTextView)?.setAdapter(adapter)

        with(binding){
            btnRegister.setOnClickListener {

                registration()

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

    private fun registration() {
        val email = binding.edtEmail.text.toString()
        val phoneNumber = binding.edtPhoneNumber.text.toString()
        val password = binding.edtPassword.text.toString()
        val name = binding.edtName.text.toString()
        val role = binding.edtDropdownInputRole.text.toString()
        val key = fireStore.collection("UsersProfile").document().id
        fireStore.collection("UsersProfile")
            .whereEqualTo("email", email).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val document = task.result
                    if (document != null) {
                        val userProfile = UserDataClass(
                            key,
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
                        fireStore.collection("UsersProfile").document(key).set(userProfile)
                        showLoading(true)
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration Success",
                            Toast.LENGTH_SHORT
                        ).show()
                        intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Email already used ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }




    private fun setButton(state : Boolean){
        with(binding){
            btnRegister.isEnabled = state
        }
    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.pgLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}