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
import com.c22027.nyoombang.databinding.ActivityRegisterBinding
import com.c22027.nyoombang.helper.SharedPreferencesHelper
import com.c22027.nyoombang.ui.addevent.AddEventActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
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

        registerViewModel.firebaseData.observe(this) {
            if (it != null) {
                val email = it.email.toString()
                val password = binding.edtPassword.text.toString()
                val role = binding.edtDropdownInputRole.text.toString()
                val name = binding.edtName.text.toString()
                registerViewModel.addUser(email,password,role,name)
                val query: Query = database.child("UsersProfile").orderByChild("email").equalTo(email)
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (item in snapshot.children) {
                                val user = item.getValue<UserDataClass>()
                                if (user != null) {
                                    sharedPreferencesHelper.prefStatus = true
                                    sharedPreferencesHelper.prefLevel = user.role
                                    sharedPreferencesHelper.prefUid = user.user_id
                                    if (user.role.equals("User")) {
                                        intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else{
                                        intent =
                                            Intent(this@RegisterActivity, AddEventActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    })
            }
        }
        init()



        init()
    }

    private fun init(){

        Log.d("masuk",binding.edtDropdownInputRole.text.toString())
        val items = listOf("Community", "User")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)

        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("USERS")
        ( binding.edtDropdownInputRole as? AutoCompleteTextView)?.setAdapter(adapter)

        with(binding){

            btnRegister.setOnClickListener {

                register()

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

            edtUsername.addTextChangedListener(object : TextWatcher {
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


    private fun register(){
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        registerViewModel.register(email, password)
        }

    private fun setButton(state : Boolean){
        with(binding){
            btnRegister.isEnabled = state
        }
    }
}