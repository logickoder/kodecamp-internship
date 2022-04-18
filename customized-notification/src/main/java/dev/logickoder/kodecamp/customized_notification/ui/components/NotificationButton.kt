package dev.logickoder.kodecamp.customized_notification.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.logickoder.kodecamp.customized_notification.R
import dev.logickoder.kodecamp.customized_notification.createNotification

@Composable
fun NotificationButton(modifier: Modifier = Modifier) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    val context = LocalContext.current
    Button(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large),
        onClick = { context.createNotification() }
    ) {
        Text(
            text = stringResource(id = R.string.button_text)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(id = R.string.subtitle),
        style = MaterialTheme.typography.caption
    )
}