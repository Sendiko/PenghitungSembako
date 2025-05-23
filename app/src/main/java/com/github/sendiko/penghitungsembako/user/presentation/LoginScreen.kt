package com.github.sendiko.penghitungsembako.user.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.sendiko.penghitungsembako.R
import com.sendiko.content_box_with_notification.ContentBoxWithNotification
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    onNavigate: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(state.isSigningIn) {
        GoogleAuthUI.signIn(context)
            .onSuccess {
                onEvent(LoginEvent.OnResult(it))
            }
            .onFailure {
                onEvent(LoginEvent.ClearState)
            }
    }

    LaunchedEffect(state.isSignInSuccessful) {
        if (state.isSignInSuccessful)
            onNavigate()
    }

    LaunchedEffect(state.signInError) {
        if (state.signInError.isNotBlank())
            delay(2000)
            onEvent(LoginEvent.ClearState)
    }

    ContentBoxWithNotification(
        message = state.signInError,
        isLoading = state.isSigningIn,
        content = {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.greeting),
                        contentDescription = stringResource(R.string.hello)
                    )
                    Column{
                        Text(
                            text = stringResource(R.string.login_title),
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            enabled = !state.isSigningIn,
                            contentPadding = PaddingValues(vertical = 16.dp),
                            onClick = { onEvent(LoginEvent.OnLoginClicked) }) {
                            Text(
                                text = stringResource(R.string.login),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        },
    )

}

@Preview
@Composable
private fun LoginScreenPrev() {
    LoginScreen(
        state = LoginState(),
        onEvent = {  }
    ) { }
}