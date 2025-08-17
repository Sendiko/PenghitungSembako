package id.my.sendiko.sembako.onboarding.presentation

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.my.sendiko.sembako.R
import id.my.sendiko.sembako.core.ui.theme.SembakoProTheme

@Composable
fun OnboardingScreen(
    state: OnboardingState,
    onEvent: (OnboardingEvent) -> Unit,
    onNavigate: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedContent(
            targetState = state.stage,
            transitionSpec = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
                    .togetherWith(slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left))
            }
        ) { stage ->
            when (stage) {
                1 -> ScreenOne(onNext = { onEvent(OnboardingEvent.OnNextClicked) })
                2 -> ScreenTwo(onNext = { onEvent(OnboardingEvent.OnNextClicked) })
                3 -> ScreenThree(onNext = { onEvent(OnboardingEvent.OnNextClicked) })
            }
        }
    }
}

@Composable
fun ScreenOne(
    onNext: () -> Unit
) {
    Box(
        modifier = Modifier.padding(32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(256.dp),
                painter = painterResource(R.drawable.farmers_market),
                contentDescription = stringResource(R.string.farmers_market)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.onboarding_one_title),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = stringResource(R.string.onboarding_one_content),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
        Button(
            onClick = onNext,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.next))
        }
    }
}

@Composable
fun ScreenTwo(
    onNext: () -> Unit
) {
    Box(
        modifier = Modifier.padding(32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(256.dp),
                painter = painterResource(R.drawable.supermarket_workers),
                contentDescription = stringResource(R.string.supermarket_workers)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.onboarding_two_title),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = stringResource(R.string.onboarding_two_content),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
        Button(
            onClick = onNext,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.next))
        }
    }
}

@Composable
fun ScreenThree(
    onNext: () -> Unit
) {
    Box(
        modifier = Modifier.padding(32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(256.dp),
                painter = painterResource(R.drawable.credit_card),
                contentDescription = stringResource(R.string.credit_card)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.onboarding_three_title),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = stringResource(R.string.onboarding_three_content),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
        Button(
            onClick = onNext,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.register))
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OnboardingScreenPrev() {
    SembakoProTheme {
        OnboardingScreen(
            state = OnboardingState(),
            onEvent = {},
            onNavigate = {}
        )
    }
}