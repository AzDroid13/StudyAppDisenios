package alejandro.ibague.studyapp.scenes.login

import alejandro.ibague.studyapp.database.AppDatabase
import alejandro.ibague.studyapp.database.daos.UserDao
import alejandro.ibague.studyapp.database.entities.UserEntity
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginRepository(application: Application) {
    private val userDao: UserDao
    private var databaseRef = FirebaseDatabase.getInstance().reference
    val userAuthenticated = MutableLiveData<UserEntity>()
    val db: AppDatabase = AppDatabase.getDatabase(application.applicationContext)

    companion object {
        var firstTimeLoginIntent = true
    }

    init {
        userDao = db.userDao()
    }

    fun findUser(userName: String, password: String) {
        if (firstTimeLoginIntent) {
            fetchRemotePackageList(userName, password)
        } else {
            userDao.findUser(userName, password).observeForever {
                if (null == it) {
                    userAuthenticated.postValue(null)
                } else {
                    userAuthenticated.postValue(it)
                }
            }
        }
    }

    private fun fetchRemotePackageList(userName: String, password: String) {
        databaseRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.childrenCount > 0) {
                    val userList = p0.value as? HashMap<String, HashMap<String, Any>>

                    if (null != userList) {
                        for (user in userList) {
                            val userObj = user.value.toMap()
                            val userEntity = UserEntity.mapToObject(user.key, userObj)

                            Thread {
                                userDao.insertAll(userEntity)
                            }.start()
                        }
                    }
                }

                firstTimeLoginIntent = false
                findUser(userName, password)
            }

            override fun onCancelled(p0: DatabaseError) { }
        })
    }
}