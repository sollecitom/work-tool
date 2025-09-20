package sollecitom.compose.desktop.example.components.customizers.text

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import javax.swing.FocusManager

open class BaseOutlinedTextFieldCustomizer(
    private val state: TextFieldState,
) : OutlinedTextFieldCustomizer {

    override var modifier: Modifier = Modifier.Companion
    override var enabled: Boolean = true
    override var readOnly: Boolean = false
    override lateinit var textStyle: TextStyle
    override var label: @Composable (() -> Unit)? = null
    override var placeholder: @Composable (() -> Unit)? = null
    override var leadingIcon: @Composable (() -> Unit)? = null
    override var trailingIcon: @Composable (() -> Unit)? = null
    override var isError: Boolean = false
    override var inputTransformation: InputTransformation? = null
    override var outputTransformation: OutputTransformation? = null
    override var keyboardOptions: KeyboardOptions = KeyboardOptions.Companion.Default
    override var onKeyboardAction: KeyboardActionHandler? = null
    override var lineLimits: TextFieldLineLimits = TextFieldLineLimits.Companion.Default
    override lateinit var scrollState: ScrollState
    override lateinit var shape: Shape
    override lateinit var colors: TextFieldColors
    override var interactionSource: MutableInteractionSource? = null

    @Composable
    open fun initialiseDefaults() {

        textStyle = LocalTextStyle.current
        scrollState = rememberScrollState()
        shape = TextFieldDefaults.OutlinedTextFieldShape
        colors = TextFieldDefaults.outlinedTextFieldColors()
    }

    @Composable
    final override fun field() {
        OutlinedTextField(
            state = state,
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            label = label ?: { Text("Username or Email") },
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            inputTransformation = inputTransformation,
            outputTransformation = outputTransformation,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            lineLimits = lineLimits,
            scrollState = scrollState,
            shape = shape,
            colors = colors,
            interactionSource = interactionSource
        )
    }
}