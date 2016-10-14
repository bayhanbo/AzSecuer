package azsecuer.androidy.com.azsecuer.activity.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lenovo on 2016/8/9.
 */
public class SettingPrefs {
    private static SharedPreferences sharedPreferences=null;
    private static final String KEY_AUTOSTART="CheckAutoStart",KEY_NOTIFIACTION="CheckNitifyAction";
    private static final String SHARECNAME="SettingPreferences";
    public static void saveAutoStart(Context context,boolean isChecked){
        if(sharedPreferences==null){
            sharedPreferences=context.getSharedPreferences(SHARECNAME,context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(KEY_AUTOSTART,isChecked);
        editor.commit();
    }
    public static void saveNotifiaction(Context context,boolean isChecked){
        if(sharedPreferences==null){
            sharedPreferences=context.getSharedPreferences(SHARECNAME,context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(KEY_NOTIFIACTION,isChecked);
        editor.commit();
    }
    public static boolean getAutoStart(Context context){
        if(sharedPreferences==null){
            sharedPreferences=context.getSharedPreferences(SHARECNAME,context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(KEY_AUTOSTART,false);
    }
    public static boolean getNotification(Context context){
        if(sharedPreferences==null){
            sharedPreferences=context.getSharedPreferences(SHARECNAME,context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(KEY_NOTIFIACTION,false);
    }
}
