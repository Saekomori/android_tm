    package com.example.tm.screens.auth

    import android.util.Patterns
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.material.Text
    import androidx.compose.material3.CircularProgressIndicator
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
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
    import com.example.tm.retrofit.users.AuthRequest
    import com.example.tm.ui.theme.AppTheme
    import com.example.tm.ui.theme.components.ButtonAuth
    import com.example.tm.ui.theme.components.MyOutlinedTextField
    import com.example.tm.ui.theme.components.PasswordOutlinedTextField
    import com.example.tm.viewModels.AuthViewModel
    import com.example.tm.viewModels.LoginState

    @Composable
    fun AuthScreen(
        regGoClick: () -> Unit,
        loginClick: () -> Unit,
        viewModel: AuthViewModel = viewModel()
    ) {
        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var emailError by rememberSaveable { mutableStateOf<String?>(null) }
        var passwordError by rememberSaveable { mutableStateOf<String?>(null) }

        val loginState by viewModel.loginState.observeAsState()

        LaunchedEffect(Unit) {
            viewModel.autoLogin()
        }
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
                    if (it.isBlank()) {
                        emailError = "Поле не может быть пустым"
                    } else if (!isValidEmail(it)) {
                        emailError = "Неправильный формат почты"
                    } else {
                        emailError = null
                    }
                },
                keyboardType = KeyboardType.Email,
                errorMessage = emailError,
                maxLength = 50 // Ограничение длины почты до 50 символов
            )
            PasswordOutlinedTextField(
                text = password,
                placeholder = "Пароль",
                label = "Пароль",
                onTextChanged = {
                    password = it
                    if (it.isBlank()) {
                        passwordError = "Поле не может быть пустым"
                    } else if (it.length > 20) {
                        passwordError = "Пароль не может быть длиннее 20 символов"
                    } else {
                        passwordError = null
                    }
                },
                keyboardType = KeyboardType.Password,
                isPassword = true,
                errorMessage = passwordError,
                maxLength = 20 // Ограничение длины пароля до 20 символов
            )
            ButtonAuth(text = "Войти") {
                if (email.isBlank()) {
                    emailError = "Поле не может быть пустым"
                } else if (!isValidEmail(email)) {
                    emailError = "Неправильный формат почты"
                }
                if (password.isBlank()) {
                    passwordError = "Поле не может быть пустым"
                }
                if (emailError == null && passwordError == null) {
                    viewModel.login(authRequest(email, password))
                }
            }

            ButtonAuth(text = "Регистрация") {
                regGoClick()
            }
            when (loginState) {
                is LoginState.Loading, is LoginState.ValidatingToken -> CircularProgressIndicator()
                is LoginState.Error -> Text(text = (loginState as LoginState.Error).message, color = Color.Red)
                is LoginState.Success -> loginClick()  // Переход на основной экран приложения
                else -> {}
            }
        }
    }
    }
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun authRequest(email: String, password: String): AuthRequest {
        return AuthRequest(email = email, password = password)
    }