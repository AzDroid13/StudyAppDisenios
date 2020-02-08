package alejandro.ibague.studyapp.scenes.create_new.subjects

import alejandro.ibague.studyapp.database.AppDatabase
import alejandro.ibague.studyapp.database.daos.SubjectDAO
import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.database.entities.SubjectEntity
import android.app.Application
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class SubjectsRepository(application: Application) {
    private val subjectDAO : SubjectDAO
    private var storageRef = FirebaseStorage.getInstance().reference
    private var databaseRef = FirebaseDatabase.getInstance().reference

    init {
        val database = AppDatabase.getDatabase(application)
        subjectDAO = database.subjectDAO()
    }

    @Deprecated(message = "Function for test")
    fun verifyNameSubject(name: String, userId: String?): LiveData<List<SubjectEntity>> {
        return subjectDAO.verifySubjectName(name, userId)
    }

    fun persistSubject(subjectEntity: SubjectEntity): String {
        subjectEntity.identifier = databaseRef.child("subjects").push().key!!
        databaseRef.child("subjects").child(subjectEntity.identifier).setValue(subjectEntity)
        return subjectEntity.identifier
    }

    fun getAllSubjects(): LiveData<List<SubjectEntity>>{
        Thread {
            subjectDAO.removeAll()
        }.start()
        return subjectDAO.getAll()
    }

    fun getAllPackagesByUserId(serId: String?): LiveData<List<SubjectEntity>>{
        Thread {
            subjectDAO.removeAll()
        }.start()
        return subjectDAO.findSubjectByUser(serId)
    }

    fun fetchRemoteSubjectsList(){
        databaseRef.child("subjects").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(d: DataSnapshot) {
                val subjectList = d.value as? HashMap<String, HashMap<String, Any>>
                if (subjectList != null){
                    for (subjectItem in subjectList){
                        val subjectObj = subjectItem.value.toMap()
                        val subjectEntity = SubjectEntity.mapToObject(subjectItem.key, subjectObj)
                        Thread {
                            subjectDAO.insertAll(subjectEntity)
                        }.start()
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) { }
        })
    }

}