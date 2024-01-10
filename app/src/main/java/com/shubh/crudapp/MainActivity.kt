package com.shubh.crudapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubh.crudapp.databinding.ActivityMainBinding
import com.shubh.crudapp.db.User
import com.shubh.crudapp.db.UserDatabase
import com.shubh.crudapp.db.UserRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private  var userViewModel: UserViewModel?=null
    private lateinit var adapter: MyRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dao = UserDatabase.getInstance(application).UserDAO
        val repository = UserRepository(dao)
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
        binding.myViewModel = userViewModel
        binding.lifecycleOwner = this

        userViewModel!!.message.observe(this) { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.userRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyRecyclerViewAdapter { selectedItem: User -> listItemClicked(selectedItem) }
        binding.userRecyclerView.adapter = adapter
        displayUsersList()
    }

    private fun displayUsersList() {
        userViewModel!!.getSavedUsers().observe(this, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(user: User) {
        userViewModel!!.initUpdateAndDelete(user)
    }
}