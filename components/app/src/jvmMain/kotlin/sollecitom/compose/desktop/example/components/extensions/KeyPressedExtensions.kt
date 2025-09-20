package sollecitom.compose.desktop.example.components.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type

context(focusManager: FocusManager)
fun Modifier.moveFocusWhenKeyIsPressed(key: Key, direction: FocusDirection = FocusDirection.Next) = moveFocusWhenKeyIsPressed(setOf(key), direction)

context(focusManager: FocusManager)
fun Modifier.moveFocusWhenKeyIsPressed(keys: Set<Key>, direction: FocusDirection = FocusDirection.Next): Modifier = onPreviewKeyEvent { keyEvent ->
    if (keyEvent.key in keys && keyEvent.type in listOf(KeyEventType.KeyDown, KeyEventType.KeyUp)) {
        if (keyEvent.type == KeyEventType.KeyDown) {
            focusManager.moveFocus(direction)
        }
        true
    } else {
        false
    }
}