import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import sollecitom.compose.desktop.example.components.Button
import sollecitom.compose.desktop.example.components.LoginForm
import sollecitom.compose.desktop.example.to_move.Credentials

@Composable
@Preview
fun LoginPage() {
    MaterialTheme {
        val loginDetails by remember { mutableStateOf(LoginForm.ViewModel()) }
        var errorMessage by remember { mutableStateOf("") }
        var loginResult by remember { mutableStateOf<String?>(null) }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LoginForm(viewModel = loginDetails) {
                username.apply {
                    modifier = Modifier.fillMaxWidth(0.5f).padding(bottom = 16.dp)
                }
                password.apply {
                    modifier = Modifier.fillMaxWidth(0.5f).padding(bottom = 16.dp)
                }
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            if (loginResult != null) {
                Text(
                    text = loginResult!!,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Button(
                onClick = {
                    if (!loginDetails.isFullyPopulated()) {
                        errorMessage = "Please fill in all fields"
                        loginResult = null
                    } else {
                        errorMessage = ""
                        // Launch coroutine to call suspend function
                        loginResult = performLogin(loginDetails.credentials()).getOrElse { "Login failed: ${it.message}" }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(48.dp)
            ) {
                Text("Log In")
            }
        }
    }
}

// Hypothetical suspend function for login (e.g., network call)
suspend fun performLogin(credentials: Credentials): Result<String> {
    // Simulate network delay
    delay(1000)
    return if (credentials.usernameOrEmailAddress.isNotBlank() && credentials.password.isNotBlank()) {
        Result.success("Login successful for ${credentials.usernameOrEmailAddress}")
    } else {
        Result.failure(Exception("Invalid credentials"))
    }
}