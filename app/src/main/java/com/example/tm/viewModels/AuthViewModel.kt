package com.example.tm.viewModels

import TokenManager
import android.util.Log
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tm.retrofit.group.GroupService
import com.example.tm.retrofit.task.InviteGroup
import com.example.tm.retrofit.users.AuthRequest
import com.example.tm.retrofit.users.LinkBot
import com.example.tm.retrofit.users.RegRequest
import com.example.tm.retrofit.users.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel : ViewModel() {
    val loginState = MutableLiveData<LoginState>()
    private val _linkBot = MutableStateFlow(LinkBot(""))
    val linkBot = _linkBot.asStateFlow()

    init {
        getLink()
    }
    fun login(authRequest: AuthRequest) {
        loginState.value = LoginState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = UserService.userApi.auth(authRequest)
                if (response.isSuccessful) {
                    response.body()?.token?.let { token ->
                        TokenManager.token = token
                        loginState.postValue(LoginState.Success)
                    } ?: loginState.postValue(LoginState.Error("No token received"))
                } else {
                    loginState.postValue(LoginState.Error("Authentication failed"))
                }
            } catch (e: Exception) {
                loginState.postValue(LoginState.Error("Network error: ${e.localizedMessage}"))
            }
        }
    }

    fun registration(regRequest: RegRequest) {
        loginState.value = LoginState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = UserService.userApi.reg(regRequest)
                if (response.isSuccessful) {
                    response.body()?.token?.let { token ->
                        TokenManager.token = token
                        loginState.postValue(LoginState.Success)
                    } ?: loginState.postValue(LoginState.Error("No token received"))
                } else {
                    loginState.postValue(LoginState.Error("Authentication failed"))
                }
            } catch (e: Exception) {
                loginState.postValue(LoginState.Error("Network error: ${e.localizedMessage}"))
            }
        }
    }

    fun autoLogin() {
        val token = TokenManager.token
        if (token != null && token.isNotEmpty()) {
            loginState.postValue(LoginState.ValidatingToken)
            validate(token)
        } else {
            loginState.postValue(LoginState.LoggedOut)
        }
    }


    fun validate(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = UserService.userApi.validateToken(token)
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.message == "Токен валиден") {
                        loginState.postValue(LoginState.Success)
                    } else {
                        loginState.postValue(
                            LoginState.Error(
                                apiResponse?.message ?: "Unknown error"
                            )
                        )
                    }
                } else {
                    loginState.postValue(
                        LoginState.Error(
                            "Authentication failed: ${
                                response.errorBody()?.string()
                            }"
                        )
                    )
                }
            } catch (e: Exception) {
                loginState.postValue(LoginState.Error("Network error: ${e.localizedMessage}"))
            }
        }
    }

    fun getLink() {
        viewModelScope.launch {

                val token = TokenManager.token
                val response = UserService.userApi.getLink("Bearer $token")
                if (response.isSuccessful) {
                    Log.d("test", "успешно")
                    _linkBot.value = response.body()!!
                }
        }
    }



    fun logout() {
        TokenManager.clearToken()
        loginState.value = LoginState.LoggedOut
    }
}

sealed class LoginState {
    object Loading : LoginState()
    object ValidatingToken : LoginState()
    object Success : LoginState()
    object LoggedOut : LoginState()
    data class Error(val message: String) : LoginState()
}