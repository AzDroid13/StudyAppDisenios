package alejandro.ibague.studyapp.scenes.profile

import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.database.entities.UserEntity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ProfileRepository(application)

    fun fetchPersonalProfileInfo(userId: String): LiveData<UserEntity> {
        return repository.getProfileInfo(userId)
    }

    fun fetchPackageOwner(userId: String): LiveData<List<PackageEntity>> {
        return repository.getPackageListOf(userId)
    }
}