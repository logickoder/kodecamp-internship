package dev.logickoder.kodecamp.customized_notification

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import dev.logickoder.kodecamp.customized_notification.ui.components.*
import dev.logickoder.kodecamp.customized_notification.ui.theme.KodeCampTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KodeCampTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MainContent() {
    Column(modifier = Modifier.fillMaxSize()) {
        var action: Action? by remember { mutableStateOf(null) }
        var color by remember { mutableStateOf(Color.Gray) }
        val stars = remember { mutableStateListOf<Star>() }

        BoxWithConstraints {
            AnimateActionButtons(
                currentAction = action,
                modifier = Modifier.fillMaxWidth(),
                onActionChange = {
                    action = it
                    if (it == Action.Shower) {
                        stars.add(
                            Animatable(Math.random().toFloat() * maxWidth.value) to
                                    Animatable(0f)
                        )
                    }
                }
            )
        }
        StarContainer(
            action,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(color),
            onColorChange = { color = it },
            stars = stars,
            onStarsChange = {
                stars.removeIf { true }
                stars.addAll(it)
            }
        )
        NotificationButton(modifier = Modifier.fillMaxWidth())
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KodeCampTheme {
        MainContent()
    }
}