package azsecuer.androidy.com.azsecuer.activity.fileManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.util.PublicUtils;

/**
 * Created by lenovo on 2016/8/24.
 */
public class FileServiceAdapter extends BaseAdapter{
    private LayoutInflater layoutInflater;
    private ArrayList<FileClassInfo> dataList = new ArrayList<>();
    public FileServiceAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }
    public void addDataToAdapter(FileClassInfo fileClassInfo) {
        if (fileClassInfo != null) {
            dataList.add(fileClassInfo);
        }
    }
    @Override
    public int getCount() {
        return dataList.size();
    }
    @Override
    public FileClassInfo getItem(int position) {
        return dataList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.file_manager_item_list_service,null);
        }
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_fileservice_listitem_title);
        TextView tv_size = (TextView) convertView.findViewById(R.id.tv_fileservice_listitem_size);
        ImageView tv_arrow = (ImageView) convertView.findViewById(R.id.iv_fileservice_listitem_arrows_right);
        ProgressBar pb_loading = (ProgressBar) convertView.findViewById(R.id.pb_fileservice_listitem_loading);
        FileClassInfo info = getItem(position);
        tv_title.setText(info.typeName);
        if(info.loadingOver){
            pb_loading.setVisibility(View.INVISIBLE);
            tv_arrow.setVisibility(View.VISIBLE);
            tv_size.setText(PublicUtils.formatSize(info.size));
        }else{
            pb_loading.setVisibility(View.VISIBLE);
            tv_arrow.setVisibility(View.INVISIBLE);
            tv_size.setText("正在计算中...请稍等");
        }
        return convertView;
    }
}
