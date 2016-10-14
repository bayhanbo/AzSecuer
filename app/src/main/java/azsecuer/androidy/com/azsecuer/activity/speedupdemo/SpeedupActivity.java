package azsecuer.androidy.com.azsecuer.activity.speedupdemo;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.base.BaseActionBarActivity;


/**
 * 手机加速页面
 * 
 *
 * 
 * @todo 备注
 * 
 * @author yuanc
 */
public class SpeedupActivity extends BaseActionBarActivity implements OnClickListener,OnCheckedChangeListener {
	// 当前页面控件 - 上
	private TextView tv_speedup_ramMessage,tv_speedup_one,tv_speedup_two; // 运行内存文本信息
	private AutomoveProgressBar pb_speedup_ram; // 运行内存进度条
	private Button btn_speedup_showorhide;// 显示or隐藏系统进程按钮
	// 当前页面控件 - 中
	private ListView lvi_loading_speedup; // 正在运行进程列表
	private ProgressBar pb_loading_progress; // loading
	private SpeedupAdapter speedupAdapter;// 手机加速列表(正在运行的进程)适配器
	// 当前页面控件 - 下
	private View include_checkAndButton; // 底部checkAndButton视图(在加载中时是隐藏的)
	private CheckBox cb_checkAndButton_select; // 全选
	private Button btn_checkAndButton_button;// 一键清理按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_mobile_speed_up);
		super.onCreate(savedInstanceState);
		initView();
		listenerView();
		initData();
	}
	public void initData() {
		asyncLoadRunningApp();
	}
	@Override
	public void initView() {
		// ActionBar
		setActionBarBack("手机加速");
		// 上
		tv_speedup_one=(TextView) findViewById(R.id.tv_speedup_one);
		tv_speedup_two=(TextView) findViewById(R.id.tv_speedup_two);
		tv_speedup_ramMessage = (TextView) findViewById(R.id.tv_speedup_ramMassage);
		pb_speedup_ram = (AutomoveProgressBar) findViewById(R.id.progressBar_speedup);
		btn_speedup_showorhide = (Button) findViewById(R.id.bt_speedup_system);
		btn_speedup_showorhide.setText(R.string.mobile_speedup_system_show);
		// 中
		lvi_loading_speedup = (ListView) findViewById(R.id.lv_loading_listview);
		pb_loading_progress = (ProgressBar) findViewById(R.id.pb_loading);
		speedupAdapter = new SpeedupAdapter(this);
		lvi_loading_speedup.setAdapter(speedupAdapter);
		// 下
		include_checkAndButton = (View) findViewById(R.id.speedup_include_checked_button);
		cb_checkAndButton_select = (CheckBox) findViewById(R.id.cb_speedup_checked);
		btn_checkAndButton_button = (Button) findViewById(R.id.bt_speedup_clear);
		btn_checkAndButton_button.setText(R.string.mobile_speedup_clear);
	}


	public void listenerView() {
		btn_checkAndButton_button.setOnClickListener(this);
		btn_speedup_showorhide.setOnClickListener(this);
		cb_checkAndButton_select.setOnCheckedChangeListener(this);
	}

	private void asyncLoadRunningApp(){
		// 更新运行内存信息
		updateMemory();
		include_checkAndButton.setVisibility(View.GONE);
		pb_loading_progress.setVisibility(View.VISIBLE);
		lvi_loading_speedup.setVisibility(View.INVISIBLE);
		// 新线程异步处理加载正在运行的进程
		// 线程这样随意处理，不做管理其实还是很危险的
		new Thread(new Runnable() {
			@Override
			public void run(){
				// 加载数据(使用写好的手机加速业务逻辑处理)
				SpeedupManager sm = SpeedupManager.getInstance(SpeedupActivity.this);
				List<SpeedupInfo> speedupInfos = null;
				// 将用户进程重置到适配器
				speedupInfos = sm.getRuningApp(SpeedupManager.CLASSIFY_USER,true);
				speedupAdapter.resetDataToAdapter(speedupInfos);//　setDatas
				// UI更新操作
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						speedupAdapter.notifyDataSetChanged();
						pb_loading_progress.setVisibility(View.INVISIBLE);
						lvi_loading_speedup.setVisibility(View.VISIBLE);
						include_checkAndButton.setVisibility(View.VISIBLE);
					}
				});
			}
		}).start();
	}
	/**
	 * 内存信息獲取 和設置
	 */
	private void updateMemory(){
		// 获取运行内存数据信息(全部，空闲可用，已使用)
		tv_speedup_one.setText(Build.BRAND.toUpperCase());
		tv_speedup_two.setText(Build.MODEL.toUpperCase());
		final long total = MemoryManager.getTotalRamMemory();
		final long avail = MemoryManager.getAvailRamMemory(SpeedupActivity.this);
		final long used = total - avail;
		final int ratio = (int) ((float) used / (float) total * 100);
		pb_speedup_ram.setMax(100);
		pb_speedup_ram.setProgressAutomove1(ratio);
		StringBuilder str = new StringBuilder();
		str.append("可用内存:");
		str.append(PublicUtils.formatSize(avail));
		str.append("/");
		str.append(PublicUtils.formatSize(total));
		tv_speedup_ramMessage.setText(str.toString());
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		List<SpeedupInfo> infos = speedupAdapter.getDataFromAdapter();
		for (SpeedupInfo info : infos) {
			info.isSelected = isChecked;
		}
		speedupAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回
		case R.id.iv_actionbar_left_icon:
			finish();
			break;
		// 显示、隐藏系统进程
		case R.id.bt_speedup_system:
			SpeedupManager sm = SpeedupManager.getInstance(SpeedupActivity.this);
			List<SpeedupInfo> sysInfos = sm.getRuningApp(SpeedupManager.CLASSIFY_SYS, false);
			// 当前按钮显示的文本
			String button_text = btn_speedup_showorhide.getText().toString();
			String button_text_show = getResources().getString(R.string.mobile_speedup_system_show);
			String button_text_hide = getResources().getString(R.string.mobile_speedup_system_hide);
			// 当前按钮显示的文本为: 隐藏系统进程
			if (button_text.equals(button_text_hide)) {
				// 将所有系统进程(无需重新加载), 从adapter中移除
				speedupAdapter.getDataFromAdapter().removeAll(sysInfos);
				btn_speedup_showorhide.setText(R.string.mobile_speedup_system_show);
				speedupAdapter.notifyDataSetChanged();
				lvi_loading_speedup.setSelection(0); // 定位到0位置
		}
			// 当前按钮显示的文本为: 显示系统进程
			if (button_text.equals(button_text_show)) {
				// 将所有系统进程(无需重新加载), 添加到adapter中
				speedupAdapter.getDataFromAdapter().addAll(sysInfos);
				btn_speedup_showorhide.setText(R.string.mobile_speedup_system_hide);
				speedupAdapter.notifyDataSetChanged();
				lvi_loading_speedup.setSelection(speedupAdapter.getCount() - 1); // 定位到最后位置
			}
			break;
		// 一键清理
		case R.id.bt_speedup_clear:
			List<SpeedupInfo> infos = speedupAdapter.getDataFromAdapter();
			for (SpeedupInfo info : infos) {
				if (info.isSelected) {
					String packageName = info.processName;
					SpeedupManager.getInstance(this).kill(packageName);
				}
			}
			// 在重新加载所有正在运行进程前 重置UI
			cb_checkAndButton_select.setChecked(false);
			// 在重新加载所有正在运行进程前 重置UI
			btn_speedup_showorhide.setText(R.string.mobile_speedup_system_show);
			// 重新加载所有正在运行进程(此方法 加载所有,但只添加显示用户进程)
			asyncLoadRunningApp();
			break;
		}
	}
}
