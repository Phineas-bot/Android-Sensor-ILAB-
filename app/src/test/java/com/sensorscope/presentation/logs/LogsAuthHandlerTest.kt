package com.sensorscope.presentation.logs

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LogsAuthHandlerTest {

    @Test
    fun returnsUnavailableMessageWhenActivityMissing() {
        val handler = LogsAuthHandler { null }
        var message: String? = null

        handler.authenticate(
            onSuccess = {},
            onMessage = { message = it }
        )

        assertEquals("Biometric authentication unavailable", message)
    }

    @Test
    fun returnsHardwareUnavailableWhenAuthenticatorCannotAuthenticate() {
        val handler = LogsAuthHandler { FakeAuthenticator(canAuthenticate = false) }
        var message: String? = null

        handler.authenticate(
            onSuccess = {},
            onMessage = { message = it }
        )

        assertEquals("Biometric hardware not available", message)
    }

    @Test
    fun forwardsFailureMessageFromAuthenticator() {
        val handler = LogsAuthHandler { FakeAuthenticator(failureMessage = "Biometric not recognized.") }
        var message: String? = null

        handler.authenticate(
            onSuccess = {},
            onMessage = { message = it }
        )

        assertEquals("Biometric not recognized.", message)
    }

    @Test
    fun triggersSuccessCallbackWhenAuthenticationSucceeds() {
        val handler = LogsAuthHandler { FakeAuthenticator(shouldSucceed = true) }
        var success = false

        handler.authenticate(
            onSuccess = { success = true },
            onMessage = {}
        )

        assertTrue(success)
    }

    private class FakeAuthenticator(
        private val canAuthenticate: Boolean = true,
        private val shouldSucceed: Boolean = false,
        private val failureMessage: String = "Auth failed"
    ) : LogsBiometricAuthenticator {
        override fun canAuthenticate(): Boolean = canAuthenticate

        override fun authenticate(
            title: String,
            subtitle: String,
            onSuccess: () -> Unit,
            onFailure: (String) -> Unit
        ) {
            if (shouldSucceed) onSuccess() else onFailure(failureMessage)
        }
    }
}
