package com.shubh.crudapp

import android.util.Patterns
import androidx.lifecycle.*
import com.shubh.crudapp.db.User
import com.shubh.crudapp.db.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    private var isUpdateOrDelete = false
    private lateinit var UserToUpdateOrDelete: User
    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()
    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun initUpdateAndDelete(user: User) {
        inputName.value = user.name
        inputEmail.value = user.email
        isUpdateOrDelete = true
        UserToUpdateOrDelete = user
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    fun saveOrUpdate() {
        if (inputName.value == null) {
            statusMessage.value = Event("Please enter User's name")
        } else if (inputEmail.value == null) {
            statusMessage.value = Event("Please enter User's email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessage.value = Event("Please enter a correct email address")
        } else {
            if (isUpdateOrDelete) {
                UserToUpdateOrDelete.name = inputName.value!!
                UserToUpdateOrDelete.email = inputEmail.value!!
                updateUser(UserToUpdateOrDelete)
            } else {
                val name = inputName.value!!
                val email = inputEmail.value!!
                insertUser(User(0, name, email))
                inputName.value = ""
                inputEmail.value = ""
            }
        }
    }

    private fun insertUser(User: User) = viewModelScope.launch {
        val newRowId = repository.insert(User)
        if (newRowId > -1) {
            statusMessage.value = Event("User Inserted Successfully $newRowId")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }


    private fun updateUser(User: User) = viewModelScope.launch {
        val noOfRows = repository.update(User)
        if (noOfRows > 0) {
            inputName.value = ""
            inputEmail.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRows Row Updated Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun getSavedUsers() = liveData {
        repository.users.collect {
            emit(it)
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            deleteUser(UserToUpdateOrDelete)
        } else {
            clearAll()
        }
    }

    private fun deleteUser(User: User) = viewModelScope.launch {
        val noOfRowsDeleted = repository.delete(User)
        if (noOfRowsDeleted > 0) {
            inputName.value = ""
            inputEmail.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRowsDeleted Row Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    private fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteAll()
        if (noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Users Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }
}