package com.codeyuba.submisionbfaaone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codeyuba.submisionbfaaone.data.GithubUser
import com.codeyuba.submisionbfaaone.databinding.ItemRowUserBinding

class UserListAdapter(private val listUsers: ArrayList<GithubUser>,private var onItemClickCallback: OnItemClickCallback) :
    RecyclerView.Adapter<UserListAdapter.ListViewHolder>() {

    fun setOnClickCallBack(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserListAdapter.ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserListAdapter.ListViewHolder, position: Int) {
        val name = listUsers[position].name
        val username = listUsers[position].username
        val photo = listUsers[position].avatar

        Glide.with(holder.itemView.context)
            .load(photo)
            .apply(RequestOptions().override(100, 100))
            .into(holder.binding.imgAvatar)

        holder.binding.tvName.text = name
        holder.binding.tvUsername.text = "@"+username

        holder.itemView.setOnClickListener { onItemClickCallback!!.onItemClicked(listUsers[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }

    inner class ListViewHolder(var binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: GithubUser)
    }

}