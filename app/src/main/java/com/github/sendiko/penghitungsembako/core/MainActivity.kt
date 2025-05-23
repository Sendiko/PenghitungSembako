package com.github.sendiko.penghitungsembako.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.github.sendiko.penghitungsembako.core.navigation.NavGraph
import com.github.sendiko.penghitungsembako.core.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController
                )
            }
        }
    }
}