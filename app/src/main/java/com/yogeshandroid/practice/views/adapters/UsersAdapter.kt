package com.yogeshandroid.practice.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.squareup.picasso.Picasso
import com.yogeshandroid.practice.databinding.RvUserBinding
import com.yogeshandroid.practice.model.UserResponse

class UsersAdapter(var myData: List<UserResponse.User>, val context: Context) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    class UserViewHolder(val rvUserBinding: RvUserBinding) : ViewHolder(rvUserBinding.root)

    fun filterList(searchList: MutableList<UserResponse.User>) {
        myData = searchList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            RvUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val single = myData[position]

        Picasso.get().load(single.image).into(holder.rvUserBinding.img)
        holder.rvUserBinding.nameTv.text = single.firstName
        holder.rvUserBinding.emailTv.text = single.email
        holder.rvUserBinding.ageTv.text = single.age.toString()
        holder.rvUserBinding.idTv.text = single.id.toString()
    }

    override fun getItemCount() = myData.size
}