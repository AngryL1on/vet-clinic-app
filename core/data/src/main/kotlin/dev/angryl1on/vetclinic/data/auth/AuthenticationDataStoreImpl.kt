package dev.angryl1on.vetclinic.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.angryl1on.vetclinic.model.auth.AuthDataStore
import kotlinx.coroutines.flow.map

class AuthenticationDataStoreImpl(
    private val dataStore: DataStore<Preferences>
) : AuthenticationDataStore {

    override suspend fun fetchUsers() = dataStore.data.map { prefs ->
        AuthDataStore(
            id = prefs[USER_ID],
            accessToken = prefs[ACCESS_TOKEN],
            refreshToken = prefs[REFRESH_TOKEN],
            accessTokenExpiresIn = prefs[ACCESS_TOKEN_EXPIRES_IN],
            refreshTokenExpiresIn = prefs[REFRESH_TOKEN_EXPIRES_IN]
        )
    }

    override suspend fun setUserId(userId: Long) {
        dataStore.edit { it[USER_ID] = userId }
    }

    override suspend fun setAccessToken(accessToken: String) {
        dataStore.edit { it[ACCESS_TOKEN] = accessToken }
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        dataStore.edit { it[REFRESH_TOKEN] = refreshToken }
    }

    override suspend fun setAccessTokenExpiresIn(accessTokenExpiresIn: Int) {
        dataStore.edit { it[ACCESS_TOKEN_EXPIRES_IN] = accessTokenExpiresIn }
    }

    override suspend fun setRefreshTokenExpiresIn(refreshTokenExpiresIn: Int) {
        dataStore.edit { it[REFRESH_TOKEN_EXPIRES_IN] = refreshTokenExpiresIn }
    }

    override suspend fun updateUser(user: AuthDataStore) {
        dataStore.edit { preferences ->
            with(user) {
                id?.let { id ->
                    preferences[USER_ID] = id
                } ?: preferences.remove(USER_ID)

                accessToken?.let { accessToken ->
                    preferences[ACCESS_TOKEN] = accessToken
                } ?: preferences.remove(ACCESS_TOKEN)

                refreshToken?.let { refreshToken ->
                    preferences[REFRESH_TOKEN] = refreshToken
                } ?: preferences.remove(REFRESH_TOKEN)

                accessTokenExpiresIn?.let { accessTokenExpiresIn ->
                    preferences[ACCESS_TOKEN_EXPIRES_IN] = accessTokenExpiresIn
                } ?: preferences.remove(ACCESS_TOKEN_EXPIRES_IN)

                refreshTokenExpiresIn?.let { refreshTokenExpiresIn ->
                    preferences[REFRESH_TOKEN_EXPIRES_IN] = refreshTokenExpiresIn
                } ?: preferences.remove(REFRESH_TOKEN_EXPIRES_IN)
            }
        }
    }

    companion object {
        val USER_ID = longPreferencesKey("id")
        val ACCESS_TOKEN = stringPreferencesKey("accessToken")
        val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
        val ACCESS_TOKEN_EXPIRES_IN = intPreferencesKey("accessTokenExpiresIn")
        val REFRESH_TOKEN_EXPIRES_IN = intPreferencesKey("refreshTokenExpiresIn")
    }
}