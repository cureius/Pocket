package com.cureius.pocket.feature_dashboard.presentation.dashboard.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.*
import com.cureius.pocket.R

@Preview
@Composable
fun AnimationIcon() {
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