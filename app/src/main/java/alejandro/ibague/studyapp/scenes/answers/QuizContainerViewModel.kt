package alejandro.ibague.studyapp.scenes.answers

import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.database.entities.QuestionEntity
import alejandro.ibague.studyapp.database.entities.UserEntity
import alejandro.ibague.studyapp.scenes.create_new.questions.QuestionContainerRepository
import alejandro.ibague.studyapp.scenes.profile.ProfileRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuizContainerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = QuestionContainerRepository(application)
    private val quizContainerRepository = QuizContainerRepository(application)
    private val profileRepository = ProfileRepository(application)

    lateinit var questionList: LiveData<List<QuestionEntity>>
    lateinit var packageInformation: LiveData<PackageEntity>

    fun getAllQuestionOf(packageId: String) {
        GlobalScope.run {
            repository.syncQuestionOf(packageId)
        }.also {
            questionList = repository.fetchQuestion(packageId)
        }
    }

    fun getPackageInformation(packageId: String) {
        packageInformation = quizContainerRepository.getInformationOf(packageId)
    }

    fun getAuthorInformation(userId: String): LiveData<UserEntity> {
        return profileRepository.getProfileInfo(userId)
    }
}