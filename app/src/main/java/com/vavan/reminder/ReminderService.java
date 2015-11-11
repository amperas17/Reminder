package com.vavan.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.os.IBinder;

import java.util.concurrent.TimeUnit;

public class ReminderService extends Service {
    NotificationManager notificationManager;
    RemindCountDownTimer timer;
    String text = "no text was set";

    public ReminderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        int timeMinutes = intent.getIntExtra("time",1);

        text = intent.getStringExtra("text");

        timer = new RemindCountDownTimer(10*1000,1000);
        timer.start();

        return START_STICKY;
    }

    public class RemindCountDownTimer extends CountDownTimer {

        public RemindCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            sendNotification();
        }
    }

    public void sendNotification(){
        new Thread(new Runnable() {
            public void run() {

                Context context = getApplicationContext();

                Intent notificationIntent = new Intent(context, MainActivity.class);
                //notificationIntent.putExtra(MainActivity.FILE_NAME, "somefile");

                //открывать ту же активность
                //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                PendingIntent contentIntent = PendingIntent.getActivity(context,
                        0, notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

                Resources res = getApplicationContext().getResources();
                Notification.Builder builder = new Notification.Builder(getApplicationContext());

                builder.setContentIntent(contentIntent)
                        .setSmallIcon(R.drawable.mdpipowerighltning)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.molniya))
                        .setTicker(text)
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setContentTitle("Don`t forget!")
                        .setContentText(text)
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND |
                                Notification.DEFAULT_VIBRATE);


                Notification notification = builder.build();

                //настойчивое уведомление
                //notification.flags = notification.flags | Notification.FLAG_INSISTENT;

                NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, notification);

                stopSelf();
            }
        }).start();
    }
}
