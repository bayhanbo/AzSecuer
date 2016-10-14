package azsecuer.androidy.com.azsecuer.activity.speedupdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class AutomoveProgressBar extends ProgressBar {

	public AutomoveProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// 先从0到目标进度
	public void setProgressAutomove1(final int targetProgress) {
		setProgress(0);
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				setProgress(getProgress() + 1);
				if (getProgress() >= targetProgress) {
					setProgress(targetProgress);
					timer.cancel();
				}
			}
		};
		timer.schedule(task, 40, 40);
	}

	private int move_state = 0;
	// 先从当前进度 退回到 0 ，再从0到目标进度
	public void setProgressAutomove2(final int targetProgress) {
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				switch (move_state) {
				case 0:
					setProgress(getProgress() - 1);
					if (getProgress() <= 0) {
						setProgress(0);
						move_state = 1;
					}
					break;
				case 1:
					setProgress(getProgress() + 1);
					if (getProgress() >= targetProgress) {
						setProgress(targetProgress);
						timer.cancel();
					}
					break;
				}

			}
		};
		timer.schedule(task, 40, 40);
	}

}
