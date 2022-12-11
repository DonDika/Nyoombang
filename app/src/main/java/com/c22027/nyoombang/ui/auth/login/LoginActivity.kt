package com.c22027.nyoombang.ui.auth.login


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.c22027.nyoombang.databinding.ActivityLoginBinding
import com.c22027.nyoombang.data.local.SharedPreferencesHelper
import com.c22027.nyoombang.R
import com.c22027.nyoombang.ui.addevent.AddEventActivity
import com.c22027.nyoombang.ui.auth.register.RegisterActivity
import com.c22027.nyoombang.ui.dashboard.DashboardActivity
import com.c22027.nyoombang.ui.dashboard.DashboardCommunity
import com.c22027.nyoombang.ui.profile.user.UserProfileActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val fireStore by lazy { Firebase.firestore}
    private val sharedPreferences by lazy { SharedPreferencesHelper(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupListener()
        showLoading(false)
    }

    override fun onStart() {
        super.onStart()

        if(sharedPreferences.prefLevel.equals("User")){
            intent = Intent(this@LoginActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        } else if(sharedPreferences.prefLevel.equals("Community")){
            intent = Intent(this@LoginActivity, DashboardCommunity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun setupListener() {
        binding.loginButton.setOnClickListener {
            loginUser()
        }

        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textInputLayoutEmail.error = validEmail()
                setButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.edtPassword.addTextChangedListener(object : TextWatcher {
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
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


    @SuppressLint("SuspiciousIndentation")
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

    private fun loginUser() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        fireStore.collection("UsersProfile")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.isEmpty) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Akun anda tidak tersedia periksa kembali email dan password",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        document.forEach {
                            sharedPreferences.prefStatus = true
                            sharedPreferences.prefLevel = it.data["role"].toString()
                            sharedPreferences.prefUid = it.data["user_id"].toString()
                            sharedPreferences.prefUsername = it.data["name"].toString()
                            sharedPreferences.prefPhone = it.data["phoneNumber"].toString()
                            sharedPreferences.prefEmail = it.data["email"].toString()
                            if (it.data["role"].toString() == "User") {
                                showLoading(true)
                                intent = Intent(this@LoginActivity,DashboardActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else if (it.data["role"].toString() == "Community") {
                                showLoading(true)
                                intent = Intent(this@LoginActivity, DashboardCommunity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                    }
                }
            }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.pgLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun setButtonEnable() {
        binding.apply {
            val password = edtPassword.text
            val email = edtEmail.text
            loginButton.isEnabled =
                password.toString().length >= 6 && Patterns.EMAIL_ADDRESS.matcher(email.toString())
                    .matches()
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






}

