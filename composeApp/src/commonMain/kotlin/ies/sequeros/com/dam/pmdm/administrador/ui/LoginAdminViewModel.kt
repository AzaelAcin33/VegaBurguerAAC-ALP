package ies.sequeros.com.dam.pmdm.administrador.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginAdministradorViewModel : ViewModel() {
    //Para la comprobación de el usuario y la contraseña
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun validateLogin(user: String, pass: String) {
        if (user == "mate" && pass == "mate") {
            _loginSuccess.value = true
            _errorMessage.value = null
        } else {
            _errorMessage.value = "Usuario o contraseña incorrectos"
        }
    }

    fun resetState() {
        _loginSuccess.value = false
        _errorMessage.value = null
    }
}
