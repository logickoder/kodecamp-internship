package dev.logickoder.kodecamp.customized_notification.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class Action {
    Rotate,
    Translate,
    Scale,
    Fade,
    SkyColor,
    Shower
}

@Composable
fun AnimateActionButtons(
    currentAction: Action?,
    modifier: Modifier = Modifier,
    onActionChange: (Action) -> Unit,
) = Column(modifier = modifier) {
    Action.values().toList().chunked(3).forEach { row ->
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
            row.forEach { action ->
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                    onClick = { onActionChange(action) },
                    enabled = currentAction != action,
                ) {
                    val name = StringBuilder(action.name.length)
                    action.name.forEach { char ->
                        name.append(if (char.isUpperCase()) " $char" else char)
                    }
                    Text(text = name.toString().uppercase())
                }
            }
        }
    }
}