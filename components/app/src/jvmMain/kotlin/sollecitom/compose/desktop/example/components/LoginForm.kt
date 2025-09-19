package sollecitom.compose.desktop.example.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.*
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.ViewModel as ComposeViewModel

object LoginForm {

    class ViewModel(initialUsername: String = "", initialPassword: String = "") : ComposeViewModel() {
        val username = TextFieldState(initialText = initialUsername)
        val password = TextFieldState(initialText = initialPassword)
    }

    @Composable
    operator fun invoke(
        viewModel: ViewModel,
        columnModifier: Modifier = Modifier,
        customize: Customizer.() -> Unit = {},
    ) {
        Column(modifier = columnModifier) {
            val customizer = CustomizerImpl(viewModel).also { it.initializeDefaults() }
            customizer.customize()
            customizer.username.field()
            customizer.password.field()
        }
    }

    interface Customizer {
        val username: OutlinedTextFieldCustomizer
        val password: OutlinedSecureTextFieldCustomizer
    }

    private class CustomizerImpl(
        viewModel: ViewModel,
        override val username: OutlinedTextFieldCustomizerImpl = OutlinedTextFieldCustomizerImpl(state = viewModel.username),
        override val password: OutlinedSecureTextFieldCustomizerImpl = OutlinedSecureTextFieldCustomizerImpl(state = viewModel.password)
    ) : Customizer {

        @Composable
        fun initializeDefaults() {
            username.initialiseDefaults()
            password.initialiseDefaults()
        }
    }

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

    private class OutlinedTextFieldCustomizerImpl(
        private val state: TextFieldState,
    ) : OutlinedTextFieldCustomizer {

        override var modifier: Modifier = Modifier
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
        override var keyboardOptions: KeyboardOptions = KeyboardOptions.Default
        override var onKeyboardAction: KeyboardActionHandler? = null
        override var lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default
        override lateinit var scrollState: ScrollState
        override lateinit var shape: Shape
        override lateinit var colors: TextFieldColors
        override var interactionSource: MutableInteractionSource? = null

        @Composable
        fun initialiseDefaults() {

            textStyle = LocalTextStyle.current
            scrollState = rememberScrollState()
            shape = TextFieldDefaults.OutlinedTextFieldShape
            colors = TextFieldDefaults.outlinedTextFieldColors()
        }

        @Composable
        override fun field() {
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

    private class OutlinedSecureTextFieldCustomizerImpl(
        private val state: TextFieldState,
    ) : OutlinedSecureTextFieldCustomizer {

        override var modifier: Modifier = Modifier
        override var enabled: Boolean = true
        override lateinit var textStyle: TextStyle
        override var label: @Composable (() -> Unit)? = null
        override var placeholder: @Composable (() -> Unit)? = null
        override var leadingIcon: @Composable (() -> Unit)? = null
        override var trailingIcon: @Composable (() -> Unit)? = null
        override var isError: Boolean = false
        override var inputTransformation: InputTransformation? = null
        override var keyboardOptions: KeyboardOptions = KeyboardOptions.Default
        override var onKeyboardAction: KeyboardActionHandler? = null
        override lateinit var shape: Shape
        override lateinit var colors: TextFieldColors
        override var interactionSource: MutableInteractionSource? = null

        @Composable
        fun initialiseDefaults() {
            textStyle = LocalTextStyle.current
            shape = TextFieldDefaults.OutlinedTextFieldShape
            colors = TextFieldDefaults.outlinedTextFieldColors()
        }

        @Composable
        override fun field() {
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
}