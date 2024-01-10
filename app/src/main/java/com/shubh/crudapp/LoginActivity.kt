package com.shubh.crudapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubh.crudapp.databinding.ActivityLoginBinding
import com.shubh.crudapp.databinding.ActivityMainBinding
import com.shubh.crudapp.db.User
import com.shubh.crudapp.db.UserDatabase
import com.shubh.crudapp.db.UserRepository

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var userViewModel: LoginViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        val dao = UserDatabase.getInstance(application).UserDAO
        val repository = UserRepository(dao)
        val factory = LoginViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        binding.myViewModel = userViewModel
        binding.lifecycleOwner = this
        userViewModel!!.findeduser.observe(this) {
            Log.e("TAG", "onCreate: $it")
            if (it != null) {
                Toast.makeText(applicationContext, "Welcome User ", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                userViewModel!!.saveOrUpdate()
                Toast.makeText(applicationContext, "No User found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}