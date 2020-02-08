package alejandro.ibague.studyapp.scenes.question_inbox

import alejandro.ibague.studyapp.database.AppDatabase
import alejandro.ibague.studyapp.database.daos.QuestionDao
import alejandro.ibague.studyapp.database.entities.QuestionEntity
import android.app.Application
import androidx.lifecycle.LiveData
import com.google.firebase.database.FirebaseDatabase

class QuestionInboxRepository(val application: Application) {
    private val questionDao: QuestionDao
    private var databaseRef = FirebaseDatabase.getInstance().reference

    init {
        val database = AppDatabase.getDatabase(application)
        questionDao = database.questionDao()
    }

    fun getQuestionOfPackage(packageId: String): LiveData<List<QuestionEntity>> {
        return questionDao.fetchQuestionListBy(packageId)
    }

    fun removeQuestion(Id: Int, packageId: String, position: Int) {
        Thread {
            questionDao.removeQuestion(Id)
        }.start()

        databaseRef.child("questions").child(packageId).child(position.toString()).removeValue()
    }
}