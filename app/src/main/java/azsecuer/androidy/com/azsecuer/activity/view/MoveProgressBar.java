package azsecuer.androidy.com.azsecuer.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lenovo on 2016/8/15.
 */
public class MoveProgressBar extends ProgressBar{
    public MoveProgressBar(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }
    public void setProgressMoved(final int targetProgress){
        final Timer timer=new Timer();
        final TimerTask timerTask=new TimerTask(){
            private int moveState=getProgress()>targetProgress?1:2;
            @Override
            public void run() {
                switch(moveState){
                    case 1:
                        setProgress(getProgress()-1);
                        break;
                    case 2:
                        setProgress(getProgress()+1);
                        break;
                }
                if(getProgress()==targetProgress)
                    timer.cancel();
            }
        };
        timer.schedule(timerTask,20,20);//todo 执行任务
    }


}
