package com.c22027.nyoombang.ui.auth.register


import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.c22027.nyoombang.repository.AppsRepositoryImpl
import com.google.firebase.auth.FirebaseUser

class RegisterViewModel(private val repository: AppsRepositoryImpl = AppsRepositoryImpl()): ViewModel(){


    val firebaseData:MutableLiveData<FirebaseUser> = repository.firebaseUserData
    val toastObserverMessage: MutableLiveData<String> = repository.toastObserverMessage


    private val _role = MutableLiveData<String?>()
    val role: LiveData<String?> = _role

    private val _btnRegister = MutableLiveData<Boolean>()
    val btnRegister: LiveData<Boolean> = _btnRegister

    private val _btnCheck = MutableLiveData<Boolean>()
    val btnCheck : LiveData<Boolean> = _btnCheck

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _confirmPassword = MutableLiveData<String>()

    val confirmPassword: LiveData<String> = _confirmPassword




    init {
        _btnRegister.value = false
        _role.value = ""
    }

    fun register(email:String,password: String)= repository.register(email, password)

    fun addUser(email: String,password: String,role:String, name: String) = repository.addUser(email,password,role,name)



    fun stateCheckBtnRegister(){
        _btnRegister.value = (_name.value.toString().isNotBlank() && _password.value.toString() == _confirmPassword.value.toString()
                && _username.value.toString().isNotBlank() && _btnCheck.value == true && Patterns.EMAIL_ADDRESS.matcher(_email.value.toString()).matches()
                && _role.value.toString().isNotBlank())
    }
    fun stateCheckbox(state : Boolean){
        _btnCheck.value = state
    }

    fun stateRole(state : String?){
        _role.value = state
    }

    fun stateEmail(value : String){
        _email.value = value
    }

    fun stateUsername(value : String){
        _username.value = value
    }

    fun stateName(value : String){
        _name.value = value
    }

    fun statePassword(value : String){
        _password.value = value
    }

    fun stateConfirmPassword(value : String){
        _confirmPassword.value = value
    }

    fun validEmail(): String? {
        if (!Patterns.EMAIL_ADDRESS.matcher(_email.value.toString()).matches()){
            return "Email not Valid"
        }
        return null
    }

    fun validConfirmPassword(): String? {
        if (_password.value.toString() != _confirmPassword.value.toString()){
            return "Password not Same"
        }
        return null
    }

    fun validPassword(): String? {
        if (_password.value.toString().isBlank() || _password.value.toString().length < 6){
            return "Password not Valid"
        }
        return null
    }

}

