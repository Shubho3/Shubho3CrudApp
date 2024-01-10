package com.shubh.crudapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shubh.crudapp.db.UserRepository

class UserViewModelFactory(
        private val repository: UserRepository
        ):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(repository) as T
    }

}

