package azsecuer.androidy.com.azsecuer.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.entity.TelTypeInfo;

/**
 * Created by lenovo on 2016/8/11.
 */
public class TelTypeListAdapter extends BaseDataAdapter{
public TelTypeListAdapter(Context conetxt){
    super(conetxt);
}
@Override
public View getView(int position, View convertView, ViewGroup parent){
    ViewHolder viewHolder = null;
    if(convertView == null){
        convertView = super.layoutInflater.inflate(R.layout.list_tel_type_item,null);
        viewHolder = new ViewHolder();
        viewHolder.textView = (TextView)convertView.findViewById(R.id.tv_teltype);
        convertView.setTag(viewHolder);
    }else{
        viewHolder = (ViewHolder) convertView.getTag();
    }
    TelTypeInfo typeInfo = (TelTypeInfo) super.datas.get(position);
    viewHolder.textView.setText(typeInfo.getTypeName());
    return convertView;
}
class ViewHolder{
    TextView textView;
}
}
