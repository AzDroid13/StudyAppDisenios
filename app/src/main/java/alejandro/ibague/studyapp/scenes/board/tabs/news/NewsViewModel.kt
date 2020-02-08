package alejandro.ibague.studyapp.scenes.board.tabs.news

import alejandro.ibague.studyapp.database.AppDatabase
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class NewsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(getApplication<Application>().applicationContext)
}