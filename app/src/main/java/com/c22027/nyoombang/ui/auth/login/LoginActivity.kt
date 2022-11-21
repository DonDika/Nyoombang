package com.c22027.nyoombang.ui.auth.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.c22027.nyoombang.MainActivity
import com.c22027.nyoombang.data.local.UserDataClass
import com.c22027.nyoombang.databinding.ActivityLoginBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper
import com.c22027.nyoombang.ui.dasboard.DashboardActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.c22027.nyoombang.R

class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var binding: ActivityLoginBinding
    private lateinit var context: Context
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        sharedPreferencesHelper= SharedPreferencesHelper(context)

        binding.loginButton.setOnClickListener {
            loginUser()
        }
        binding.edtEmail.setOnFocusChangeListener{_,focused ->
            if (!focused){
                binding.textInputLayoutEmail.error = validEmail()
            }

        }
    }

    private fun validEmail(): String? {
     val emailText = binding.edtEmail.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return context.getString(R.string.email_error)
        }
        return null
    }

    private fun loginUser() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val query: Query = database.child("Login").orderByChild("email").equalTo(email)
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



    }

