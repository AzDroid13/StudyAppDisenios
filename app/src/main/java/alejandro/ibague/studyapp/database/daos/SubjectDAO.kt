package alejandro.ibague.studyapp.database.daos

import alejandro.ibague.studyapp.database.entities.SubjectEntity
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SubjectDAO {
    @Query("SELECT * FROM subject_entity")
    fun getAll(): LiveData<List<SubjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg subjectEntity: SubjectEntity): LongArray

    @Query("""
           SELECT * FROM subject_entity 
           WHERE subject_entity.user_id=:userId
           """)
    fun findSubjectByUser(userId: String?): LiveData<List<SubjectEntity>>

    @Query("SELECT * FROM subject_entity WHERE subject_entity.identifier=:subjectId")
    fun findInformationOf(subjectId: String): LiveData<SubjectEntity>

    @Query("DELETE FROM package_entity")
    fun removeAll()

    @Query("SELECT * FROM subject_entity WHERE subject_entity.name =:nameSubject AND subject_entity.user_id=:userId")
    fun verifySubjectName(nameSubject: String, userId: String?): LiveData<List<SubjectEntity>>
}