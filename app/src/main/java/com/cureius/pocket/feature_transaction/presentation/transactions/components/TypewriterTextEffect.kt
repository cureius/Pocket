package com.cureius.pocket.feature_transaction.presentation.transactions.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun TypewriterTextEffect(
    texts: List<String>,
    minDelayInMillis: Long = 200,
    maxDelayInMillis: Long = 300,
    minCharacterChunk: Int = 1,
    maxCharacterChunk: Int = 2,
    onEffectCompleted: () -> Unit = {},
    displayTextComposable: @Composable (displayedText: String) -> Unit
) {
    // Ensure minDelayInMillis is less than or equal to maxDelayInMillis
    require(minDelayInMillis <= maxDelayInMillis) {
        "TypewriterTextEffect: Invalid delay range. minDelayInMillis ($minDelayInMillis) must be less than or equal to maxDelayInMillis ($maxDelayInMillis)."
    }

    // Ensure minCharacterChunk is less than or equal to maxCharacterChunk
    require(minCharacterChunk <= maxCharacterChunk) {
        "TypewriterTextEffect: Invalid character chunk range. minCharacterChunk ($minCharacterChunk) must be less than or equal to maxCharacterChunk ($maxCharacterChunk)."
    }

    // Initialize and remember the displayedText
    var displayedText by remember { mutableStateOf("") }

    // Call the displayTextComposable with the current displayedText value
    displayTextComposable(displayedText)

    // Launch the effect to update the displayedText value over time
    LaunchedEffect(texts) {
        for (text in texts) {
            val textLength = text.length
            var endIndex = 0
            while (endIndex < textLength) {
                endIndex = minOf(
                    endIndex + Random.nextInt(minCharacterChunk, maxCharacterChunk + 1),
                    textLength
                )
                displayedText = text.substring(startIndex = 0, endIndex = endIndex)
                delay(Random.nextLong(minDelayInMillis, maxDelayInMillis))
            }
            delay(Random.nextLong(minDelayInMillis, maxDelayInMillis))
        }
        onEffectCompleted()
    }
}
