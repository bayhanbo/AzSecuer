package azsecuer.androidy.com.azsecuer.activity.softwaerManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import azsecuer.androidy.com.azsecuer.R;

/**
 * Created by lenovo on 2016/8/23.
 */
public class SoftBowserAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater layoutInflater;
    private List<SoftwareBrowseInfo> datas ;
    public SoftBowserAdapter(Context context){
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.datas = new ArrayList<>();
    }
    public void setDatasToAdapter(List<SoftwareBrowseInfo> datas){
        this.datas = datas;
    }
    public void romoveDataFromAdapter(SoftwareBrowseInfo softwareBrowseInfo){
        this.datas.remove(softwareBrowseInfo);
    }
    @Override
    public int getCount() {
        return this.datas.size();
    }
    @Override
    public SoftwareBrowseInfo getItem(int position) {
        return this.datas.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_software_list,null);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView)convertView.findViewById(R.id.iv_software_icon);
            viewHolder.lable = (TextView)convertView.findViewById(R.id.tv_software_name);
            viewHolder.packageName = (TextView)convertView.findViewById(R.id.tv_software_package_name);
            viewHolder.version = (TextView)convertView.findViewById(R.id.tv_software_version);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        SoftwareBrowseInfo softwareBrowseInfo = getItem(position);
        viewHolder.lable.setText( softwareBrowseInfo.lable);
        viewHolder.packageName.setText( softwareBrowseInfo.packagename);
        viewHolder.version.setText( softwareBrowseInfo.version);
        viewHolder.icon.setImageDrawable(softwareBrowseInfo.drawable);
        return convertView;
    }
    class ViewHolder{
        ImageView icon;
        TextView lable,packageName,version;
    }
}
