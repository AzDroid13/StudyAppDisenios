package alejandro.ibague.studyapp.scenes.create_new.subjects

import alejandro.ibague.studyapp.database.AppDatabase
import alejandro.ibague.studyapp.database.AppDatabase_Impl
import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.database.entities.SubjectEntity
import alejandro.ibague.studyapp.scenes.create_new.packages.PackageCreatorRepository
import alejandro.ibague.studyapp.scenes.create_new.packages.PackageCreatorViewModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SubjectsViewModel(application: Application) : AndroidViewModel(application){
    private val db = AppDatabase.getDatabase(getApplication<Application>().applicationContext)
    private val subjectsRepository = SubjectsRepository(application)
    private val packageRepository = PackageCreatorRepository(application)

    companion object{
        val registrationState = MutableLiveData<String>()
    }

    fun createSubject(subjectEntity: SubjectEntity) {
        GlobalScope.launch {
            db.subjectDAO().insertAll(subjectEntity)
        }
        registrationState.value = subjectsRepository.persistSubject(subjectEntity)
    }

    fun verifyNameSubject(nameSubject: String, userId: String?): LiveData<List<SubjectEntity>> {
        return  subjectsRepository.verifyNameSubject(nameSubject, userId)
    }
}