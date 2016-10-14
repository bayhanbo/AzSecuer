package azsecuer.androidy.com.azsecuer.activity.db;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import azsecuer.androidy.com.azsecuer.activity.entity.MobileSpeedUpInfo;
import azsecuer.androidy.com.azsecuer.activity.util.LogUtil;

/**
 * Created by lenovo on 2016/8/19.
 */
public class MobileSpeedUpManager{
    private Context context;
    private PackageManager pm;
    private ActivityManager am;

    private HashMap<Integer, List<MobileSpeedUpInfo>> mobileSpeedUpInfos = null;
    public static final int CLASSIFY_ALL = 0; //todo 分类：全部进程
    public static final int CLASSIFY_SYS = 1; //todo 分类：系统进程
    public static final int CLASSIFY_USER = 2; //todo 分类：用户进程

    private static MobileSpeedUpManager manager = null;

    public static MobileSpeedUpManager getInstance(Context context) {
        if (manager == null) {
            manager = new MobileSpeedUpManager(context);
        }
        return manager;
    }
    private MobileSpeedUpManager(Context context){
        this.context = context;
        pm = context.getPackageManager();
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //todo 将用来保存 正在运行的系统和用户进程两个集合
        mobileSpeedUpInfos = new HashMap<>();
        //todo 将用来保存 正在运行的全部进程
        mobileSpeedUpInfos.put(CLASSIFY_ALL, new ArrayList<MobileSpeedUpInfo>());
        //todo 将用来保存 正在运行的系统进程
        mobileSpeedUpInfos.put(CLASSIFY_SYS, new ArrayList<MobileSpeedUpInfo>());
        //todo 将用来保存 正在运行的用户进程
        mobileSpeedUpInfos.put(CLASSIFY_USER, new ArrayList<MobileSpeedUpInfo>());
    }
    /** 获取所有正在运行的运程集合 */
    public List<MobileSpeedUpInfo> getRuningApp(int classify, boolean isReload) {
        if (isReload) {
            //todo 清空内部所有数据 list
            mobileSpeedUpInfos.get(CLASSIFY_ALL).clear();
            mobileSpeedUpInfos.get(CLASSIFY_SYS).clear();
            mobileSpeedUpInfos.get(CLASSIFY_USER).clear();
            //todo 加载正在运行的进程
            loadRuningApp();
        }
        //todo 返回加载到的指定分类运行进程集合
        return mobileSpeedUpInfos.get(classify);
    }
    public void kill(String packageName) {
        am.killBackgroundProcesses(packageName);
    }
    public void defSpeedup(){
        //todo 1 获取所有正在运行中的APP
        MobileSpeedUpManager mobileSpeedUpManager = MobileSpeedUpManager.getInstance(context);
        List<MobileSpeedUpInfo> mobileSpeedUpInfos = mobileSpeedUpManager.getRuningApp(0, true);
        //todo 2 清理这些APP后结束循环旋转的加速动画
        for (MobileSpeedUpInfo mobileSpeedUpInfo : mobileSpeedUpInfos) {
            mobileSpeedUpManager.kill(mobileSpeedUpInfo.processName);
        }
    }

    private void loadRuningApp() {
        //todo API： 加载所有正在运行的APP【不是所有都要】
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        LogUtil.p("TAGTAG", " size = " + processInfos.size());
        //todo 分解，解析出我们所需的数据 【对应UI显示所需】
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            //todo 只有前台进程不要 (等级：前台进程 - 可视.. - 服务.. - 后台.. - 空进程)
            //todo IMPORTANCE_FOREGROUND [100]
            //todo IMPORTANCE_VISIBLE [200]
            //todo IMPORTANCE_SERVICE [300]
            //todo IMPORTANCE_BACKGROUND [400]
            //todo IMPORTANCE_EMPTY. [500]
            if (processInfo.importance > 100) {
                String processName = processInfo.processName;//todo 进程名称(取图标)
                //todo 不保存自己
                if (!processName.equals("com.androidy.anzysecure")){
                    try {
                        ApplicationInfo appInfo = pm.getApplicationInfo(processName, 0);
                        Drawable icon = appInfo.loadIcon(pm); //todo 图标
                        String label = appInfo.loadLabel(pm).toString();//todo 标签
                        int pid = processInfo.pid;
                        Debug.MemoryInfo memoryInfo = am.getProcessMemoryInfo(new int[] { pid })[0];
                        long memory = memoryInfo.getTotalPrivateDirty() * 1024;//todo 占用内存
                        MobileSpeedUpInfo mobileSpeedUpInfo = null; //todo 实体【UI所需】
                        //todo 保存系统进程
                        if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                            mobileSpeedUpInfo = new MobileSpeedUpInfo(processName, icon, label, memory, true);
                            mobileSpeedUpInfos.get(CLASSIFY_SYS).add(mobileSpeedUpInfo);
                        }
                        //todo 保存用户进程
                        else {
                            mobileSpeedUpInfo = new MobileSpeedUpInfo(processName, icon, label, memory, false);
                            mobileSpeedUpInfos.get(CLASSIFY_USER).add(mobileSpeedUpInfo);
                        }
                        //todo 保存全部进程
                        mobileSpeedUpInfos.get(CLASSIFY_ALL).add(mobileSpeedUpInfo);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
