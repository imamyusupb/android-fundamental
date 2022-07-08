package com.codeyuba.bfaa3submission.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import com.codeyuba.bfaa3submission.data.local.entity.UserEntity
import com.codeyuba.bfaa3submission.data.Result
import com.codeyuba.bfaa3submission.data.local.entity.UserWithFavStatusLiveData
import com.codeyuba.bfaa3submission.data.local.preferences.SettingPreferences
import com.codeyuba.bfaa3submission.data.local.room.UserDao
import com.codeyuba.bfaa3submission.data.remote.response.UserItem
import com.codeyuba.bfaa3submission.data.remote.response.UserListResponse
import com.codeyuba.bfaa3submission.data.remote.response.UserResponse
import com.codeyuba.bfaa3submission.data.remote.retrofit.ApiServices
import com.codeyuba.bfaa3submission.helper.AppExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiServices,
    private val userDao: UserDao,
    private val appExecutors: AppExecutors,
    private val pref: SettingPreferences
) {
    private val famousRes = MediatorLiveData<Result<List<UserWithFavStatusLiveData>>>()
    private val followersRes = MediatorLiveData<Result<List<UserWithFavStatusLiveData>>>()
    private val followingRes = MediatorLiveData<Result<List<UserWithFavStatusLiveData>>>()
    private val detailResult = MediatorLiveData<Result<UserResponse>>()
    val searchResult = MediatorLiveData<Result<List<UserWithFavStatusLiveData>>>()

    fun getFamouseUsers(): LiveData<Result<List<UserWithFavStatusLiveData>>> {
        famousRes.value = Result.Loading

        val userItem = arrayListOf(
            UserWithFavStatusLiveData(avatar = "https://avatars.githubusercontent.com/u/66577?v=4", username = "JakeWharton", isFav = false, isFavLiveData = isFavorites("JakeWharton")),
            UserWithFavStatusLiveData(avatar = "https://avatars.githubusercontent.com/u/9877145?v=4", username = "amitshekhariitbhu", isFav = false, isFavLiveData = isFavorites("amitshekhariitbhu")),
            UserWithFavStatusLiveData(avatar = "https://avatars.githubusercontent.com/u/869684?v=4", username = "romainguy", isFav = false, isFavLiveData = isFavorites("romainguy")),
            UserWithFavStatusLiveData(avatar = "https://avatars.githubusercontent.com/u/227486?v=4", username = "chrisbanes", isFav = false, isFavLiveData = isFavorites("chrisbanes")),
            UserWithFavStatusLiveData(avatar = "https://avatars.githubusercontent.com/u/1521451?v=4", username = "tipsy", isFav = false, isFavLiveData = isFavorites("tipsy")),
            UserWithFavStatusLiveData(avatar = "https://avatars.githubusercontent.com/u/497670?v=4", username = "ravi8x", isFav = false, isFavLiveData = isFavorites("ravi8x")),
            UserWithFavStatusLiveData(avatar = "https://avatars.githubusercontent.com/u/363917?v=4", username = "jasoet", isFav = false, isFavLiveData = isFavorites("jasoet")),
            UserWithFavStatusLiveData(avatar = "https://avatars.githubusercontent.com/u/2031493?v=4", username = "budioktaviyan", isFav = false, isFavLiveData = isFavorites("budioktaviyan")),
            UserWithFavStatusLiveData(avatar = "https://avatars.githubusercontent.com/u/3713580?v=4", username = "hendisantika", isFav = false, isFavLiveData = isFavorites("hendisantika")),
            UserWithFavStatusLiveData(avatar = "https://avatars.githubusercontent.com/u/4090245?v=4", username = "sidiqpermana", isFav = false, isFavLiveData = isFavorites("sidiqpermana")),
        )

        famousRes.value = Result.Success(userItem)

        return famousRes
    }

    fun getFavUsers(): LiveData<List<UserEntity>> {
        return userDao.getFav()
    }

    fun setFavUser(user: UserWithFavStatusLiveData) {
        appExecutors.diskIO.execute {

            userDao.addFav(
                UserEntity(
                user.username,
                user.avatarUrl,
                true,
            ))

        }
    }

    fun removeFavUser(user: UserWithFavStatusLiveData) {
        appExecutors.diskIO.execute {

            userDao.removeFav(UserEntity(
                user.username,
                user.avatarUrl,
                user.isFavorites,
            ))

        }
    }


    fun searchUser(keyword: String) {
        searchResult.value = Result.Loading
        val request = apiService.searchUsers(keyword)
        request.enqueue(object: Callback<UserListResponse> {
            override fun onResponse(call: Call<UserListResponse>, response: Response<UserListResponse>) {
                if (response.isSuccessful){
                    response.body()?.users.let {
                        val resArray = ArrayList<UserWithFavStatusLiveData>()
                        it?.forEach { item ->
                            resArray.add(
                                UserWithFavStatusLiveData(item.username, item.avatarUrl, false, isFavorites(item.username))
                            )
                        }
                        searchResult.value = Result.Success(resArray)
                    }
                }else{
                    Log.e("Detail User", "onFailure: ${response.message()}")
                    searchResult.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                Log.e("Detail User", "onFailure: ${t.message.toString()}")
                searchResult.value = Result.Error(t.message.toString())
            }

        })

    }

    fun resetSearch() {
        searchResult.value = Result.Success(ArrayList())
    }

    fun getDetailUser(username: String): LiveData<Result<UserResponse>> {
        detailResult.value = Result.Loading
        val request = apiService.getUser(username)
        request.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        detailResult.value = Result.Success(it)
                    }
                } else {
                    Log.e("Detail User Repository", "onFailure: ${response.message()}")
                    detailResult.value = Result.Error(response.message())
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("Detail User Repository", "onFailure: ${t.message.toString()}")
                detailResult.value = Result.Error(t.message.toString())
            }
        })

        return detailResult
    }


    fun getUserFollowers(username: String): LiveData<Result<List<UserWithFavStatusLiveData>>> {
        followersRes.value = Result.Loading
        val request = apiService.getUserFollowers(username)
        request.enqueue(object: Callback<List<UserItem>> {
            override fun onResponse(call: Call<List<UserItem>>, response: Response<List<UserItem>>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        val resArray = ArrayList<UserWithFavStatusLiveData>()
                        it.forEach { item ->
                            resArray.add(
                                UserWithFavStatusLiveData(item.username, item.avatarUrl, false, isFavorites(item.username))
                            )
                        }
                        followersRes.value = Result.Success(resArray)
                    }
                }else{
                    Log.e("Detail User", "onFailure: ${response.message()}")
                    followersRes.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                Log.e("Detail User", "onFailure: ${t.message.toString()}")
                followersRes.value = Result.Error(t.message.toString())
            }

        })

        return followersRes
    }

    fun getUserFollowing(username: String): LiveData<Result<List<UserWithFavStatusLiveData>>> {
        followingRes.value = Result.Loading
        val request = apiService.getUserFollowing(username)
        request.enqueue(object: Callback<List<UserItem>> {
            override fun onResponse(call: Call<List<UserItem>>, response: Response<List<UserItem>>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        val resArray = ArrayList<UserWithFavStatusLiveData>()
                        it.forEach { item ->
                            resArray.add(
                                UserWithFavStatusLiveData(item.username, item.avatarUrl, false, isFavorites(item.username))
                            )
                        }
                        followingRes.value = Result.Success(resArray)
                    }
                }else{
                    Log.e("Detail User", "onFailure: ${response.message()}")
                    followingRes.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                Log.e("Detail User", "onFailure: ${t.message.toString()}")
                followingRes.value = Result.Error(t.message.toString())
            }

        })

        return followingRes
    }

    fun isFavorites(username: String): LiveData<Boolean> {
        return userDao.isFav(username)
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean, viewModelLifeCycle: CoroutineScope) {
        viewModelLifeCycle.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiServices,
            userDao: UserDao,
            appExecutors: AppExecutors,
            pref: SettingPreferences

        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao, appExecutors, pref)
            }.also { instance = it }
    }
}