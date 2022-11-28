package com.c22027.nyoombang.ui.auth.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.c22027.nyoombang.MainActivity
import com.c22027.nyoombang.data.local.UserDataClass
import com.c22027.nyoombang.databinding.ActivityLoginBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper
import com.c22027.nyoombang.ui.dasboard.DashboardActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.c22027.nyoombang.R
import com.c22027.nyoombang.ui.auth.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var binding: ActivityLoginBinding
    private lateinit var context: Context
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        sharedPreferencesHelper= SharedPreferencesHelper(context)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginButton.setOnClickListener {
            loginUser()
        }
        binding.edtEmail.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textInputLayoutEmail.error = validEmail()
                setButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        binding.edtPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textInputLayoutPassword.error = validPassword()
                setButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.signUp.setOnClickListener {
            val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun validEmail(): String? {
     val emailText = binding.edtEmail.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return context.getString(R.string.email_error)
        }
        return null
    }
    private fun validPassword(): String? {
        val passwordText = binding.edtPassword.text.toString()
        if (!passwordText.isNullOrEmpty() && passwordText.length < 6){
            return context.getString(R.string.password_error)
        }
        return null
    }

    private fun loginUser() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val query: Query = database.child("User").orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        val user = item.getValue<UserDataClass>()
                        if (user != null) {
                            if (user.password.equals(password)) {
                                sharedPreferencesHelper.prefStatus = true
                                sharedPreferencesHelper.prefLevel = user.role
                                if (user.role.equals("User")) {
                                    intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    intent =
                                        Intent(this@LoginActivity, DashboardActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Kata sandi salah masukan kembali",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Email Belum terdaftar",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    context,
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })


        }
    private fun setButtonEnable() {
        binding.apply {
            val password = edtPassword.text
            val email = edtEmail.text
            loginButton.isEnabled = password.toString().length >= 6 && Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches()
        }
    }



    }

