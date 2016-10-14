package azsecuer.androidy.com.azsecuer.activity.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by lenovo on 2016/8/17.
 */
public class JunkClearInfo {
    public String chineseName,englishName,apkName,filePath;
    public String stype;
    public boolean isChecked;
    public Drawable drawable;
    public JunkClearInfo(String chineseName,String englishName,String apkName,String filePath,boolean isChecked,Drawable drawable,String stype) {
        this.englishName = englishName;
        this.drawable=drawable;
        this.apkName = apkName;
        this.filePath = filePath;
        this.chineseName=chineseName;
        this.isChecked=isChecked;
        this.stype = stype;
    }
    @Override
    public String toString() {
        return "JunkClearInfo{" +
                "chineseName='" + chineseName + '\'' +
                ", englishName='" + englishName + '\'' +
                ", apkName='" + apkName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", stype=" + stype +
                ", isChecked=" + isChecked +
                ", drawable=" + drawable +
                '}';
    }
}
