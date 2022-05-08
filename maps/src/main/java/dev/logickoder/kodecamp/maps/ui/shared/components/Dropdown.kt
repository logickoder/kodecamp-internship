package dev.logickoder.kodecamp.maps.ui.shared.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import dev.logickoder.kodecamp.maps.ui.theme.AppTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> DropdownField(
    currentItem: T,
    items: List<T>,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        content = {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = currentItem.toString(),
                onValueChange = {},
                textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Medium),
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                shape = AppTheme.shapes.medium,
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                content = {
                    items.forEach { item ->
                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                expanded = false
                                onItemClick(item)
                            },
                            content = {
                                Text(text = item.toString())
                            }
                        )
                    }
                }
            )
        }
    )
}
