package com.shubh.crudapp

import android.util.Patterns
import androidx.lifecycle.*
import com.shubh.crudapp.db.User
import com.shubh.crudapp.db.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    val findeduser = MutableLiveData<User?>()
    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()
    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        clearAllOrDeleteButtonText.value = "Login"
    }

    fun saveOrUpdate() {
        val name = inputName.value!!
        val email = inputEmail.value!!
        insertUser(User(0, name, email))
        inputName.value = ""
        inputEmail.value = ""
    }

    private fun insertUser(User: User) = viewModelScope.launch {
        val newRowId = repository.insert(User)
        if (newRowId > -1) {
            statusMessage.value = Event("User Inserted Successfully $newRowId")
            findeduser.value = User
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }


    fun clearAllOrDelete() {

        if (inputName.value == null) {
            statusMessage.value = Event("Please enter User's name")
        } else if (inputEmail.value == null) {
            statusMessage.value = Event("Please enter User's email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessage.value = Event("Please enter a correct email address")
        } else {
            findUser()
        }

    }

    private fun findUser() = viewModelScope.launch {
        val noOfRowsDeleted = inputEmail.value?.let { repository.login(it) }
            findeduser.value = noOfRowsDeleted

    }

}