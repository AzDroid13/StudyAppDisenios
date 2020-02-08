package alejandro.ibague.studyapp.scenes.register

import alejandro.ibague.studyapp.database.entities.UserEntity
import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RegistrationRepository(application)
    val registrationState = MutableLiveData<RegistrationState>()

    enum class RegistrationState {
        COLLECT_PROFILE_DATA,
        REGISTRATION_COMPLETED
    }

    var authToken = ""
        private set

    fun createAccountAndLogin(userInformation: UserEntity) {
        if (createAccount(userInformation)) {
            this.authToken = authenticate(userInformation.name, userInformation.password)
        }

        // Change State to registration completed
        registrationState.value = RegistrationState.REGISTRATION_COMPLETED
    }

    private fun createAccount(userInformation: UserEntity): Boolean {
        repository.createNewUser(userInformation)
        return true
    }

    private fun authenticate(userName: String?, password: String?) : String {
        return "ASDFASFASFASFASF" //TODO: Return token from server
    }

    fun userCancelledRegistration() : Boolean {
        // Clear existing registration data
        registrationState.value = RegistrationState.COLLECT_PROFILE_DATA
        authToken = ""
        return true
    }

    fun uploadPhoto(file: Uri): MutableLiveData<String> {
        repository.savePackageThumbnail(file)
        return repository.downloadThumbnailUri
    }
}