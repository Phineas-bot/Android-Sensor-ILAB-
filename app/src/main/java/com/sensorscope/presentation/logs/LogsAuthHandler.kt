package com.sensorscope.presentation.logs

import androidx.fragment.app.FragmentActivity
import com.sensorscope.core.security.BiometricAuthenticator

interface LogsBiometricAuthenticator {
    fun canAuthenticate(): Boolean
    fun authenticate(
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )
}

class LogsAuthHandler(
    private val authenticatorProvider: () -> LogsBiometricAuthenticator?
) {
    fun authenticate(
        onSuccess: () -> Unit,
        onMessage: (String) -> Unit
    ) {
        val authenticator = authenticatorProvider() ?: run {
            onMessage("Biometric authentication unavailable")
            return
        }
        if (!authenticator.canAuthenticate()) {
            onMessage("Biometric hardware not available")
            return
        }

        authenticator.authenticate(
            title = "Unlock Sensor Logs",
            subtitle = "Use biometrics to continue",
            onSuccess = onSuccess,
            onFailure = onMessage
        )
    }
}

fun logsAuthHandler(activity: FragmentActivity?): LogsAuthHandler {
    return LogsAuthHandler {
        val host = activity ?: return@LogsAuthHandler null
        object : LogsBiometricAuthenticator {
            private val delegate = BiometricAuthenticator(host)

            override fun canAuthenticate(): Boolean = delegate.canAuthenticate()

            override fun authenticate(
                title: String,
                subtitle: String,
                onSuccess: () -> Unit,
                onFailure: (String) -> Unit
            ) {
                delegate.authenticate(title, subtitle, onSuccess, onFailure)
            }
        }
    }
}