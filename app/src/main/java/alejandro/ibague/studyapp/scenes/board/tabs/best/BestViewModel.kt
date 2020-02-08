package alejandro.ibague.studyapp.scenes.board.tabs.best

import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.scenes.board.tabs.myPackages.MyPackageRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class BestViewModel(application: Application) : AndroidViewModel(application) {
    private val myPackageRepository = MyPackageRepository(application)
    lateinit var packageList: LiveData<List<PackageEntity>>

    fun loadData() {
        myPackageRepository.fetchRemotePackageList()
        packageList = myPackageRepository.getAllPackage()
    }

    fun loadDataUserIdPublic(userId: String?) {
        myPackageRepository.fetchRemotePackageList()
        packageList = myPackageRepository.getAllPackageUserIdPuc(userId)
    }

}