package com.codeyuba.bfaa3submission.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeyuba.bfaa3submission.data.local.entity.UserWithFavStatusLiveData
import com.codeyuba.bfaa3submission.data.repository.UserRepository

open class UserViewModel(private val userRepo: UserRepository): ViewModel() {

    val searchRes = userRepo.searchResult

    fun getFamouseUser() = userRepo.getFamouseUsers()

    fun getFavUser() = userRepo.getFavUsers()

    fun setFav(user: UserWithFavStatusLiveData) = userRepo.setFavUser(user)

    fun removeFav(user: UserWithFavStatusLiveData) = userRepo.removeFavUser(user)

    fun searchUser(keyword: String) = userRepo.searchUser(keyword)

    fun resetSearchUser() = userRepo.resetSearch()

    fun getDetailUser(username: String) = userRepo.getDetailUser(username)

    fun getFollowers(username: String) = userRepo.getUserFollowers(username)

    fun getFollowing(username: String) = userRepo.getUserFollowing(username)

    fun isFav(username: String) = userRepo.isFavorites(username)

    fun getThemeSettings() = userRepo.getThemeSettings()

    fun setThemeSettings(status: Boolean) = userRepo.saveThemeSetting(status, viewModelScope)

}
