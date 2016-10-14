package azsecuer.androidy.com.azsecuer.activity.speedupdemo;

import android.graphics.drawable.Drawable;

public class SpeedupInfo {

	public String processName;
	public Drawable icon; // 图标
	public String label;// 标签
	public long memory;
	public boolean isSelected;
	public boolean isSystemApp;

	public SpeedupInfo(String processName,Drawable icon, String label, long memory, boolean isSystemApp) {
		super();
		this.processName = processName;
		this.icon = icon;
		this.label = label;
		this.memory = memory;
		this.isSystemApp = isSystemApp;
		this.isSelected = false;
	}
}
