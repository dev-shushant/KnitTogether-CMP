package dev.shushant.permission.compose

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dev.shushant.permission.PermissionsController

@Composable
actual fun rememberPermissionsControllerFactory(): PermissionsControllerFactory {
    val context = LocalContext.current as Activity
    return remember(context) {
        PermissionsControllerFactory {
            PermissionsController(applicationContext = context)
        }
    }
}
