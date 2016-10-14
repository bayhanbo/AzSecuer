package azsecuer.androidy.com.azsecuer.activity.softwaerManager;

import android.graphics.drawable.Drawable;

/**
 * Created by lenovo on 2016/8/22.
 */
public class SoftwareBrowseInfo{
    public String lable;
    public String packagename;
    public String version;
    public Drawable drawable;
    public boolean isSelected;
    public SoftwareBrowseInfo(){}
    public SoftwareBrowseInfo(String lable,String packagename,String version,Drawable drawable) {
        this.lable = lable;
        this.packagename = packagename;
        this.version = version;
        this.drawable = drawable;
        this.isSelected = false;
    }
}
