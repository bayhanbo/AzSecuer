package azsecuer.androidy.com.azsecuer.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.entity.MobileSpeedUpInfo;
import azsecuer.androidy.com.azsecuer.activity.util.PublicUtils;

/**
 * Created by lenovo on 2016/8/19.
 */
public class MobileSpeedUpAdapter extends BaseAdapter implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    private List<MobileSpeedUpInfo> dataList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    public MobileSpeedUpAdapter(Context context){
        layoutInflater=LayoutInflater.from(context);
    }
    public void addDataToAdapter(List<MobileSpeedUpInfo> speedupInfos) {
        if (speedupInfos != null) {
            dataList.addAll(speedupInfos);
        }
    }
    public void resetDataToAdapter(List<MobileSpeedUpInfo> speedupInfos) {
        dataList.clear();
        if (speedupInfos != null) {
            dataList.addAll(speedupInfos);
        }
    }
    public List<MobileSpeedUpInfo> getDataFromAdapter() {
        return dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public MobileSpeedUpInfo getItem(int position) {
        return dataList.get(position);
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    //todo 视图类型数量
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    //todo getView中每个convertView在使用前都会来回调此方法进行缓存处理判断
    @Override
    public int getItemViewType(int position) {
        MobileSpeedUpInfo info =getItem(position);
        if (info.isSystemApp) {
            return 1;//todo 1: system
        }
        return 0;//todo 0: user
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        int type = getItemViewType(position);
        //todo UI
        View view = null;
        if (convertView == null){
            switch (type) {
                case 0:
                    view = layoutInflater.inflate(R.layout.speedup_listitem_user,null);
                    break;
                case 1:
                    view = layoutInflater.inflate(R.layout.speedup_listitem_system,null);
                    break;
            }
        }else{
            view = convertView;
        }
        //todo 对整个view进行监听处理(单击每个view时checkbox将选中或取消)
        view.setTag(position);
        view.setOnClickListener(this);
        CheckBox cb_speedup_clear = (CheckBox) view.findViewById(R.id.cb_speedup_clear);
        TextView tv_speedup_info = (TextView) view.findViewById(R.id.tv_speedup_info);
        TextView tv_speedup_ram = (TextView) view.findViewById(R.id.tv_speedup_ram);
        ImageView iv_speedup_icon = (ImageView) view.findViewById(R.id.iv_speedup_icon);
        cb_speedup_clear.setTag(position);
        cb_speedup_clear.setOnCheckedChangeListener(this);
        MobileSpeedUpInfo info = getItem(position);
        cb_speedup_clear.setChecked(info.isSelected);
        tv_speedup_info.setText(info.label);
        tv_speedup_ram.setText(PublicUtils.formatSize(info.ram));
        iv_speedup_icon.setImageDrawable(info.icon);
        return view;
    }
    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        CheckBox cb_speedup_clear = (CheckBox) v.findViewById(R.id.cb_speedup_clear);
        boolean checked = !getItem(position).isSelected;
        //todo UI修改
        cb_speedup_clear.setChecked(checked);
        //todo 实体数据的修改
        getItem(position).isSelected = checked;
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //todo 实体数据的修改
        int position = (Integer) buttonView.getTag();
        MobileSpeedUpInfo info = getItem(position);
        info.isSelected = isChecked;
    }

}
