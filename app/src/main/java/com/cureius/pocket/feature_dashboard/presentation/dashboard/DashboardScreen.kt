package com.cureius.pocket.feature_dashboard.presentation.dashboard

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import com.airbnb.lottie.Lottie
import com.airbnb.lottie.compose.*
import com.cureius.pocket.R
import com.cureius.pocket.feature_dashboard.presentation.dashboard.components.DashBoardHeader
import com.cureius.pocket.ui.theme.RedOrange
import kotlinx.coroutines.delay
import org.json.JSONObject
import pl.droidsonroids.gif.GifDrawable

@Composable
fun DashboardScreen(navHostController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .height(280.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            DashBoardHeader(navHostController)
            val shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp,
                bottomStart = 24.dp,
                bottomEnd = 24.dp
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(136.dp)
                    .padding(16.dp, 0.dp, 16.dp, 0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f), shape)
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(104.dp)
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {

                        Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                            Row(modifier = Modifier.padding(8.dp, 8.dp, 4.dp, 12.dp)) {
                                Text(text = "Total Balance")
                                Text(text = "    $ 5,000.00", color = RedOrange)
                            }
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                drawLine(
                                    color = Color.DarkGray,
                                    start = Offset(x = 0f, y = size.height / 2),
                                    end = Offset(x = size.width, y = size.height / 2),
                                    strokeWidth = 8f,
                                    pathEffect = PathEffect.dashPathEffect(
                                        intervals = floatArrayOf(20f, 10f),
                                        phase = 0f
                                    )
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .background(Color.Transparent)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LottieLoaderAnimation()
                        }

                    }

                }
            }
            val wrapperShape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            )
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background, wrapperShape)
                    .fillMaxSize()
            ) {

            }
        }
    }
}


@Composable
fun MyVideoComposable() {
    val context = LocalContext.current
    val videoUri = Uri.parse("android.resource://${context.packageName}/${R.raw.thunder}")

    var isPlaying by remember { mutableStateOf(false) }

    AndroidView(
        factory = { context ->
            VideoView(context).apply {
                setVideoURI(videoUri)
                setOnPreparedListener { mp ->
                    mp.isLooping = true
                    if (isPlaying) {
                        start()
                    }
                }
            }
        },
        update = { view ->
            if (isPlaying) {
                view.start()
            } else {
                view.pause()
            }
        }
    )

    IconButton(onClick = { isPlaying = !isPlaying }) {
        Icon(
            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = if (isPlaying) "Pause" else "Play"
        )
    }
}

@Composable
fun LottieLoaderAnimation() {
    val comositeResult: LottieCompositionResult = rememberLottieComposition(
    spec = LottieCompositionSpec.RawRes(R.raw.refresh)
    )
    val progressAnimation by animateLottieCompositionAsState(
        comositeResult.value,
        isPlaying = true,
        iterations = LottieConstants.IterateForever,
        speed = 1.0f
    )
    LottieAnimation(composition = comositeResult.value, progress = progressAnimation)
}