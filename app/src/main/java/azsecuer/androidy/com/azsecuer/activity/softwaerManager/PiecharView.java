package azsecuer.androidy.com.azsecuer.activity.softwaerManager;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.speedupdemo.LogUtil;

/** 软件管理页面的饼形图视图 */
public class PiecharView extends View {
	private Paint paint;
	private RectF piecharRect;
	private int speed = 3;
	private int bgColor = 0;

	/** 饼形图数据,[][0]为颜色, [][1]为目标角度(0-360),[][2]动画初始角度 - 0 */

	private int[][] datas;

	public PiecharView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);//todo 去除锯齿
		//
		bgColor = context.getResources().getColor(R.color.bottle_green);//todo 颜色暂时设置
		//
		datas = new int[0][0];
		LogUtil.d("CustomView","构造方法-paint"+paint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int w = MeasureSpec.getSize(widthMeasureSpec);
		int h = MeasureSpec.getSize(heightMeasureSpec);
		LogUtil.d("CustomView","onMeasure-w"+w+"-h"+h);
		piecharRect = new RectF(0, 0, w, h); //todo 确定饼形圆环大小
		LogUtil.d("CustomView","onMeasure-Rect"+piecharRect);
		setMeasuredDimension(w, h);
	}

	/** 不带动画效果，设置饼形图数据 [][0]为颜色, [][1]为目标角度(0-360),[][2]动画初始角度(传入0即可) */
	public void setAngle(int[][] datas) {
		this.datas = datas;
		for (int i = 0; i < datas.length; i++) {
			datas[i][2] = datas[i][1];
		}
		postInvalidate();
	}
	/** 带动画效果, 各扇形同时进行动画，设置饼形图数据 [][0]为颜色, [][1]为目标角度(0-360),[][2]动画初始角度(传入0即可) */
	public void setAngleWithAnim1(final int[][] datas) {
		speed = 6;
		this.datas = datas;
		for (int i = 0; i < datas.length; i++) {
			datas[i][2] = 0;
		}
		final Timer timer = new Timer();
		final TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				//todo #2
				for (int i = 0; i < datas.length; i++) {
					if (datas[i][2] < datas[i][1]) {
						datas[i][2] += speed;
					} else {
						datas[i][2] = datas[i][1];
					}
				}
				//todo #3
				postInvalidate();
				//todo #4
				boolean isEnd = true;
				for (int i = 0; i < datas.length; i++) {
					if (datas[i][2] < datas[i][1]) {
						isEnd = false;
						break;
					}
				}
				if (isEnd) {
					timer.cancel();
				}
			}
		};
		//todo #1
		timer.schedule(timerTask, 40, 40);
	}

	/**
	 * 带动画效果 , 各扇形逐个进行动画 ，设置饼形图数据 [][0]为颜色, [][1]为目标角度(0-360),[][2]动画初始角度(传入0即可)
	 */
	public void setAngleWithAnim2(int[][] datas) {
		speed = 10;
		this.datas = datas;
		for (int i = 0; i < datas.length; i++) {
			datas[i][2] = 0;
		}
		//todo 1个1个来
		if (datas.length >= 1) {
			startAnim2(0);
		}
	}

	private int tmpi = 0;

	private void startAnim2(final int i) {
		tmpi = i;
		final Timer timer = new Timer();
		final TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				datas[i][2] += speed;
				postInvalidate();
				if (datas[i][2] >= datas[i][1]) {
					datas[i][2] = datas[i][1];
					timer.cancel();
					if (i < datas.length - 1) {
						tmpi++;
						startAnim2(tmpi); //todo 递归
					}
				}
			}
		};
		timer.schedule(timerTask, 40, 40);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int count = 0;
		//todo 底色
		paint.setColor(bgColor);
		canvas.drawArc(piecharRect, 0, 360, true, paint);
		//todo 饼形图数据datas [][0]为颜色, [][1]为目标角度(0-360),[][2]动画初始角度
		int startAngle = -90; //todo 开始画圆的角度(-90开始)
		for (int i = 0; i < datas.length; i++) {
			int color = datas[i][0]; //todo 颜色
			paint.setColor(color);
			canvas.drawArc(piecharRect, startAngle, datas[i][2], true, paint);
			startAngle += datas[i][1];
		}
		LogUtil.d("CustomView","OnDraw()"+count++);
	}
}
