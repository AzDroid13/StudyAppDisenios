package alejandro.ibague.studyapp.scenes.profile

import alejandro.ibague.studyapp.database.AppDatabase
import alejandro.ibague.studyapp.database.daos.PackageDao
import alejandro.ibague.studyapp.database.daos.UserDao
import alejandro.ibague.studyapp.database.entities.PackageEntity
import alejandro.ibague.studyapp.database.entities.UserEntity
import android.app.Application
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileRepository(application: Application) {
    private val userDao: UserDao
    private val packageDao: PackageDao
    private var databaseRef = FirebaseDatabase.getInstance().reference

    init {
        val database = AppDatabase.getDatabase(application)
        userDao = database.userDao()
        packageDao = database.packageDao()
    }

    //TODO: Remove duplicate method. [LoginRepository].
    fun updateUserLocalDatabase() {
        databaseRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val remoteUserList = p0.value as? HashMap<String, HashMap<String, Any>>

                if (null != remoteUserList) {
                    for (userItem in remoteUserList) {
                        val userObj = userItem.value.toMap()
                        val userEntity = UserEntity.mapToObject(userItem.key, userObj)
                        Thread {
                            userDao.insertAll(userEntity)
                        }.start()
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) { }
        })
    }

    fun getProfileInfo(userId: String): LiveData<UserEntity> {
        return  userDao.findUserInformationById(userId)
    }

    fun getPackageListOf(userId: String): LiveData<List<PackageEntity>> {
        return  packageDao.findPackageByUser(userId)
    }
}