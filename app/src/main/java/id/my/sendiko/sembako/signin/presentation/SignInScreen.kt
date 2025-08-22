package id.my.sendiko.sembako.signin.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sendiko.content_box_with_notification.ContentBoxWithNotification
import id.my.sendiko.sembako.R
import id.my.sendiko.sembako.core.ui.theme.SembakoProTheme
import id.my.sendiko.sembako.signin.presentation.components.GoogleButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignInScreen(
    state: SignInState,
    onEvent: (SignInEvent) -> Unit,
    onNavigate: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.isSuccess) {
        delay(1500)
        if (state.isSuccess) {
            onNavigate()
        }
    }

    LaunchedEffect(state.isError) {
        Log.e("FirebaseAuth", "SignInScreen: ${state.message}")
        delay(5000)
        if (state.isError) {
            onEvent(SignInEvent.OnClearState)
        }
    }

    ContentBoxWithNotification(
        isLoading = state.isLoading,
        isErrorNotification = state.isError,
        message = state.message,
        content = {
            Scaffold {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
                ) {
                    Image(
                        modifier = Modifier.size(256.dp),
                        painter = painterResource(R.drawable.sign_up),
                        contentDescription = stringResource(R.string.register)
                    )
                    Text(
                        text = stringResource(R.string.register),
                        style = MaterialTheme.typography.titleLarge
                    )
                    GoogleButton(
                        onClick = {
                            coroutineScope.launch {
                                val result = GoogleAuthUI.interactiveSignIn(context)
                                onEvent(SignInEvent.OnGoogleSignedIn(result))
                            }
                        }
                    )
                }
            }
        }
    )

}

@Preview
@Composable
private fun SignInScreenPrev() {
    SembakoProTheme {
        SignInScreen(
            state = SignInState(),
            onEvent = {},
            onNavigate = {}
        )
    }
}