package com.sensorscope

import androidx.compose.ui.test.assertIsDisplayed
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
}
