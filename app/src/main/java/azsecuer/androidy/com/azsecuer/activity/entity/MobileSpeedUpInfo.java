package azsecuer.androidy.com.azsecuer.activity.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by lenovo on 2016/8/19.
 */
public class MobileSpeedUpInfo{
    public String processName;
    public Drawable icon; //todo 图标
    public String label;//todo 标签
    public long ram;
    public boolean isSelected;
    public boolean isSystemApp;
    public MobileSpeedUpInfo(String processName,Drawable icon, String label, long ram, boolean isSystemApp) {
        super();
        this.processName = processName;
        this.icon = icon;
        this.label = label;
        this.ram = ram;
        this.isSystemApp = isSystemApp;
        this.isSelected = false;
    }
}
