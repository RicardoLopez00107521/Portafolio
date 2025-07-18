package com.avanzo.avanzotestsimulator.ui.register.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.avanzo.avanzotestsimulator.AvanzoReviewerApplication
import com.avanzo.avanzotestsimulator.network.ApiResponse
import com.avanzo.avanzotestsimulator.repositories.CredentialsRepository
import com.avanzo.avanzotestsimulator.ui.register.RegisterUiStatus
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: CredentialsRepository) : ViewModel() {
    var firstname = MutableLiveData("")
    var lastname = MutableLiveData("")
    var role = MutableLiveData("student")
    var email = MutableLiveData("")
    var password = MutableLiveData("")
    var password_confirmation = MutableLiveData("")

    var acceptTerms = MutableLiveData(false)

     private val _status = MutableLiveData<RegisterUiStatus>(RegisterUiStatus.Resume)
    val status: LiveData<RegisterUiStatus>
        get() = _status

        private fun register(firstname: String, lastname: String, role: String, email: String, password: String, password_confirmation: String) {
        viewModelScope.launch {
            _status.postValue(
                when (val response = repository.register(firstname, lastname, role, email, password, password_confirmation)) {
                    is ApiResponse.Error -> RegisterUiStatus.Error(response.exception)
                    is ApiResponse.ErrorWithMessage -> RegisterUiStatus.ErrorWithMessage(response.message)
                    is ApiResponse.Success -> RegisterUiStatus.Success
                }
            )
        }
    }

    fun onRegister() {
        if (!validateData()) {
            _status.value = RegisterUiStatus.ErrorWithMessage("Completa todos los campos")
            return
        }

        if (!validatePassword()) {
            _status.value = RegisterUiStatus.ErrorWithMessage("Las contraseñas no coinciden")
            return
        }

        if (!acceptTerms.value!!) {
            _status.value = RegisterUiStatus.ErrorWithMessage("Debe llenar los términos y condiciones")
            return
        }

        register(firstname.value!!, lastname.value!!, role.value!!, email.value!!, password.value!!, password_confirmation.value!!)
    }

    private fun validateData(): Boolean {
        when {
            firstname.value.isNullOrEmpty() -> return false
            lastname.value.isNullOrEmpty() -> return false
            role.value.isNullOrEmpty() -> return false
            email.value.isNullOrEmpty() -> return false
            password.value.isNullOrEmpty() -> return false
            password_confirmation.value.isNullOrEmpty() -> return false
        }

        // Validar que el checkbox esté marcado


        return true
    }

    private fun validatePassword(): Boolean {
        when {
            password.value != password_confirmation.value -> return false
        }
        return true
    }

    fun clearStatus() {
        _status.value = RegisterUiStatus.Resume
    }

    fun clearData() {
        firstname.value = ""
        lastname.value = ""
        email.value = ""
        password.value = ""
        password_confirmation.value = ""
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as AvanzoReviewerApplication
                RegisterViewModel(app.credentialsRepository)
            }
        }
    }
}