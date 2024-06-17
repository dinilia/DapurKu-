package org.d3if3119.dapurku.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.d3if3119.dapurku.model.User


val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
    name = "user_preference"
)
class UserDataStore(private val context: Context) {

    companion object {
        private val USER_NAME = stringPreferencesKey("name")
        private val USER_EMAIL = stringPreferencesKey("email")
        private val USER_PHOTO = stringPreferencesKey("photoUrl")

    }

    val userFlow: Flow<User> = context.dataStore.data.map {
        User(
            name = it[USER_NAME] ?: "",
            email = it[USER_EMAIL] ?: "",
            photoUrl = it[USER_PHOTO] ?: ""
        )
    }

    suspend fun saveData(user: User) {
        context.dataStore.edit {
            it[USER_NAME] = user.name
            it[USER_EMAIL] = user.email
            it[USER_PHOTO] = user.photoUrl
        }
    }

}