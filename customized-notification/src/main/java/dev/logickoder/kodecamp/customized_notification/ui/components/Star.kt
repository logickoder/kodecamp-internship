package dev.logickoder.kodecamp.customized_notification.ui.components

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import dev.logickoder.kodecamp.customized_notification.R

@Composable
fun Star(modifier: Modifier = Modifier) = Icon(
    painter = painterResource(id = R.drawable.ic_star),
    contentDescription = "star used for animations",
    tint = Color.Yellow,
    modifier = modifier,
)