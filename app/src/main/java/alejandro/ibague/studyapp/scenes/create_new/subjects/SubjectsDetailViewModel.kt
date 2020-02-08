package alejandro.ibague.studyapp.scenes.create_new.subjects

import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.database.entities.SubjectEntity
import alejandro.ibague.studyapp.scenes.board.tabs.myPackages.MyPackageRepository
import alejandro.ibague.studyapp.scenes.profile.ProfileRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SubjectsDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MyPackageRepository = MyPackageRepository(application)
    private val subjectRepository: SubjectsRepository = SubjectsRepository(application)
    private val profileRepository = ProfileRepository(application)

    lateinit var packageList: LiveData<List<PackageEntity>>
    lateinit var subjectList: LiveData<List<SubjectEntity>>

    fun loadData() {
        repository.fetchRemotePackageList()
        packageList = repository.getAllPackage()
        updateAuthorInformation()
    }

    fun loadDataInMainView(){
        subjectRepository.fetchRemoteSubjectsList()
        subjectList = subjectRepository.getAllSubjects()
    }

    private fun updateAuthorInformation() {
        GlobalScope.launch {
            profileRepository.updateUserLocalDatabase()
        }
    }

}
