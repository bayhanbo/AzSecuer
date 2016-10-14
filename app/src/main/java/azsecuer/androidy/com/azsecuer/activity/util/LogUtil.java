package azsecuer.androidy.com.azsecuer.activity.util;

import android.util.Log;

/**
 * Created by lenovo on 2016/8/3.
 * Log优化一个工具类
 */
public class LogUtil {
//    测试开关
private static boolean isDebug=true;
//    错误信息的开关
private static  boolean isErro=true;
public static void p(String tag,String msg){
if(isDebug) Log.i(tag,msg);
}
public static void p(String tag,String msg,Throwable tr){
if(isErro) Log.i(tag,msg,tr);
}
}
