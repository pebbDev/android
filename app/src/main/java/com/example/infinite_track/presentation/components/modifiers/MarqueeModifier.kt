package com.example.infinite_track.presentation.components.modifiers

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.delay

/**
 * A custom modifier that creates a marquee (text scrolling) effect.
 * The animation only runs if the text width exceeds the container width.
 *
 * @param iterations Number of animation iterations. If Int.MAX_VALUE, it's infinite
 * @param delayMillis Initial delay before animation starts (in milliseconds)
 * @param spacerWidth Width of the spacer between the end and start of text (in dp)
 * @param velocityMultiplier Speed multiplier for the animation (higher = faster)
 */
fun Modifier.basicMarquee(
    iterations: Int = Int.MAX_VALUE,
    delayMillis: Int = 1000,
    spacerWidth: Int = 32,
    velocityMultiplier: Float = 1f
) = composed {
    val density = LocalDensity.current
    var textWidth by remember { mutableStateOf(0) }
    var containerWidth by remember { mutableStateOf(0) }

    // Create an animatable for the horizontal offset
    val offsetX = remember { Animatable(0f) }

    // Only start animation if text is wider than container
    if (textWidth > containerWidth && containerWidth > 0) {
        LaunchedEffect(textWidth, containerWidth, iterations) {
            // Initial delay before starting animation
            delay(delayMillis.toLong())

            // Calculate animation duration based on text width
            // Longer text should scroll at the same velocity
            val baseSpeedMillis = 3000 // Base duration in milliseconds
            val durationMillis = (baseSpeedMillis * textWidth / containerWidth / velocityMultiplier).toInt()

            // Define animation specification with keyframes for pauses
            val animationSpec: AnimationSpec<Float> = infiniteRepeatable(
                animation = keyframes {
                    // Start with the text fully visible
                    0f at 0

                    // Pause at the beginning for delayMillis
                    0f at delayMillis

                    // Animate to the position where the text has scrolled completely out of view
                    -textWidth.toFloat() at durationMillis + delayMillis

                    // Pause at the end for delayMillis
                    -textWidth.toFloat() at durationMillis + (delayMillis * 2)
                },
                repeatMode = RepeatMode.Restart
            )

            // Start the animation
            offsetX.animateTo(
                targetValue = -textWidth.toFloat(),
                animationSpec = animationSpec
            )
        }
    } else {
        // Reset the animation if text fits or container width is 0
        LaunchedEffect(textWidth, containerWidth) {
            offsetX.snapTo(0f)
        }
    }

    // Use SubcomposeLayout to measure the text width and container width
    SubcomposeLayout { constraints ->
        // Measure the container with given constraints
        val containerPlaceables = subcompose("container") {
            // Use Box instead of BoxWithConstraints since we don't need the constraint scope
            Box(modifier = Modifier.clipToBounds()) {}
        }

        // Handle the case when containerPlaceables is empty
        if (containerPlaceables.isEmpty()) {
            // Return a valid MeasureResult with minimum constraints
            return@SubcomposeLayout layout(constraints.minWidth, constraints.minHeight) {}
        }

        val containerPlaceable = containerPlaceables.first().measure(constraints)

        // Store the container width
        containerWidth = containerPlaceable.width

        // Measure the content with relaxed width constraint to get its full width
        val contentPlaceables = subcompose("content") {
            this@composed
        }

        // Handle the case when contentPlaceables is empty
        if (contentPlaceables.isEmpty()) {
            // Return a valid MeasureResult with container dimensions
            return@SubcomposeLayout layout(containerPlaceable.width, containerPlaceable.height) {}
        }

        val contentPlaceable = contentPlaceables.first().measure(
            constraints.copy(maxWidth = Int.MAX_VALUE)
        )

        // Store the content (text) width
        textWidth = contentPlaceable.width

        // If text fits, just use the original content without animation
        if (textWidth <= containerWidth || containerWidth == 0) {
            layout(containerPlaceable.width, containerPlaceable.height) {
                contentPlaceable.place(0, 0)
            }
        } else {
            // Otherwise, use the animated content
            layout(containerPlaceable.width, containerPlaceable.height) {
                // Place the content at the animated offset position
                contentPlaceable.place(offsetX.value.toInt(), 0)

                // Place a duplicate of the content right after the first one
                // to create a seamless loop effect
                contentPlaceable.place(offsetX.value.toInt() + textWidth + spacerWidth, 0)
            }
        }
    }

    // Apply clipToBounds to ensure content doesn't overflow
    this.clipToBounds()
}
