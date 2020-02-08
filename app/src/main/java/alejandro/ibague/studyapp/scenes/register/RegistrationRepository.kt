package alejandro.ibague.studyapp.scenes.register

import alejandro.ibague.studyapp.database.AppDatabase
import alejandro.ibague.studyapp.database.daos.UserDao
import alejandro.ibague.studyapp.database.entities.UserEntity
import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RegistrationRepository(application: Application) {
    private val userDao: UserDao
    private var storageRef = FirebaseStorage.getInstance().reference
    private var databaseRef = FirebaseDatabase.getInstance().reference
    val downloadThumbnailUri = MutableLiveData<String>()
    init {
        val db = AppDatabase.getDatabase(application)
        userDao = db.userDao()
    }

    fun createNewUser(userEntity: UserEntity) {
        userEntity.identifier = databaseRef.child("users").push().key!!
        databaseRef.child("users").child(userEntity.identifier).setValue(userEntity)
    }

    fun savePackageThumbnail(fileUri: Uri) {
        val riversRef = storageRef.child("images/${fileUri.lastPathSegment}")
        val uploadTask = riversRef.putFile(fileUri)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnSuccessListener { task ->
            val last = fileUri.lastPathSegment
            val new = task.uploadSessionUri.toString().substringBefore("?")
                .plus("/images%2F$last?alt=media")

            downloadThumbnailUri.postValue(new)
        }
    }
}