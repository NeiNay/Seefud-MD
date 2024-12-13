package com.seefud.seefud.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.vendorDataStore: DataStore<Preferences> by preferencesDataStore(name = "vendor_data")

class VendorPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveVendor(vendor: VendorModel) {
        dataStore.edit { preferences ->
            preferences[STORE_NAME_KEY] = vendor.store_name
            preferences[DESCRIPTION_KEY] = vendor.description
            preferences[LOCATION_KEY] = vendor.location
            preferences[RATING_KEY] = vendor.rating
            preferences[IS_VERIFIED_KEY] = vendor.is_verified
        }
    }

    fun getVendor(): Flow<VendorModel> {
        return dataStore.data.map { preferences ->
            VendorModel(
                id = preferences[VENDOR_ID_KEY] ?: 0,
                store_name = preferences[STORE_NAME_KEY] ?: "",
                description = preferences[DESCRIPTION_KEY] ?: "",
                location = preferences[LOCATION_KEY] ?: "",
                rating = preferences[RATING_KEY] ?: 0,
                is_verified = preferences[IS_VERIFIED_KEY] ?: false
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: VendorPreference? = null

        private val VENDOR_ID_KEY = intPreferencesKey("vendor_id")
        private val STORE_NAME_KEY = stringPreferencesKey("store_name")
        private val DESCRIPTION_KEY = stringPreferencesKey("description")
        private val LOCATION_KEY = stringPreferencesKey("location")
        private val RATING_KEY = intPreferencesKey("rating")
        private val IS_VERIFIED_KEY = booleanPreferencesKey("is_verified")

        fun getInstance(dataStore: DataStore<Preferences>): VendorPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = VendorPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}