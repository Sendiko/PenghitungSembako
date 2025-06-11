package id.my.sendiko.sembako.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import id.my.sendiko.sembako.R
import id.my.sendiko.sembako.core.navigation.AboutDestination
import id.my.sendiko.sembako.core.navigation.SplashDestination
import id.my.sendiko.sembako.core.ui.theme.AppTheme
import id.my.sendiko.sembako.core.domain.User
import id.my.sendiko.sembako.profile.presentation.component.LogoutCard
import com.sendiko.content_box_with_notification.ContentBoxWithNotification
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit,
    onNavigate: (Any?) -> Unit
) {

    LaunchedEffect(state.isSignOutSuccessful) {
        if (state.isSignOutSuccessful)
            onNavigate(SplashDestination)
    }

    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage.isNotBlank()) {
            delay(1000)
            onEvent(ProfileEvent.ClearState)
        }
    }

    ContentBoxWithNotification(
        isLoading = state.isLoading,
        message = state.errorMessage,
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = stringResource(R.string.profile))
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    onNavigate(null)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = stringResource(R.string.back)
                                )
                            }
                        },
                    )
                }
            ) { paddingValues ->
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        top = paddingValues.calculateTopPadding() + 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    verticalItemSpacing = 16.dp,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            shape = CircleShape
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(64.dp),
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(state.user?.profileUrl)
                                        .crossfade(true)
                                        .build(),
                                    error = painterResource(R.drawable.baseline_broken_image_24),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = state.user?.username,
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = state.user?.username ?: "",
                                        style = MaterialTheme.typography.headlineSmall,
                                    )
                                    Text(
                                        text = state.user?.email ?: "",
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }
                            }
                        }
                    }
                    item(
                        span = StaggeredGridItemSpan.FullLine
                    ) {
                        Text(
                            text = stringResource(R.string.settings),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                    item {
                        Card(
                            onClick = { onEvent(ProfileEvent.OnThemeChanged(!state.dynamicTheme)) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (state.dynamicTheme)
                                    MaterialTheme.colorScheme.tertiary
                                else MaterialTheme.colorScheme.surfaceContainer,
                                contentColor = if (state.dynamicTheme)
                                    MaterialTheme.colorScheme.onTertiary
                                else MaterialTheme.colorScheme.onSurface,
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Switch(
                                    checked = state.dynamicTheme,
                                    onCheckedChange = { onEvent(ProfileEvent.OnThemeChanged(!state.dynamicTheme)) },
                                    thumbContent = {
                                        if (state.dynamicTheme)
                                            Icon(
                                                imageVector = Icons.Filled.Palette,
                                                contentDescription = stringResource(R.string.dynamic_theme)
                                            )
                                        else Icon(
                                            imageVector = Icons.Filled.Android,
                                            contentDescription = stringResource(R.string.dynamic_theme)
                                        )
                                    }
                                )
                                Text(
                                    text = if (state.dynamicTheme)
                                        stringResource(R.string.dynamic_theme)
                                    else stringResource(R.string.app_theme),
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }
                    }
                    item {
                        Card(
                            onClick = {
                                onNavigate(AboutDestination)
                            }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Icon(
                                    modifier = Modifier.size(48.dp),
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = stringResource(R.string.about)
                                )
                                Text(
                                    text = stringResource(R.string.about),
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }
                    }
                    item {
                        LogoutCard(
                            onLogout = { onEvent(ProfileEvent.OnLogoutClicked) },
                            text = stringResource(R.string.logout),
                        ) {
                            Icon(
                                modifier = Modifier.size(48.dp),
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = stringResource(R.string.logout)
                            )
                        }
                    }
                }
            }
        }
    )

}

@Preview(showSystemUi = true)
@Composable
private fun ProfileScreenPrev() {
    AppTheme {
        ProfileScreen(
            state = ProfileState(
                user = User(
                    id = 0,
                    username = "Sendiko",
                    email = "sarangtawon897@gmail.com",
                    profileUrl = "https://images.pexels.com/photos/31995895/pexels-photo-31995895/free-photo-of-turkish-coffee-with-scenic-bursa-view.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                )
            ),
            onEvent = {},
            onNavigate = {}
        )
    }
}