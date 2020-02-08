package alejandro.ibague.studyapp.scenes.board.tabs.myPackages

import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.scenes.profile.ProfileRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyPackagesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MyPackageRepository = MyPackageRepository(application)
    private val profileRepository = ProfileRepository(application)

    lateinit var packageList: LiveData<List<PackageEntity>>

    fun loadData() {
        repository.fetchRemotePackageList()
        packageList = repository.getAllPackage()
        updateAuthorInformation()
    }

    private fun updateAuthorInformation() {
        GlobalScope.launch {
            profileRepository.updateUserLocalDatabase()
        }
    }
}