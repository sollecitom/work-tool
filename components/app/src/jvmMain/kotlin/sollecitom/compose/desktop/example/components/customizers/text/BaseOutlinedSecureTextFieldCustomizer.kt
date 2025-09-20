package sollecitom.compose.desktop.example.components.customizers.text

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedSecureTextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import javax.swing.FocusManager

open class BaseOutlinedSecureTextFieldCustomizer(
    private val state: TextFieldState,
) : OutlinedSecureTextFieldCustomizer {

    override var modifier: Modifier = Modifier.Companion
    override var enabled: Boolean = true
    override lateinit var textStyle: TextStyle
    override var label: @Composable (() -> Unit)? = null
    override var placeholder: @Composable (() -> Unit)? = null
    override var leadingIcon: @Composable (() -> Unit)? = null
    override var trailingIcon: @Composable (() -> Unit)? = null
    override var isError: Boolean = false
    override var inputTransformation: InputTransformation? = null
    override var keyboardOptions: KeyboardOptions = KeyboardOptions.Companion.Default
    override var onKeyboardAction: KeyboardActionHandler? = null
    override lateinit var shape: Shape
    override lateinit var colors: TextFieldColors
    override var interactionSource: MutableInteractionSource? = null

    @Composable
    open fun initialiseDefaults() {
        textStyle = LocalTextStyle.current
        shape = TextFieldDefaults.OutlinedTextFieldShape
        colors = TextFieldDefaults.outlinedTextFieldColors()
    }

    @Composable
    final override fun field() {
        OutlinedSecureTextField(
            state = state,
            modifier = modifier,
            enabled = enabled,
            textStyle = textStyle,
            label = label ?: { Text("Password") },
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            inputTransformation = inputTransformation,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            shape = shape,
            colors = colors,
            interactionSource = interactionSource
        )
    }
}