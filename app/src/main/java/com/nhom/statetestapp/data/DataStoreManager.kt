package com.nhom.statetestapp.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * ============================================================
 * DataStoreManager – Quản lý Preferences DataStore
 *
 * 👤 PHỤ TRÁCH: THÀNH VIÊN 5 (TV5)
 * ============================================================
 *
 * File này đã setup sẵn. TV5 cần:
 * 1. KHÔNG thay đổi cấu trúc file này
 * 2. Sử dụng DataStoreManager trong DataStoreScreen.kt
 * 3. Hiểu rõ: mỗi lần saveName/saveCount/saveChoice ghi ra file
 *    trên internal storage → survive mọi tình huống
 *
 * CÁCH DÙNG trong Composable:
 *   val dsManager = remember { DataStoreManager(context.dataStore) }
 *   val name by dsManager.nameFlow.collectAsState(initial = "")
 *   scope.launch { dsManager.saveName("new value") }
 * ============================================================
 */

private const val TAG = "STATE_TEST"

// Extension property – đảm bảo chỉ có 1 instance DataStore duy nhất
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "state_test_preferences"
)

class DataStoreManager(private val dataStore: DataStore<Preferences>) {

    companion object {
        val NAME_KEY   = stringPreferencesKey("ds_name")
        val COUNT_KEY  = intPreferencesKey("ds_count")
        val CHOICE_KEY = booleanPreferencesKey("ds_choice")
    }

    // =========================================================
    // Read – trả về Flow (reactive, tự cập nhật khi data thay đổi)
    // =========================================================

    val nameFlow: Flow<String> = dataStore.data
        .catch { e ->
            if (e is IOException) {
                Log.e(TAG, "[DataStore] Error reading name", e)
                emit(emptyPreferences())
            } else throw e
        }
        .map { preferences ->
            val value = preferences[NAME_KEY] ?: ""
            Log.d(TAG, "[DataStore] Read name='$value' from disk")
            value
        }

    val countFlow: Flow<Int> = dataStore.data
        .catch { e ->
            if (e is IOException) {
                Log.e(TAG, "[DataStore] Error reading count", e)
                emit(emptyPreferences())
            } else throw e
        }
        .map { preferences ->
            val value = preferences[COUNT_KEY] ?: 0
            Log.d(TAG, "[DataStore] Read count=$value from disk")
            value
        }

    val choiceFlow: Flow<Boolean> = dataStore.data
        .catch { e ->
            if (e is IOException) {
                Log.e(TAG, "[DataStore] Error reading choice", e)
                emit(emptyPreferences())
            } else throw e
        }
        .map { preferences ->
            val value = preferences[CHOICE_KEY] ?: false
            Log.d(TAG, "[DataStore] Read choice=$value from disk")
            value
        }

    // =========================================================
    // Write – suspend function (phải gọi trong coroutine scope)
    // =========================================================

    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
        }
        Log.d(TAG, "[DataStore] Saved name='$name' to disk")
    }

    suspend fun saveCount(count: Int) {
        dataStore.edit { preferences ->
            preferences[COUNT_KEY] = count
        }
        Log.d(TAG, "[DataStore] Saved count=$count to disk")
    }

    suspend fun saveChoice(choice: Boolean) {
        dataStore.edit { preferences ->
            preferences[CHOICE_KEY] = choice
        }
        Log.d(TAG, "[DataStore] Saved choice=$choice to disk")
    }
}
