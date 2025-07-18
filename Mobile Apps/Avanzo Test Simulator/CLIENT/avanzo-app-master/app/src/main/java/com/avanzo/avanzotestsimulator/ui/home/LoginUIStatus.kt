package com.avanzo.avanzotestsimulator.ui.home

sealed class LoginUIStatus{
    object Resume : LoginUIStatus()
    class Error(val exception: Exception) : LoginUIStatus()
    data class ErrorWithMessage(val message: String) : LoginUIStatus()
    data class Success(val token: String) : LoginUIStatus()
}
