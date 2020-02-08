package alejandro.ibague.studyapp.database.daos

import alejandro.ibague.studyapp.database.entities.QuestionEntity
import alejandro.ibague.studyapp.infrastucture.commons.Constants
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuestionDao {
    @Query("SELECT * FROM question_entity")
    fun getAll(): LiveData<List<QuestionEntity>>

    @Insert
    fun insertAll(vararg questionEntity: QuestionEntity)

    @Query("SELECT * FROM question_entity WHERE packageId=:packageId")
    fun fetchQuestionListBy(packageId: String): LiveData<List<QuestionEntity>>

    @Query("DELETE FROM question_entity WHERE question_entity.uid=:id")
    fun removeQuestion(id: Int)

    @Query("UPDATE question_entity SET packageId=:packageId WHERE packageId=:oldPackageId")
    fun updateSandboxQuestionsWith(packageId: String, oldPackageId: String = Constants.SANDBOX_PACKAGE_ID)

    @Query("DELETE FROM question_entity")
    fun removeAll()
}