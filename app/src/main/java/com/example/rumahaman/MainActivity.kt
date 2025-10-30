// MainActivity.kt

package com.example.rumahaman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
// import androidx.compose.material3.MaterialTheme // Hapus import ini
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.rumahaman.navigation.AppNavHost
// import com.example.rumahaman.ui.theme.RumahAmanTheme // Hapus import ini
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Hapus pembungkus RumahAmanTheme
            Surface(modifier = Modifier.fillMaxSize()) {
                AppNavHost( )
            }
        }
    }
}
