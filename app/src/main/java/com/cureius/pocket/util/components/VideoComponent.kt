package com.cureius.pocket.util.components

import android.net.Uri
import android.widget.VideoView
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.cureius.pocket.R

@Composable
fun VideoComponent() {
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
