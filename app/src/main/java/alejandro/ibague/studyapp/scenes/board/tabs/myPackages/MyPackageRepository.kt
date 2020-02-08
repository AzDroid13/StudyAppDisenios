package alejandro.ibague.studyapp.scenes.board.tabs.myPackages

import alejandro.ibague.studyapp.database.AppDatabase
import alejandro.ibague.studyapp.database.daos.PackageDao
import alejandro.ibague.studyapp.database.entities.PackageEntity
import android.app.Application
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyPackageRepository(application: Application) {
    private val packageDao: PackageDao
    private var databaseRef = FirebaseDatabase.getInstance().reference

    init {
        val db = AppDatabase.getDatabase(application.applicationContext)
        packageDao = db.packageDao()
    }

    fun getAllPackage(): LiveData<List<PackageEntity>> {
        Thread {
            packageDao.removeAll()
        }.start()
        return packageDao.getAll()
    }

    fun getAllPackageUserIdPuc(serId: String?): LiveData<List<PackageEntity>> {
        Thread {
            packageDao.removeAll()
        }.start()
        return packageDao.getAllPublicId(serId)
    }


    fun fetchRemotePackageList() {
        databaseRef.child("packages").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val packageList = p0.value as? HashMap<String, HashMap<String, Any>>

                if (null != packageList) {

                    for (packageItem in packageList) {
                        val packageObj = packageItem.value.toMap()
                        val packageEntity = PackageEntity.mapToObject(packageItem.key, packageObj)
                        Thread {
                            packageDao.insertAll(packageEntity)
                        }.start()
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) { }
        })
    }
}