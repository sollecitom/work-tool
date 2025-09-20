package sollecitom.compose.desktop.example.components.customizers.text

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle

interface OutlinedTextFieldCustomizer {

    var modifier: Modifier
    var enabled: Boolean
    var readOnly: Boolean
    var textStyle: TextStyle
    var label: @Composable (() -> Unit)?
    var placeholder: @Composable (() -> Unit)?
    var leadingIcon: @Composable (() -> Unit)?
    var trailingIcon: @Composable (() -> Unit)?
    var isError: Boolean
    var inputTransformation: InputTransformation?
    var outputTransformation: OutputTransformation?
    var keyboardOptions: KeyboardOptions
    var onKeyboardAction: KeyboardActionHandler?
    var lineLimits: TextFieldLineLimits
    var scrollState: ScrollState
    var shape: Shape
    var colors: TextFieldColors
    var interactionSource: MutableInteractionSource?

    @Composable
    fun field()

    companion object
}

