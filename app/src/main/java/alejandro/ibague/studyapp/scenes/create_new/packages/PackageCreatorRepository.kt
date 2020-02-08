package alejandro.ibague.studyapp.scenes.create_new.packages

import alejandro.ibague.studyapp.database.AppDatabase
import alejandro.ibague.studyapp.database.daos.PackageDao
import alejandro.ibague.studyapp.database.entities.PackageEntity
import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class PackageCreatorRepository(application: Application) {
    private val packageDao: PackageDao
    private var storageRef = FirebaseStorage.getInstance().reference
    private var databaseRef = FirebaseDatabase.getInstance().reference

    val downloadThumbnailUri = MutableLiveData<String>()

    init {
        val database = AppDatabase.getDatabase(application)
        packageDao = database.packageDao()
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

    @Deprecated(message = "Function created by Ivan no warranty.")
    fun verifyNamePackage(name: String, userId: String?): LiveData<List<PackageEntity>>{
        return packageDao.verifyPackageName(name, userId)
    }

    fun persistPackage(packageEntity: PackageEntity): String {
        packageEntity.identifier = databaseRef.child("packages").push().key!!
        databaseRef.child("packages").child(packageEntity.identifier).setValue(packageEntity)

        return packageEntity.identifier
    }
}