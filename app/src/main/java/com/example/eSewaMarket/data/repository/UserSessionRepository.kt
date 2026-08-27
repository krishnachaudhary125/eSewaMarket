package com.example.eSewaMarket.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.eSewaMarket.data.models.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userSession by preferencesDataStore(name = "user_session")

class UserSessionRepository(private val context: Context) {

    private object Keys {
        val ID = longPreferencesKey("id")
        val FIREBASE_UID = stringPreferencesKey("firebase_uid")
        val NAME = stringPreferencesKey("name")
        val EMAIL = stringPreferencesKey("email")
        val PHONE = stringPreferencesKey("phone")
        val PHOTO = stringPreferencesKey("photo")
        val EMAIL_VERIFIED = booleanPreferencesKey("emailVerified")
        val LOGGED_IN = booleanPreferencesKey("logged_in")
    }

    suspend fun saveUser(user: UserResponse) {
        context.userSession.edit { prefs ->
            prefs[Keys.ID] = user.id
            prefs[Keys.FIREBASE_UID] = user.firebaseUid
            prefs[Keys.NAME] = user.name
            prefs[Keys.EMAIL] = user.email
            prefs[Keys.PHONE] = user.phone ?: ""
            prefs[Keys.PHOTO] = user.photoUrl ?: ""
            prefs[Keys.EMAIL_VERIFIED] = true
            prefs[Keys.LOGGED_IN] = true
        }
    }

    val isLoggedIn: Flow<Boolean> =
        context.userSession.data.map {
            it[Keys.LOGGED_IN] ?: false
        }

    val user: Flow<UserResponse> =
        context.userSession.data.map { prefs ->
            UserResponse(
                id = prefs[Keys.ID] ?: 0L,
                firebaseUid = prefs[Keys.FIREBASE_UID] ?: "",
                name = prefs[Keys.NAME] ?: "",
                phone = prefs[Keys.PHONE] ?: "",
                email = prefs[Keys.EMAIL] ?: "",
                photoUrl = prefs[Keys.PHOTO] ?: "",
                role = "",
                createdAt = "",
                updatedAt = ""
            )
        }

    suspend fun logout() {
        clearSession()
    }

    suspend fun clearSession() {
        context.userSession.edit {
            it.clear()
        }
    }
}