package com.codeyuba.bfaa3submission.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codeyuba.bfaa3submission.R
import com.codeyuba.bfaa3submission.data.local.entity.UserEntity
import com.codeyuba.bfaa3submission.data.local.entity.UserWithFavStatusLiveData
import com.codeyuba.bfaa3submission.databinding.UserItemBinding
import com.codeyuba.bfaa3submission.interfaces.ItemAdapterCallback
import com.codeyuba.bfaa3submission.viewmodel.UserViewModel

class UserListAdapter(
    private val listUser: List<Any>,
    private val owner: LifecycleOwner,
    private val viewModel: UserViewModel
) : RecyclerView.Adapter<UserListAdapter.ListViewHolder>() {

    private lateinit var itemCallback: ItemAdapterCallback<UserWithFavStatusLiveData>

    class ListViewHolder(var binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(vGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(vGroup.context), vGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, pos: Int) {

        when (val user = listUser[pos]) {
            is UserWithFavStatusLiveData -> {
                holder.binding.apply {
                    tvListUsername.text = user.username

                    Glide.with(holder.itemView.context)
                        .load(user.avatarUrl)
                        .circleCrop()
                        .into(ivListAvatar)
                    user.isFavLiveData.observe(owner) {
                        if (it) {
                            btnFavorites.setImageResource(R.drawable.ic_baseline_favorite_fill_20)
                        } else {
                            btnFavorites.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                        }

                        btnFavorites.setOnClickListener { _ ->

                            if (it) {
                                viewModel.removeFav(user)
                            } else {
                                viewModel.setFav(user)
                            }
                        }

                        holder.itemView.setOnClickListener { view ->
                            itemCallback.onItemClicked(
                                view,
                                user
                            )
                        }
                    }
                }
            }
            is UserEntity -> {
                holder.binding.apply {
                    tvListUsername.text = user.username

                    Glide.with(holder.itemView.context)
                        .load(user.avatarUrl)
                        .circleCrop()
                        .into(ivListAvatar)

                    val usersItem = UserWithFavStatusLiveData(
                        user.username,
                        user.avatarUrl,
                        user.isFavorites,
                        viewModel.isFav(user.username)
                    )

                    if (user.isFavorites) {
                        btnFavorites.setImageResource(R.drawable.ic_baseline_favorite_fill_20)
                    } else {
                        btnFavorites.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    }

                    btnFavorites.setOnClickListener {

                        if (user.isFavorites) {
                            viewModel.removeFav(usersItem)
                        } else {
                            viewModel.setFav(usersItem)
                        }
                    }

                    holder.itemView.setOnClickListener { view ->
                        itemCallback.onItemClicked(
                            view,
                            usersItem
                        )
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int = listUser.size

    fun setAdapterItemCallback(callback: ItemAdapterCallback<UserWithFavStatusLiveData>) {
        this.itemCallback = callback
    }

}