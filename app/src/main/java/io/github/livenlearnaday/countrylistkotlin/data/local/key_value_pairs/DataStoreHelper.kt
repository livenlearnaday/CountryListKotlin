package io.github.livenlearnaday.countrylistkotlin.data.local.key_value_pairs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.livenlearnaday.countrylistkotlin.data.local.key_value_pairs.DataStoreHelper.PreferencesKeys.COUNTRIES_LOADED_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("settings")

@Singleton
class DataStoreHelper @Inject constructor(@ApplicationContext appContext: Context) {

    private val settingsDataStore = appContext.dataStore

    private object PreferencesKeys {
        val COUNTRIES_LOADED_KEY = booleanPreferencesKey("loaded")
    }

    suspend fun setCountryLoaded(loaded: Boolean) {
        settingsDataStore.edit { settings ->
            settings[COUNTRIES_LOADED_KEY] = loaded
        }
    }

    val countryLoadedFlow: Flow<Boolean?> = settingsDataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
        preferences[COUNTRIES_LOADED_KEY] ?: false
    }




}