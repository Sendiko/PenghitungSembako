package id.my.sendiko.sembako.grocery.form.presentation.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import id.my.sendiko.sembako.R

@Composable
fun GroceryImage(
    bitmap: Bitmap? = null,
    imageUrl: String,
    onLaunchImagePicker: () -> Unit,
    contentDescription: String
) {
    var isSquare by remember { mutableStateOf(false) }
    when {
        imageUrl.isNotBlank() && bitmap != null -> {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(156.dp)
                    .clip(RoundedCornerShape(16.dp)),
                bitmap = bitmap.asImageBitmap(),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
            )
        }
        imageUrl.isNotBlank() -> {
            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .then(
                            if (isSquare) {
                                Modifier.aspectRatio(1f)
                            } else Modifier.height(156.dp)
                        ),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl.replace("http", "https"))
                        .crossfade(true)
                        .build(),
                    contentDescription = contentDescription,
                    error = painterResource(R.drawable.baseline_broken_image_24),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier.padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { isSquare = !isSquare }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Fullscreen,
                                contentDescription = stringResource(R.string.full_screen)
                            )
                        }
                        IconButton(
                            onClick = onLaunchImagePicker
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.add_image),
                                contentDescription = stringResource(R.string.add_image)
                            )
                        }
                    }
                }
            }
        }

        bitmap == null -> {
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(156.dp),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    onLaunchImagePicker()
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(R.drawable.add_image),
                        contentDescription = stringResource(R.string.add_image)
                    )
                    Text(text = stringResource(R.string.add_image))
                }
            }
        }

        else -> {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(156.dp)
                    .clip(RoundedCornerShape(16.dp)),
                bitmap = bitmap.asImageBitmap(),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
            )
        }
    }
}