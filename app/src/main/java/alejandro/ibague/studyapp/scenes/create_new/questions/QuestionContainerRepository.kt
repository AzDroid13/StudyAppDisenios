package alejandro.ibague.studyapp.scenes.create_new.questions

import alejandro.ibague.studyapp.database.AppDatabase
import alejandro.ibague.studyapp.database.daos.QuestionDao
import alejandro.ibague.studyapp.database.entities.QuestionEntity
import android.app.Application
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuestionContainerRepository(application: Application) {
    private val questionDao: QuestionDao
    private var databaseRef = FirebaseDatabase.getInstance().reference

    init {
        val db = AppDatabase.getDatabase(application)
        questionDao = db.questionDao()
    }

    fun insertNewQuestion(questionEntity: QuestionEntity) {
        Thread {
            questionDao.insertAll(questionEntity)
        }.start()
    }

    fun persistAllPackageQuestion(packageId: String) {
        questionDao.fetchQuestionListBy(packageId).observeForever { questionEntityList ->
            databaseRef.child("questions").child(packageId).setValue(questionEntityList)
        }
    }

    fun syncQuestionOf(packageId: String) {
        databaseRef.child("questions").child(packageId).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val questionList = p0.value as? ArrayList<HashMap<String, Any>>

                if (null != questionList) {
                    Thread {
                        questionDao.removeAll()
                        for (questionItem in questionList) {
                            val questionObj = questionItem.toMap()
                            insertNewQuestion(QuestionEntity.mapToObject(questionObj))
                        }
                    }.start()
                }
            }

            override fun onCancelled(p0: DatabaseError) { }
        })
    }

    fun fetchQuestion(packageId: String): LiveData<List<QuestionEntity>> {
        return questionDao.fetchQuestionListBy(packageId)
    }

    fun updateSandboxQuestion(newPackageId: String){
        questionDao.updateSandboxQuestionsWith(newPackageId)
    }
}