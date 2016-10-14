package azsecuer.androidy.com.azsecuer.activity.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lenovo on 2016/8/9.
 */
public class SettingPreferences {
    // SharedPreferences 对象
    private static SharedPreferences sharedPreferences = null;
    // 存取读取时所使用的键
    private static final String KEY_ISCHECKEDAUTO = "CheckAutoStart",KEY_ISCHECKEDNOTI = "CheckNitifyAction";
    // 会使用到的上下文对象
    private static Context context = null;
    // 将构造方法私有化 不提供产生这个类的对象
    private SettingPreferences(){

    }
    // 相关的初始化操作
    public static void initSettingPre(Context context){
        SettingPreferences.context = context.getApplicationContext();
        if(sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("SettingPreferences",context.MODE_PRIVATE);
    }
    // 存入数据到sharedPreferences的操作 帮助性方法
    private static void saveSetting(String key,boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }
    // 保存当前的开机启动信息到手机
    public static void saveCheckedAuto(boolean isCheckedAuto){
        saveSetting(KEY_ISCHECKEDAUTO,isCheckedAuto);
    }
    public static void saveCheckedNoti(boolean isCheckedNoti){
        saveSetting(KEY_ISCHECKEDNOTI,isCheckedNoti);
    }
    // 获取当前设置信息返回给调用者
    public static boolean getCheckedAuto(){
        return sharedPreferences.getBoolean(KEY_ISCHECKEDAUTO,false);
    }
    public static boolean getCheckedNoti(){
        return  sharedPreferences.getBoolean(KEY_ISCHECKEDNOTI,false);
    }
}
