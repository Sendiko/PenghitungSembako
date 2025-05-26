package com.github.sendiko.penghitungsembako.splash.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.sendiko.penghitungsembako.R
import com.github.sendiko.penghitungsembako.core.navigation.DashboardDestination
import com.github.sendiko.penghitungsembako.core.navigation.LoginDestination
import com.github.sendiko.penghitungsembako.core.ui.theme.AppTheme
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Suppress("UNCHECKED_CAST")
@Composable
fun SplashScreen(
    state: SplashState,
    onNavigate: (Any) -> Unit,
) {

    LaunchedEffect(state.user) {
        delay(2000)
        if (state.user.username.isBlank()) {
            onNavigate(LoginDestination)
        } else {
            onNavigate(DashboardDestination)
        }
    }

    Scaffold {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(R.string.app_name)
            )
        }
    }

}

@Preview(showSystemUi = true)
@Composable
private fun SplashScreenPrev() {
    AppTheme{
        SplashScreen(
            state = SplashState(),
            onNavigate = {}
        )
    }
}