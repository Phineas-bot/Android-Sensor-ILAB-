package com.sensorscope

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SensorScopeInstrumentationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun dashboardTabsAreVisible() {
        composeRule.onNodeWithText("Dashboard").assertIsDisplayed()
        composeRule.onNodeWithText("Labs").assertIsDisplayed()
        composeRule.onNodeWithText("Logs").assertIsDisplayed()
    }

    @Test
    fun logsScreenShowsAuthenticationPrompt() {
        composeRule.onNodeWithText("Logs").performClick()
        composeRule.onNodeWithText("Authenticate").assertIsDisplayed()
    }

    @Test
    fun logsScreenShowsProtectedStateMessaging() {
        composeRule.onNodeWithText("Logs").performClick()
        composeRule.onNodeWithText("Logs are protected").assertIsDisplayed()
        composeRule.onNodeWithText("Authenticate to view and export sessions").assertIsDisplayed()
    }

    @Test
    fun dashboardStartStopButtonsToggleEnabledState() {
        composeRule.onNodeWithText("Start").assertIsDisplayed().assertIsEnabled()
        composeRule.onNodeWithText("Stop").assertIsDisplayed().assertIsNotEnabled()

        composeRule.onNodeWithText("Start").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Start").assertIsNotEnabled()
        composeRule.onNodeWithText("Stop").assertIsEnabled()

        composeRule.onNodeWithText("Stop").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Start").assertIsEnabled()
        composeRule.onNodeWithText("Stop").assertIsNotEnabled()
    }

    @Test
    fun dashboardShowsEnvironmentalSensorCards() {
        composeRule.onNodeWithText("Dashboard").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Light").assertIsDisplayed()
        composeRule.onNodeWithText("Proximity").assertIsDisplayed()
        composeRule.onNodeWithText("Pressure").assertIsDisplayed()
        composeRule.onNodeWithText("Audio Level").assertIsDisplayed()
        composeRule.onNodeWithText("Camera Metadata").assertIsDisplayed()
        composeRule.onNodeWithText("Bluetooth Signal").assertIsDisplayed()
    }

    @Test
    fun canOpenDetailsForEachSensorType() {
        val sensors = listOf(
            "Accelerometer",
            "Gyroscope",
            "Magnetometer",
            "Light",
            "Proximity",
            "Pressure",
            "Audio Level",
            "Camera Metadata",
            "Bluetooth Signal"
        )

        sensors.forEach { sensorName ->
            composeRule.onNodeWithText("Dashboard").performClick()
            composeRule.waitForIdle()

            composeRule.onNodeWithText(sensorName).performClick()
            composeRule.waitForIdle()

            composeRule.onNodeWithText("Real-time values").assertIsDisplayed()

            composeRule.activityRule.scenario.onActivity {
                it.onBackPressedDispatcher.onBackPressed()
            }
            composeRule.waitForIdle()
        }
    }
}
