package com.shubh.crudapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shubh.crudapp.db.UserRepository

class LoginViewModelFactory(
        private val repository: UserRepository
        ):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }

}

