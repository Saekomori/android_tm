package com.example.tm.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tm.retrofit.users.RegRequest
import com.example.tm.ui.theme.AppTheme
import com.example.tm.ui.theme.components.ButtonAuth
import com.example.tm.ui.theme.components.MyOutlinedTextField
import com.example.tm.ui.theme.components.PasswordOutlinedTextField
import com.example.tm.viewModels.AuthViewModel
import com.example.tm.viewModels.LoginState


@Composable
fun RegScreen(
    authGo: () -> Unit,
    tasksGo: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordRepeat by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }

    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordRepeatError by rememberSaveable { mutableStateOf<String?>(null) }
    var usernameError by rememberSaveable { mutableStateOf<String?>(null) }

    val loginState by viewModel.loginState.observeAsState()
    AppTheme {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MyOutlinedTextField(
            text = email,
            placeholder = "Почта",
            label = "Почта",
            onTextChanged = {
                email = it
                emailError = if (it.isBlank()) "Поле не может быть пустым"
                else if (!isValidEmail(it)) "Неправильный формат почты"
                else null
            },
            keyboardType = KeyboardType.Email,
            errorMessage = emailError,
            maxLength = 50 // Ограничение длины почты до 50 символов
        )
        MyOutlinedTextField(
            text = username,
            placeholder = "Имя пользователя",
            label = "Имя пользователя",
            onTextChanged = {
                username = it
                usernameError = if (it.isBlank()) "Поле не может быть пустым"
                else null
            },
            keyboardType = KeyboardType.Text,
            errorMessage = usernameError,
            maxLength = 20 // Ограничение длины имени пользователя до 20 символов
        )
        PasswordOutlinedTextField(
            text = password,
            placeholder = "Пароль",
            label = "Пароль",
            onTextChanged = {
                password = it
                passwordError = if (it.isBlank()) "Поле не может быть пустым"
                else if (it.length > 20) "Пароль не может быть длиннее 20 символов"
                else null
            },
            keyboardType = KeyboardType.Password,
            errorMessage = passwordError,
            maxLength = 20 // Ограничение длины пароля до 20 символов
        )
        PasswordOutlinedTextField(
            text = passwordRepeat,
            placeholder = "Повторите пароль",
            label = "Повторите пароль",
            onTextChanged = {
                passwordRepeat = it
                passwordRepeatError = if (it != password) "Пароли не совпадают"
                else null
            },
            keyboardType = KeyboardType.Password,
            errorMessage = passwordRepeatError,
            maxLength = 20 // Ограничение длины повтора пароля до 20 символов
        )
        ButtonAuth(text = "Зарегистрироваться") {
            if (email.isBlank()) {
                emailError = "Поле не может быть пустым"
            } else if (!isValidEmail(email)) {
                emailError = "Неправильный формат почты"
            }
            if (username.isBlank()) {
                usernameError = "Поле не может быть пустым"
            } else if (username.length < 4) {
                usernameError = "Поле не может быть меньше 4"
            }
            if (password.isBlank()) {
                passwordError = "Поле не может быть пустым"
            } else if (password.length > 20) {
                passwordError = "Пароль не может быть длиннее 20 символов"
            }
            if (passwordRepeat.isBlank()) {
                passwordRepeatError = "Поле не может быть пустым"
            } else if (passwordRepeat != password) {
                passwordRepeatError = "Пароли не совпадают"
            }

            if (emailError == null && usernameError == null && passwordError == null && passwordRepeatError == null) {
                viewModel.registration(regRequest(email, password, username))
            }
        }
        ButtonAuth(text = "Назад") {
            authGo()
        }
        when (loginState) {
            is LoginState.Loading, is LoginState.ValidatingToken -> CircularProgressIndicator()
            is LoginState.Error -> Text(text = (loginState as LoginState.Error).message, color = Color.Red)
            is LoginState.Success -> tasksGo()
            else -> {}
        }
    }
}}

fun regRequest(email: String, password: String, username: String): RegRequest {
    return RegRequest(email = email, password = password, username = username)
}

