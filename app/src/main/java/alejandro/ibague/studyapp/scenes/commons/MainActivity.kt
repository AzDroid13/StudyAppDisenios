package alejandro.ibague.studyapp.scenes.commons

import alejandro.ibague.studyapp.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import android.view.WindowManager
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.default_floatingview_channel_id)
            val channelName = getString(R.string.default_floatingview_channel_name)
            val defaultChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_MIN)
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(defaultChannel)
        }

        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

    }

    override fun onSupportNavigateUp() = findNavController(R.id.main_board_fragment).navigateUp()
}

