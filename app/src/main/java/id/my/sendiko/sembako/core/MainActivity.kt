package id.my.sendiko.sembako.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import id.my.sendiko.sembako.core.navigation.DashboardDestination
import id.my.sendiko.sembako.core.navigation.NavGraph
import id.my.sendiko.sembako.core.navigation.OnboardingDestination
import id.my.sendiko.sembako.core.navigation.SignInDestination
import id.my.sendiko.sembako.core.preferences.UserPreferences
import id.my.sendiko.sembako.core.preferences.dataStore
import id.my.sendiko.sembako.core.ui.theme.SembakoProTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
        userPreferences = UserPreferences(this.dataStore)
        var isLoading = true
        lifecycleScope.launch {
            userPreferences.getHasBoarding().first()
            isLoading = false
        }
        splashScreen.setKeepOnScreenCondition { isLoading }
        setContent {
            val dynamicTheme by userPreferences.getDynamicTheme().collectAsStateWithLifecycle(true)
            val hasBoarding by userPreferences.getHasBoarding().collectAsStateWithLifecycle(null)
            val user by userPreferences.getUser().collectAsStateWithLifecycle(null)
            SembakoProTheme(
                dynamicColor = dynamicTheme
            ) {
                if (user != null) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        startDestination = if (user?.id == 0) {
                            if (hasBoarding == true) {
                                SignInDestination
                            } else {
                                OnboardingDestination
                            }
                        } else {
                            DashboardDestination
                        }
                    )
                }
            }
        }
    }
}