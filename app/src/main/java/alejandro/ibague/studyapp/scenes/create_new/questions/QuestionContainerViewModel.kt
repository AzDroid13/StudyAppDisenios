package alejandro.ibague.studyapp.scenes.create_new.questions

import alejandro.ibague.studyapp.database.entities.QuestionEntity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class QuestionContainerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QuestionContainerRepository = QuestionContainerRepository(application)

    fun insert(questionEntity: QuestionEntity) {
        repository.insertNewQuestion(questionEntity)
    }

    /*fun persistAllPackageQuestion(packageId: String) {
        repository.persistAllPackageQuestion(packageId)
    }*/
}