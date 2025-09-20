package sollecitom.compose.desktop.example.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import sollecitom.compose.desktop.example.components.customizers.text.BaseOutlinedSecureTextFieldCustomizer
import sollecitom.compose.desktop.example.components.customizers.text.BaseOutlinedTextFieldCustomizer
import sollecitom.compose.desktop.example.components.customizers.text.OutlinedSecureTextFieldCustomizer
import sollecitom.compose.desktop.example.components.customizers.text.OutlinedTextFieldCustomizer
import sollecitom.compose.desktop.example.components.extensions.moveFocusWhenKeyIsPressed
import sollecitom.compose.desktop.example.to_move.Credentials
import androidx.lifecycle.ViewModel as ComposeViewModel

object LoginForm {

    class ViewModel(initialUsername: String = "", initialPassword: String = "") : ComposeViewModel() {

        internal val username = TextFieldState(initialText = initialUsername)
        internal val password = TextFieldState(initialText = initialPassword)

        fun isFullyPopulated(): Boolean = username.text.isNotBlank() && password.text.isNotBlank()

        fun credentials(): Credentials {

            check(isFullyPopulated()) { "Username or password are blank" }
            return Credentials(username.text, password.text)
        }
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
        override val username: UsernameCustomizer = UsernameCustomizer(state = viewModel.username),
        override val password: PasswordCustomizer = PasswordCustomizer(state = viewModel.password)
    ) : Customizer {

        @Composable
        fun initializeDefaults() {
            username.initialiseDefaults()
            password.initialiseDefaults()
        }
    }

    private class UsernameCustomizer(state: TextFieldState) : BaseOutlinedTextFieldCustomizer(state) {

        @Composable
        override fun initialiseDefaults() {
            super.initialiseDefaults()
            val focusManager = LocalFocusManager.current
            modifier = with(focusManager) { modifier.moveFocusWhenKeyIsPressed(key = Key.Tab) }
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        }
    }

    private class PasswordCustomizer(state: TextFieldState) : BaseOutlinedSecureTextFieldCustomizer(state)
}