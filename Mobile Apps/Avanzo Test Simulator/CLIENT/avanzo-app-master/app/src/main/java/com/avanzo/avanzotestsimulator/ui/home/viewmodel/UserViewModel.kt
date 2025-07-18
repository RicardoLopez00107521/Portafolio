package com.avanzo.avanzotestsimulator.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.avanzo.avanzotestsimulator.AvanzoReviewerApplication
import com.avanzo.avanzotestsimulator.repositories.UserRepository

class UserViewModel(private val userRepository: UserRepository): ViewModel() {

    suspend fun getUsers() = userRepository.getUsers()

    suspend fun updateUser(
        password: String,
        password_confirmation: String
    ) = userRepository.updateUser(password, password_confirmation)

    suspend fun getLocalUser() = userRepository.getLocalUser()

    companion object {
        val Factory =
            viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AvanzoReviewerApplication
                UserViewModel(app.userRepository)
            }
        }
    }
}