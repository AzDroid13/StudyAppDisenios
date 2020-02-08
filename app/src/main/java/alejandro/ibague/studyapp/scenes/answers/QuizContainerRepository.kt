package alejandro.ibague.studyapp.scenes.answers

import alejandro.ibague.studyapp.database.AppDatabase
import alejandro.ibague.studyapp.database.daos.PackageDao
import alejandro.ibague.studyapp.database.entities.PackageEntity
import android.app.Application
import androidx.lifecycle.LiveData
import com.google.firebase.database.FirebaseDatabase

class QuizContainerRepository(application: Application) {
    private val packageDao: PackageDao
    private var databaseRef = FirebaseDatabase.getInstance().reference

    init {
        val db = AppDatabase.getDatabase(application.applicationContext)
        packageDao = db.packageDao()
    }

    fun getInformationOf(packageId: String): LiveData<PackageEntity> {
        return packageDao.findInformationOf(packageId)
    }
}