package com.c22027.nyoombang.ui.auth.register

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.c22027.nyoombang.R
import com.c22027.nyoombang.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    lateinit var ref: DatabaseReference
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    private fun setButton(state : Boolean){
        with(binding){
            btnRegister.isEnabled = state
        }
    }
}