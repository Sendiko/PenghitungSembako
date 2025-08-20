package id.my.sendiko.sembako.signin.presentation

import android.annotation.SuppressLint
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sendiko.content_box_with_notification.ContentBoxWithNotification
import id.my.sendiko.sembako.R
import id.my.sendiko.sembako.core.ui.theme.SembakoProTheme
import id.my.sendiko.sembako.signin.presentation.components.GoogleButton

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignInScreen(
    state: SignInState,
    onEvent: (SignInEvent) -> Unit
) {
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
                            onEvent(SignInEvent.OnSignIn)
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
            onEvent = {}
        )
    }
}