package dev.logickoder.kodecamp.customized_notification.ui.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import kotlinx.coroutines.launch

private const val Duration = 1000
typealias Star = Pair<Animatable<Float, AnimationVector1D>, Animatable<Float, AnimationVector1D>>

@Composable
fun StarContainer(
    action: Action?,
    onActionDone: (Action) -> Unit,
    modifier: Modifier = Modifier,
    stars: List<Star>,
    onStarsChange: (List<Star>) -> Unit,
) = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
) {
    val spec = remember {
        tween<Float>(Duration, easing = LinearEasing)
    }
    val scope = rememberCoroutineScope()

    when (action) {
        Action.Rotate -> {
            val startValue = 0f
            val angle = remember { Animatable(startValue) }
            LaunchedEffect(key1 = Unit) {
                angle.animateTo(360f, spec)
                angle.animateTo(startValue, spec)
                onActionDone(action)
            }
            Star(Modifier.rotate(angle.value))
        }
        Action.Translate -> {
            val startValue = 0f
            val offset = remember { Animatable(startValue) }
            LaunchedEffect(key1 = Unit) {
                offset.animateTo(50.dp.value, spec)
                offset.animateTo(startValue, spec)
                onActionDone(action)
            }
            Star(Modifier.offset(offset.value.dp, 0.dp))
        }
        Action.Scale -> {
            val startValue = 1f
            val scale = remember { Animatable(startValue) }
            LaunchedEffect(key1 = Unit) {
                scale.animateTo(3f, spec)
                scale.animateTo(startValue, spec)
                onActionDone(action)
            }
            Star(Modifier.scale(scale.value))
        }
        Action.Fade -> {
            val startValue = 1f
            val fade = remember { Animatable(startValue) }
            LaunchedEffect(key1 = Unit) {
                fade.animateTo(0f, spec)
                fade.animateTo(startValue, spec)
                onActionDone(action)
            }
            Star(Modifier.alpha(fade.value))
        }
        Action.SkyColor -> {
            val startColor = Color.Black
            val color = remember { Animatable(startColor) }
            LaunchedEffect(key1 = Unit) {
                color.animateTo(Color.Green, spec as TweenSpec<Color>)
                color.animateTo(startColor, spec as TweenSpec<Color>)
                onActionDone(action)
            }
            Box(modifier = Modifier
                .fillMaxSize()
                .background(color.value))
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
                                tween(Duration * 3, easing = LinearEasing)
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