package azsecuer.androidy.com.azsecuer.activity.db;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.entity.JunkClearInfo;
import azsecuer.androidy.com.azsecuer.activity.entity.TelNumberInfo;
import azsecuer.androidy.com.azsecuer.activity.entity.TelTypeInfo;
import azsecuer.androidy.com.azsecuer.activity.util.FileUtils;
import azsecuer.androidy.com.azsecuer.activity.util.LogUtil;
import azsecuer.androidy.com.azsecuer.activity.util.PublicUtils;

/**
 * Created by lenovo on 2016/8/11.
 */
public class DBManager {
    //todo 创建
    public static File fileDB ;
    public static void createFile(Context context)throws IOException {
        //todo  文件夹 路径
        String dbFileDir = "data/data/"+context.getPackageName()+"/db";
        File fileDir = new File(dbFileDir);
        boolean mkSuer = fileDir.mkdirs();//todo 创建路径
        Log.i("createDBFile",mkSuer+"");
        fileDB = new File(fileDir,"commonnum.db");//todo 创建db文件
        fileDB.createNewFile();
    }
    public static void createFileClear(Context context)throws IOException {
        //todo  文件夹 路径
        String dbFileDir = "data/data/"+context.getPackageName()+"/db";
        File fileDir = new File(dbFileDir);
        boolean mkSuer = fileDir.mkdirs();//todo 创建路径
        Log.i("createDBFile",mkSuer+"");
        fileDB = new File(fileDir,"clearpath.db");//todo 创建db文件
        fileDB.createNewFile();
    }
    /**
     * 当前DBFile是否已经拥有
     *
     * @return boolean true已有不需要再做拷贝 false 没有 需要拷贝
     */
    //todo 判定
    public static boolean dbFileIsExists(){
        if(fileDB.length()==0||!fileDB.exists()){
            return true;
        }
        return false;
    }
    //todo 读取电话类型数据
    public static List<TelTypeInfo> readTableClassList(Context context){
        List<TelTypeInfo> typeList = new ArrayList<>();
        //todo 关于查询
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(fileDB,null);
        Cursor cursor = sqLiteDatabase.rawQuery("select * from classlist",null);//todo 执行查询
        if(cursor.moveToFirst()){//todo 游标已经来到了0的位置
            TelTypeInfo typeInfo = null;
            do{//
                typeInfo = new TelTypeInfo(cursor.getString(cursor.getColumnIndex("name"))
                        ,cursor.getInt(cursor.getColumnIndex("idx")));
                typeList.add(typeInfo);//todo 装入集合当中
                Log.i("DBManager-Typeinfo",typeInfo.toString());
            }while(cursor.moveToNext());
        }else{
            Log.i("DBManager--TableClass","没有数据");
        }
        return typeList;
    }
    //todo 获取电话分类下的电话信息
    public static List<TelNumberInfo> readTabelNumber(Integer idx){
        List<TelNumberInfo> numberList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(fileDB,null);
        Cursor cursor = sqLiteDatabase.rawQuery("select * from table"+idx,null);//todo 执行查询
        if(cursor.moveToFirst()){
            TelNumberInfo telNumberInfo = null;
            do{
                telNumberInfo = new TelNumberInfo(cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("number")));
                numberList.add(telNumberInfo);
                Log.i("DBManager--NumberInfo",telNumberInfo.toString());
            }while(cursor.moveToNext());
        }else{
            Log.i("DBManager--NumberClass","没有数据");
        }
        return numberList;
    }
    //todo 获取垃圾文件信息
    /**
     * 创建目录
     *
     * @param context 上下文对象
     */
    public static List<JunkClearInfo> readTabelJunkFile(Context context){
        List<JunkClearInfo> junClearList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=SQLiteDatabase.openOrCreateDatabase(fileDB,null);
        Cursor cursor=sqLiteDatabase.rawQuery("select * from softdetail",null);
//        if(cursor.moveToFirst()){//todo 游标已经来到了0的位置
//            JunkClearInfo junkClearInfo = null;
//            do{
//                junkClearInfo = new JunkClearInfo(cursor.getString(cursor.getColumnIndex("softEnglishname")),
//                        context.getResources().getDrawable(R.drawable.icon_folder),
//                        cursor.getDouble(cursor.getColumnIndex("stype")));
//                junClearList.add(junkClearInfo);//todo 装入集合当中
//                Log.i("DBManager-Typeinfo",junkClearInfo.toString());
//            }while(cursor.moveToNext());
//        }else{
//            Log.i("DBManager--ClearClass","没有数据");
//        }
//        return junClearList;
        if (cursor.moveToFirst()) {
            do {
                String chineseName = cursor.getString(cursor.getColumnIndex("softChinesename"));
                String softEnglishname = cursor.getString(cursor.getColumnIndex("softEnglishname"));
                String apkName = cursor.getString(cursor.getColumnIndex("apkname"));
                String filepath = Environment.getExternalStorageDirectory()+cursor.getString(cursor.getColumnIndex("filepath"));
                //todo 拼接目录
                File cacheFile = new File(filepath);
                if(cacheFile.exists()) { //todo 如果有效路径 代表手机上是安装了这个APP 的 并且有这个目录的
                    long fileSize = FileUtils.getFileSize(cacheFile);//todo 获取文件大小
                    String fileSizeStr = PublicUtils.formatSize(fileSize);//todo 转换成相应的格式
                    Drawable drawable = null;
                    try {//todo 能够拿到application的图标
                        drawable = context.getPackageManager().getApplicationIcon(apkName);
                    } catch (PackageManager.NameNotFoundException e){
                        //todo 不能拿到的时候给的默认图片
                        drawable = context.getResources().getDrawable(R.drawable.icon_folder);
                        e.printStackTrace();
                    }
                    //todo 拿到不为零的文件 并且存放到集合当中 交给adapter适配数据
                    JunkClearInfo junkClearInfo = new JunkClearInfo(chineseName,softEnglishname,apkName,
                            filepath, false, drawable, fileSizeStr);
                    junClearList.add(junkClearInfo);
                }
            } while (cursor.moveToNext());
        }
        return junClearList;
    }
}
