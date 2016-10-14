package azsecuer.androidy.com.azsecuer.activity.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.HomeActivity;

/**
 * Created by lenovo on 2016/8/8.
 */
public class MainNotification {
    private static NotificationManager notificationManager=null;
    private static final int NOTIFICATIONID=1;
public static void openNotifition(Context context){
    if(notificationManager==null){
        notificationManager=(NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
    }
    Notification notification=null;
//    RemoteViews remoteView=new RemoteViews(context.getPackageName(),R.layout.activity_notification);//TODO 布局已设置
    Intent intent=new Intent(context,HomeActivity.class);//TODO 路径已设置
    PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);
    notification=new Notification.Builder(context)
            .setSmallIcon(R.mipmap.ic_launcher)
//            .setContent(remoteView)//todo 去设置notification的自定义布局
            .setContentTitle("手机管家助手")
            .setContentText("你有新消息了")
            .setTicker("有新消息")
            .setContentIntent(pendingIntent)
            .build();
    notification.flags=Notification.FLAG_AUTO_CANCEL;//todo 除非调用cancleAll()其他情况是不消失的
    notificationManager.notify(NOTIFICATIONID,notification);//todo 发送通知栏消息
}
public static void closeNotifition(Context context){
    if(notificationManager==null){
    notificationManager=(NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
    }
    notificationManager.cancel(NOTIFICATIONID);
}
}
