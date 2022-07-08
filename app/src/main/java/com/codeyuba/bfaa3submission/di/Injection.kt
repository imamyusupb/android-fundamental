package com.codeyuba.bfaa3submission.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.codeyuba.bfaa3submission.data.local.preferences.SettingPreferences
import com.codeyuba.bfaa3submission.data.local.room.UserDatabase
import com.codeyuba.bfaa3submission.data.remote.retrofit.GithubApi
import com.codeyuba.bfaa3submission.data.repository.UserRepository
import com.codeyuba.bfaa3submission.helper.AppExecutors

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object Injection {


    fun provideRepository(ctx: Context): UserRepository {
        val apiService = GithubApi.getServiceApi()
        val database = UserDatabase.getInstance(ctx)
        val dao = database.userDao()
        val appExecutors = AppExecutors()
        val pref = SettingPreferences.getInstance(ctx.dataStore)

        return UserRepository.getInstance(apiService, dao, appExecutors, pref)
    }
}