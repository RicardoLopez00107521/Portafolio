package com.avanzo.avanzotestsimulator.ui.tests

sealed class TestUiStatus {
    object Resume : TestUiStatus()

    class Error(val exception: Exception) : TestUiStatus()

    data class ErrorWithMessage(val message: String) : TestUiStatus()

    data class Success(val data: String): TestUiStatus()
}