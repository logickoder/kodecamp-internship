package dev.logickoder.kodecamp.customized_notification

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KodeCampTheme {
        MainContent()
    }
}