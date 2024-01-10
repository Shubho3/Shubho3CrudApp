package com.shubh.crudapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.shubh.crudapp.databinding.ListItemBinding
import com.shubh.crudapp.db.User

class MyRecyclerViewAdapter(private val clickListener: (User) -> Unit) :
        RecyclerView.Adapter<MyViewHolder>() {
    private val UsersList = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return UsersList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(UsersList[position], clickListener)
    }

    fun setList(Users: List<User>) {
        UsersList.clear()
        UsersList.addAll(Users)

    }

}

class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User, clickListener: (User) -> Unit) {
        binding.nameTextView.text = user.name
        binding.emailTextView.text = user.email
        binding.listItemLayout.setOnClickListener {
            clickListener(user)
        }
    }
}