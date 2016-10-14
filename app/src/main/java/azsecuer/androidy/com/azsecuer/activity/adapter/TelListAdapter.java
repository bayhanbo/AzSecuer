package azsecuer.androidy.com.azsecuer.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.entity.TelNumberInfo;

/**
 * Created by lenovo on 2016/8/9.
 */
public class TelListAdapter extends BaseDataAdapter{
    private Context context;
    public TelListAdapter(Context context) {
        super(context);
        this.context = context;
    }
    /**
    ListView显示的项数
     */
    @Override
    public int getCount() {
        return datas.size();
    }
    /**
    返回某一项
    返回类型是非常宽松的
     */
    @Override
    public Object getItem(int position) {
        return datas.get(position);// 返回当前项的数据 Object
    }
    /**
    返回项的id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
    返回每一项的视图
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = super.layoutInflater.inflate(R.layout.list_tel_item,null);
            holder.tvTelName = (TextView)convertView.findViewById(R.id.tv_tel_name);
            holder.tvTelNumber=(TextView)convertView.findViewById(R.id.tv_tel_number);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTelName.setText(((TelNumberInfo)super.getItem(position)).getName());//todo 设置数据
        holder.tvTelNumber.setText(((TelNumberInfo)super.getItem(position)).getNumber());
        return convertView;
    }
    class ViewHolder{
        TextView tvTelName,tvTelNumber;
    }
}
