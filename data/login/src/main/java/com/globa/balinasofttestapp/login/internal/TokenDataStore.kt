package com.globa.balinasofttestapp.login.internal

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenDataStore @Inject constructor(
    private val context: Context
){
    private val Context.dataStore by preferencesDataStore(name = "token_datastore")
    private val TOKEN_KEY = stringPreferencesKey("access_token")
    fun getAccessToken() = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY] ?: ""
    }

    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit {preferences ->
            preferences[TOKEN_KEY] = token
        }
    }
}