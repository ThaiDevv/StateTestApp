package com.nhom.statetestapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.nhom.statetestapp.navigation.AppNavigation
import com.nhom.statetestapp.ui.theme.StateTestAppTheme

/**
 * ============================================================
 * MainActivity – Entry point của app
 * Phụ trách: TV5
 * ============================================================
 * File này KHÔNG cần chỉnh sửa.
 * TV5 đã setup sẵn. Chỉ cần mở app là chạy được.
 * ============================================================
 */
private const val TAG = "STATE_TEST"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Log.d(TAG, "=== MainActivity.onCreate() === " +
                "isRestored=${savedInstanceState != null}")

        setContent {
            StateTestAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "=== MainActivity.onStart() ===")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "=== MainActivity.onResume() ===")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "=== MainActivity.onPause() ===")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "=== MainActivity.onStop() ===")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "=== MainActivity.onSaveInstanceState() === Bundle được lưu!")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "=== MainActivity.onDestroy() ===")
    }
}
