package sollecitom.compose.desktop.example.components.customizers.text

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.material.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle

interface OutlinedSecureTextFieldCustomizer {

    var modifier: Modifier
    var enabled: Boolean
    var textStyle: TextStyle
    var label: @Composable (() -> Unit)?
    var placeholder: @Composable (() -> Unit)?
    var leadingIcon: @Composable (() -> Unit)?
    var trailingIcon: @Composable (() -> Unit)?
    var isError: Boolean
    var inputTransformation: InputTransformation?
    var keyboardOptions: KeyboardOptions
    var onKeyboardAction: KeyboardActionHandler?
    var shape: Shape
    var colors: TextFieldColors
    var interactionSource: MutableInteractionSource?

    @Composable
    fun field()

    companion object
}