package alejandro.ibague.studyapp.scenes.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val loginRepository: LoginRepository
    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(application.baseContext)
    companion object {
        var userToken: String? = null
    }

    enum class AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED,          // The user has authenticated successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }

    val authenticationState = MutableLiveData<AuthenticationState>()
    private var username: String
    private var password: String

    init {
        // In this example, the user is always unauthenticated when MainActivity is launched
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
        username = ""
        password = ""

        loginRepository = LoginRepository(application)
    }

    fun refuseAuthentication() {
        sharedPref.edit().putString("user_identifier", null).apply()
        sharedPref.edit().putString("userToken", null).apply()
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    fun authenticate(username: String, password: String) {
        if (passwordIsValidForUsername(username, password)) {
            LoginRepository.firstTimeLoginIntent = true
            loginRepository.findUser(username.toLowerCase(), password.toLowerCase())
            loginRepository.userAuthenticated.observeForever {
                 if (it != null) {
                     userToken = it.identifier
                     this.username = username
                     authenticationState.value = AuthenticationState.AUTHENTICATED
                 } else {
                     authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                 }
            }
        } else {
            authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
        }
    }

    fun authenticate(authorizationToken: String) {
        userToken = authorizationToken
        authenticationState.value = AuthenticationState.AUTHENTICATED
    }

    private fun passwordIsValidForUsername(username: String, password: String): Boolean {
        return !username.isBlank() && !password.isBlank()
    }
}