package alejandro.ibague.studyapp.scenes.question_inbox

import alejandro.ibague.studyapp.database.entities.QuestionEntity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class QuestionInboxViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = QuestionInboxRepository(application)

    fun fetchQuestionOfPackage(packageId: String): LiveData<List<QuestionEntity>> {
        return repository.getQuestionOfPackage(packageId)
    }

    fun removeQuestionId(questionEntity: QuestionEntity, position: Int) {
        return repository.removeQuestion(questionEntity.uid, questionEntity.packageId!!, position)
    }

}
