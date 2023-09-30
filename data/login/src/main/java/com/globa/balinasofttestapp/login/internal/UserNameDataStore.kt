package com.globa.balinasofttestapp.login.internal

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class UserNameDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore by preferencesDataStore(name = "uer_name_datastore")
    private val TOKEN_KEY = stringPreferencesKey("user_name")
    fun getUserName() = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY] ?: ""
    }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit {preferences ->
            preferences[TOKEN_KEY] = name
        }
    }
}