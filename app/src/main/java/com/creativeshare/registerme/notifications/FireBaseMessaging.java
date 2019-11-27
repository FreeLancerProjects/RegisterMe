package com.creativeshare.registerme.notifications;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.registerme.models.Order_Model;
import com.creativeshare.registerme.preferences.Preferences;
import com.creativeshare.registerme.tags.Tags;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.Random;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;


public class FireBaseMessaging extends FirebaseMessagingService {
    private Preferences preferences;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
preferences=Preferences.getInstance();
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        Map<String,String> map = remoteMessage.getData();

        if(preferences.getSession(this).equals( Tags.session_login)) {
            for (String key:map.keySet())
            {
                Log.e("key",key+"    value "+map.get(key));
            }
            if (map.size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());

manageNotification(map);

            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                Toast.makeText(this,remoteMessage.getNotification().getBody(),Toast.LENGTH_LONG).show();
            }
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void manageNotification(Map<String, String> map) {

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            createNewNotificationVersion(map);
        }else
        {
            createOldNotificationVersion(map);

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createNewNotificationVersion(Map<String, String> map) {

        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;

        String not_type = map.get("notification_type");
Log.e("type",map.get("notification_type"));
     if(not_type.equals("order"))
        {

            int to_id = Integer.parseInt(map.get("adv_owner"));
           // String title = map.get("title");
            String content = map.get("text");

            sendNotification_VersionNew(content,sound_Path);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createOldNotificationVersion(Map<String, String> map)
    {


        String sound_Path = "android.resource://" + getPackageName() + "/" + R.raw.not;

        String not_type = map.get("notification_type");
        Log.e("type",map.get("notification_type"));

     if(not_type.equals("order"))
        {

            String content = map.get("text");

            sendNotification_VersionOld(content,sound_Path);
        }

    }

    private void sendNotification_VersionOld( String content, String sound_path) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSound(Uri.parse(sound_path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.drawable.logonot);
        builder.setAutoCancel(true);
      //  builder.setContentTitle(title);

        Intent intent = new Intent(this, Home_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("not",true);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
          builder.setContentIntent(pendingIntent);

        builder.setContentText(content);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(12352, builder.build());

        }
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                final  Order_Model order_model=new Order_Model();
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //    Log.e("mange","mang");
                if (manager != null) {

                    builder.setLargeIcon(bitmap);
                    EventBus.getDefault().post(order_model);

                    manager.notify(new Random().nextInt(200), builder.build());
                  //  Log.e("mange","mang");

                }

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        new Handler(Looper.getMainLooper())
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Picasso.with(FireBaseMessaging.this).load(R.drawable.logonot).into(target);



                    }
                }, 1);



    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification_VersionNew(String content, String sound_path) {

        String CHANNEL_ID = "my_channel_02";
        CharSequence CHANNEL_NAME = "my_channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
        channel.setShowBadge(true);
        channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .build()
        );
        builder.setChannelId(CHANNEL_ID);
        builder.setSound(Uri.parse(sound_path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.drawable.logonot);
        builder.setAutoCancel(true);

        //  builder.setContentTitle(title);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logonot);
        builder.setLargeIcon(bitmap);
        Intent intent = new Intent(this, Home_Activity.class);
        intent.putExtra("not",true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        builder.setContentText(content);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(channel);
            manager.notify(12352, builder.build());
        }

        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                final   Order_Model order_model=new Order_Model();

                if (manager != null) {
                    builder.setLargeIcon(bitmap);
                    EventBus.getDefault().post(order_model);
                    manager.createNotificationChannel(channel);
                    manager.notify(new Random().nextInt(200), builder.build());
                }

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };


        new Handler(Looper.getMainLooper())
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Picasso.with(FireBaseMessaging.this).load(R.drawable.logonot).into(target);

                    }
                }, 1);

    }

}
