package alejandro.ibague.studyapp.scenes.create_new.packages

import alejandro.ibague.studyapp.database.AppDatabase
import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.scenes.create_new.questions.QuestionContainerRepository
import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PackageCreatorViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(getApplication<Application>().applicationContext)
    private val repository = PackageCreatorRepository(application)
    private val questionRepository = QuestionContainerRepository(application)

    companion object {
        val registrationState = MutableLiveData<String>()
    }

    fun createPackage(packageEntity: PackageEntity) {
        GlobalScope.launch {
            db.packageDao().insertAll(packageEntity)
        }

        registrationState.value = repository.persistPackage(packageEntity)
    }

    fun verifyNamePackage(namePack: String, userId: String?): LiveData<List<PackageEntity>> {
        return  repository.verifyNamePackage(namePack, userId)
    }

    fun updateSandboxQuestionWith(newPackageId: String){
        GlobalScope.launch {
            questionRepository.updateSandboxQuestion(newPackageId)
        }

        questionRepository.persistAllPackageQuestion(newPackageId)
    }

/*
    fun uploadPhoto(file: Uri): MutableLiveData<String> {
        repository.savePackageThumbnail(file)
        return repository.downloadThumbnailUri
    }*/
}
