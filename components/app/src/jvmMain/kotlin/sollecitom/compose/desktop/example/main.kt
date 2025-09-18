package sollecitom.compose.desktop.example

import LoginPage
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "desktop-compose-example",
    ) {
        LoginPage()
//        App()
    }
}