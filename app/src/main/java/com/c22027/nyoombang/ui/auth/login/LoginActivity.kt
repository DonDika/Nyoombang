package com.c22027.nyoombang.ui.auth.login


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.c22027.nyoombang.MainActivity
import com.c22027.nyoombang.data.local.UserDataClass
import com.c22027.nyoombang.databinding.ActivityLoginBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper
import com.c22027.nyoombang.ui.dashboard.DashboardActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.c22027.nyoombang.R
import com.c22027.nyoombang.data.local.UserResponse
import com.c22027.nyoombang.ui.auth.register.RegisterActivity
import com.c22027.nyoombang.ui.auth.register.RegisterViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var binding: ActivityLoginBinding
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferencesHelper= SharedPreferencesHelper(this)
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
            return this.getString(R.string.email_error)
        }
        return null
    }
    private fun validPassword(): String? {
        val passwordText = binding.edtPassword.text.toString()
        if (!passwordText.isNullOrEmpty() && passwordText.length < 6){
            return this.getString(R.string.password_error)
        }
        return null
    }


private fun login(response: UserResponse){
    val password = binding.edtPassword.text.toString()
    response.items?.let { users ->
        users.forEach { user ->
            if (user.password.equals(password)) {
                sharedPreferencesHelper.prefStatus = true
                sharedPreferencesHelper.prefLevel = user.role
                if (user.role.equals("User")) {
                    intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else if (user.role.equals("Community")) {
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
        }
    }
}

    private fun loginUser() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        loginViewModel.loginUsingLiveData(email, password).observe(this) {
           login(it)

                }
            }


//        val query: Query = database.child("UsersProfile").orderByChild("email").equalTo(email)
//        query.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    for (item in snapshot.children) {
//                        val user = item.getValue<UserDataClass>()
//                        if (user != null) {
//                            if (user.password.equals(password)) {
//                                sharedPreferencesHelper.prefStatus = true
//                                sharedPreferencesHelper.prefLevel = user.role
//                                if (user.role.equals("User")) {
//                                    intent = Intent(this@LoginActivity, MainActivity::class.java)
//                                    startActivity(intent)
//                                    finish()
//                                } else if(user.role.equals("Community")){
//                                    intent =
//                                        Intent(this@LoginActivity, DashboardActivity::class.java)
//                                    startActivity(intent)
//                                    finish()
//                                }
//                            } else {
//                                Toast.makeText(
//                                    this@LoginActivity,
//                                    "Kata sandi salah masukan kembali",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        } else {
//                            Toast.makeText(
//                                this@LoginActivity,
//                                "Email Belum terdaftar",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(
//                    this@LoginActivity,
//                    error.message,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//        })



    private fun setButtonEnable() {
        binding.apply {
            val password = edtPassword.text
            val email = edtEmail.text
            loginButton.isEnabled = password.toString().length >= 6 && Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches()
        }
    }

    override fun onStart() {
        super.onStart()
        if(sharedPreferencesHelper.prefLevel.equals("User")){
            intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else if(sharedPreferencesHelper.prefLevel.equals("Community")){
            intent =
                Intent(this@LoginActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



    }

