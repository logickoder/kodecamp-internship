package dev.logickoder.kodecamp.customized_notification.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private const val Duration = 1000
typealias Star = Pair<Animatable<Float, AnimationVector1D>, Animatable<Float, AnimationVector1D>>

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun StarContainer(
    action: Action?,
    modifier: Modifier = Modifier,
    onColorChange: (Color) -> Unit,
    stars: List<Star>,
    onStarsChange: (List<Star>) -> Unit,
) = BoxWithConstraints(
    modifier = modifier,
    contentAlignment = Alignment.Center,
) {
    when(action) {
        Action.Rotate -> {
            val angle = remember { Animatable(0f) }
            LaunchedEffect(key1 = Unit) {
                delay(Duration.toLong())
                angle.animateTo(360f)
            }
            Star(Modifier.rotate(angle.value))
        }
        Action.Translate -> {
            val offset = remember { Animatable(0f) }
            LaunchedEffect(key1 = Unit) {
                delay(Duration.toLong())
                offset.animateTo(25.dp.value)
            }
            Star(Modifier.offset(offset.value.dp, 0.dp))
        }
        Action.Scale -> {
            val scale = remember { Animatable(1f) }
            LaunchedEffect(key1 = Unit) {
                delay(Duration.toLong())
                scale.animateTo(3f)
            }
            Star(Modifier.scale(scale.value))
        }
        Action.Fade -> {
            val fade = remember { Animatable(1f) }
            LaunchedEffect(key1 = Unit) {
                delay(Duration.toLong())
                fade.animateTo(0f)
            }
            Star(Modifier.alpha(fade.value))
        }
        Action.SkyColor -> {
            val color = remember { Animatable(0f) }
            LaunchedEffect(key1 = Unit) {
                delay(Duration.toLong())
                color.animateTo(1f)
                delay(Duration.toLong())
                color.animateTo(2f)
            }
            onColorChange(
                when(color.value) {
                    0f -> Color.Black
                    1f -> Color.Green
                    else -> Color.Gray
                }
            )
            Star()
        }
        Action.Shower -> {
            LaunchedEffect(Unit) {
                stars.forEach {
                    it.second.animateTo(10.dp.value)
                }
                delay(Duration.toLong())
            }
            stars.forEach { loc ->
                Star(modifier = Modifier.absoluteOffset(loc.first.value.dp, loc.second.value.dp))
            }
            val containedStars = stars.filter { it.second.value < maxHeight.value }
            if (containedStars.size < stars.size) {
                onStarsChange(containedStars)
            }
            Star()
        }
        null -> {
            Star()
        }
    }
}