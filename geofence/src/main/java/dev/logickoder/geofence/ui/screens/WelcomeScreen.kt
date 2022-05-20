package dev.logickoder.geofence.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import dev.logickoder.geofence.R
import dev.logickoder.geofence.ui.theme.Padding

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onStart: () -> Unit,
) {
    val texts = stringArrayResource(id = R.array.welcome_text)
    var page by remember { mutableStateOf(0) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            Text(
                text = texts[page],
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(Padding))

            Button(
                onClick = {
                    if (page == texts.lastIndex) onStart() else ++page
                },
                content = {
                    Text(stringResource(if (page < texts.lastIndex) R.string.next else R.string.start))
                }
            )
        }
    )
}
