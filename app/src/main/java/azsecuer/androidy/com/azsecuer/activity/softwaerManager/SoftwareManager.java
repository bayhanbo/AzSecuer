package azsecuer.androidy.com.azsecuer.activity.softwaerManager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lenovo on 2016/8/22.
 */
public class SoftwareManager{
    private PackageManager packageManager;
    /** 用来保存所有应用实体的HashMap */
    private HashMap<Integer,List<SoftwareBrowseInfo>> softwareInfos;
    public static final int CLASSIFY_ALL = 0; //todo 所有应用KEY
    public static final int CLASSIFY_SYS = 1; //todo 系统应用KEY
    public static final int CLASSIFY_USER = 2; //todo 用户应用KEY
    private static SoftwareManager manager;
    public static SoftwareManager getInstance(Context context){
        if(manager==null){
            manager=new SoftwareManager(context);}
        return  manager;
    }
    private SoftwareManager(Context context) {
        packageManager=context.getPackageManager();
        //todo 将用来保存应用程序的三个集合
        softwareInfos = new HashMap<Integer,List<SoftwareBrowseInfo>>();
        //todo 将用来保存 所有应用程序
        softwareInfos.put(CLASSIFY_ALL,new ArrayList<SoftwareBrowseInfo>());
        //todo 将用来保存 系统应用程序
        softwareInfos.put(CLASSIFY_SYS,new ArrayList<SoftwareBrowseInfo>());
        //todo 将用来保存 用户应用程序
        softwareInfos.put(CLASSIFY_USER,new ArrayList<SoftwareBrowseInfo>());
    }
    /** 获取手机上指定分类的应用实体数据集合 */
    public List<SoftwareBrowseInfo> getSoftware(int classify){
        //todo 清空内部所有数据
        softwareInfos.get(CLASSIFY_ALL).clear();
        softwareInfos.get(CLASSIFY_SYS).clear();
        softwareInfos.get(CLASSIFY_USER).clear();
        //todo 加载正在所有应用程序
        loadSoftwareInfos();
        //todo 返回加载到的指定分类应用实体数据集合
        return softwareInfos.get(classify);
    }
    private void loadSoftwareInfos(){
        //todo 能过PackageManager查询到所有MAIN+LAUNCHER的应用程序
        Intent mainIntent = new Intent();
        mainIntent.setAction(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resplveInfos=packageManager.queryIntentActivities(mainIntent,0);
        //todo 循环，通过ResolveInfo取到我们所需的数据
        for(ResolveInfo resplveInfo:resplveInfos){
            Drawable drawable=resplveInfo.loadIcon(packageManager);
            String lable=resplveInfo.loadLabel(packageManager).toString();
            String packagename=resplveInfo.activityInfo.applicationInfo.packageName;
            String version=null;
            try {
                version=packageManager.getPackageInfo(packagename,0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            //todo 分类整合数据
            SoftwareBrowseInfo info=new SoftwareBrowseInfo(lable,packagename,version,drawable);
            int flags=resplveInfo.activityInfo.applicationInfo.flags;
            classifySoftware(info,flags);
        }
    }
    private void classifySoftware(SoftwareBrowseInfo info,int flags){
        softwareInfos.get(CLASSIFY_ALL).add(info);//todo 全部
        if ((flags&ApplicationInfo.FLAG_SYSTEM)!=0){//todo 系统
            softwareInfos.get(CLASSIFY_SYS).add(info);
        }else{//todo 用户
            softwareInfos.get(CLASSIFY_USER).add(info);
        }
    }
    @Deprecated
    public List<SoftwareBrowseInfo> getAllSoftware() {
        List<SoftwareBrowseInfo> browseInfos=new ArrayList<SoftwareBrowseInfo>();
        //todo 通过PackageManager(API)获取到手机所有安装的应用
        int flags = PackageManager.GET_ACTIVITIES | PackageManager.GET_UNINSTALLED_PACKAGES;
        List<PackageInfo> packageInfos=packageManager.getInstalledPackages(0);
        //todo 循环，通过PackageInfo(API,每个应用的包信息,相当于每个应用的manifest)
        for (PackageInfo packageInfo : packageInfos) {
            //todo 取出我们所需的应用信息
            String lable = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            String packagename = packageInfo.packageName;
            String version = packageInfo.versionName;
            Drawable drawable = packageInfo.applicationInfo.loadIcon(packageManager);
            //todo 重新实例为我们所需的实体,保存到集合内
            SoftwareBrowseInfo browseInfo = new SoftwareBrowseInfo(lable, packagename, version, drawable);
            browseInfos.add(browseInfo);
        }
        return browseInfos;
    }
}
