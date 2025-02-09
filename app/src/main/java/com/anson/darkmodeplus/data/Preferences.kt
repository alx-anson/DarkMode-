package com.anson.darkmodeplus.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun saveMemory(value: String, memoryNumber: Int) {
        println("Saving memory $memoryNumber with value $value")
        val key = stringPreferencesKey("MEMORY_$memoryNumber")
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun getMemory(memoryNumber: Int): Flow<String?> {
        println("Getting memory $memoryNumber")
        val key = stringPreferencesKey("MEMORY_$memoryNumber")
        return dataStore.data.map { preferences ->
            preferences[key]
        }
    }
}