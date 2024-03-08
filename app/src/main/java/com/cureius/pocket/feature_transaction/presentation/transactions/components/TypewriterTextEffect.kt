package com.cureius.pocket.feature_transaction.presentation.transactions.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

/**
 * An example composable that uses the TypewriterTextEffect function.
 */
@Preview
@Composable
fun ExampleTypewriterTextEffect() {
    // Use the TypewriterTextEffect composable with a Text composable to display the texts with the typewriter effect
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TypewriterTextEffect(
            texts = listOf(
                "Lorem ipsum dolor sit amet",
                "consectetur adipiscing elit",
                "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam",
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident",
                "sunt in culpa qui officia deserunt mollit anim id est laborum."
            )
        ) { displayedText ->
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = displayedText,
                style = MaterialTheme.typography.h5
            )
        }
    }
}