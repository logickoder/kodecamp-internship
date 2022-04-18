package dev.logickoder.kodecamp.customized_notification.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val Duration = 1000
typealias Star = Pair<Animatable<Float, AnimationVector1D>, Animatable<Float, AnimationVector1D>>

@Composable
fun StarContainer(
    action: Action?,
    modifier: Modifier = Modifier,
    onColorChange: (Color) -> Unit,
    stars: List<Star>,
    onStarsChange: (List<Star>) -> Unit,
) = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
) {
    val scope = rememberCoroutineScope()
    when (action) {
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
            BoxWithConstraints(modifier = Modifier
                .clipToBounds()
                .fillMaxSize()) {
                for (loc in stars) {
                    LaunchedEffect(Unit) {
                        scope.launch {
                            loc.second.animateTo(
                                maxHeight.value,
                                tween(Duration * 4)
                            )
                        }
                    }
                    Star(
                        modifier = Modifier.absoluteOffset(
                            loc.first.value.dp,
                            loc.second.value.dp
                        )
                    )
                }
                val containedStars = stars.filter { it.second.value < maxHeight.value }
                if (containedStars.size < stars.size) {
                    onStarsChange(containedStars)
                }
            }
            Star()
        }
        null -> {
            Star()
        }
    }
}