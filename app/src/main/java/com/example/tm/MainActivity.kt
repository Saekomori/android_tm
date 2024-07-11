package com.example.tm

import TokenManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.tm.graphs.RootNavigationGraph
import com.example.tm.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        TokenManager.init(this)
        setContent {

            AppTheme {
                RootNavigationGraph(navController = rememberNavController())
            }
        }
    }
}


