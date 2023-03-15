package com.lyneon.splash;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Context;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.WindowManager;
import android.graphics.PixelFormat;
import android.view.ViewGroup;
import android.view.Gravity;
import android.os.Handler;
import android.os.Looper;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import androidx.core.app.NotificationCompat;
import com.lyneon.customview.MarqueeTextView;
import android.widget.Toast;

public class ShowService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    

    private BroadcastReceiver mReceiver;
    private SharedPreferences shared_preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_USER_PRESENT);
        mReceiver = new MyReceiver();
        registerReceiver(mReceiver, filter);
        shared_preferences = getApplicationContext().getSharedPreferences("message",Context.MODE_PRIVATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1,getNotification());
        return super.onStartCommand(intent, flags, startId);
    }
    
    

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消注册广播接收器
        unregisterReceiver(mReceiver);
        stopForeground(true);
    }

    // 自定义的广播接收器类
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try{
            if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                // 处理解锁屏幕的逻辑
                final MarqueeTextView floating_textview_message = new MarqueeTextView(context);
                floating_textview_message.setText(shared_preferences.getString("message", "no message"));
                floating_textview_message.setTextColor(shared_preferences.getInt("text_color", Color.parseColor("#000000")));
                floating_textview_message.setTextSize(shared_preferences.getFloat("text_size",20));
                floating_textview_message.setSpeed(shared_preferences.getInt("text_speed",0));
                final WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                //FLAG_NOT_TOUCHABLE
                //FLAG_SHOW_WHEN_LOCKED
                params.flags = params.FLAG_NOT_TOUCHABLE | params.FLAG_SHOW_WHEN_LOCKED;
                params.format = PixelFormat.TRANSPARENT;
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.gravity = Gravity.CENTER;
                //TYPE_SYSTEM_OVERLAY
                params.type = params.TYPE_APPLICATION_OVERLAY;
                manager.addView(floating_textview_message,params);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            manager.removeView(floating_textview_message);
                        }
                    }, 5000);
            }
            }catch(Exception e){
                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

//在Service中创建Notification对象
    private Notification getNotification() {
        NotificationChannel channel = new NotificationChannel("channelId", "channelName", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this, "channelId")
            .setContentTitle("前台服务")
            .setContentText("解锁后将显示预定消息")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build();
        return notification;
    }
}
