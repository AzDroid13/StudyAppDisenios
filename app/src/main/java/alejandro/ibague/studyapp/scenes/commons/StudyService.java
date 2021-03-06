package alejandro.ibague.studyapp.scenes.commons;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.core.app.NotificationCompat;

import alejandro.ibague.studyapp.R;
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewListener;
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewManager;


public class StudyService extends Service implements FloatingViewListener {
    public static final String EXTRA_CUTOUT_SAFE_AREA = "cutout_safe_area";
    private static final int NOTIFICATION_ID = 9083150;
    private FloatingViewManager mFloatingViewManager;

    //https://medium.com/@prasannajeet/using-the-navigation-architecture-component-in-android-jetpack-kotlin-48d4167ec9e5

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mFloatingViewManager != null) {
            return START_STICKY;
        }

        final DisplayMetrics metrics = new DisplayMetrics();
        final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        final LayoutInflater inflater = LayoutInflater.from(this);
        final ImageView iconView = (ImageView) inflater.inflate(R.layout.widget_chathead, null, false);
        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.d(TAG, getString(R.string.chathead_click_message));
            }
        });

        mFloatingViewManager = new FloatingViewManager(this, this);
        mFloatingViewManager.setFixedTrashIconImage(R.drawable.ic_trash_fixed);
        mFloatingViewManager.setActionTrashIconImage(R.drawable.ic_trash_action);
        mFloatingViewManager.setSafeInsetRect((Rect) intent.getParcelableExtra(EXTRA_CUTOUT_SAFE_AREA));
        final FloatingViewManager.Options options = new FloatingViewManager.Options();
        options.overMargin = (int) (16 * metrics.density);
        mFloatingViewManager.addViewToWindow(iconView, options);

        startForeground(NOTIFICATION_ID, createNotification(this));

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        destroy();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onFinishFloatingView() {
        stopSelf();
    }


    @Override
    public void onTouchFinished(boolean isFinishing, int x, int y) {
        //   Log.d(TAG, getString(R.string.deleted_soon));
        /// Log.d(TAG, getString(R.string.touch_finished_position, x, y));
    }

    private void destroy() {
        if (mFloatingViewManager != null) {
            mFloatingViewManager.removeAllViewToWindow();
            mFloatingViewManager = null;
        }
    }

    private static Notification createNotification(Context context) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.default_floatingview_channel_id));
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
      //  builder.setContentTitle(context.getString(R.string.chathead_content_title));
      //  builder.setContentText(context.getString(R.string.content_text));
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_MIN);
        builder.setCategory(NotificationCompat.CATEGORY_SERVICE);

        return builder.build();
    }
}
