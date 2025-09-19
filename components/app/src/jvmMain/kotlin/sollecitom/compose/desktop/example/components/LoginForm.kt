package sollecitom.compose.desktop.example.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.OutlinedSecureTextField
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel as ComposeViewModel

object LoginForm {

    class ViewModel(initialUsername: String = "", initialPassword: String = "") : ComposeViewModel() {
        val username = TextFieldState(initialText = initialUsername)
        val password = TextFieldState(initialText = initialPassword)
    }

    @Composable
    operator fun invoke(viewModel: ViewModel, usernameLabel: String = "Username or Email", usernameModifier: Modifier = Modifier, passwordLabel: String = "Password", passwordModifier: Modifier = usernameModifier, columnModifier: Modifier = Modifier) {
        Column(columnModifier) {
            OutlinedTextField(state = viewModel.username, label = { Text(usernameLabel) }, modifier = usernameModifier)
            OutlinedSecureTextField(state = viewModel.password, label = { Text(passwordLabel) }, modifier = passwordModifier)
        }
    }
}