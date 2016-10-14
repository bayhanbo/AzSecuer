package azsecuer.androidy.com.azsecuer.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.entity.JunkClearInfo;

/**
 * Created by lenovo on 2016/8/17.
 */
public class JunkClearAdapter extends BaseDataAdapter<JunkClearInfo>{
    public JunkClearAdapter(Context context) {
        super(context);
    }
//@Override
//public View getView(int position, View convertView, ViewGroup parent){
//    ViewHolder viewHolder = null;
//    if(convertView==null){
//        convertView = super.layoutInflater.inflate(R.layout.item_junk_clear_type,null);
//        viewHolder=new ViewHolder();
//        viewHolder.tvClearEnglishName=(TextView)convertView.findViewById(R.id.tv_clear_english_name);
//        viewHolder.tvClearApkName=(TextView)convertView.findViewById(R.id.tv_clear_apk_name);
//        viewHolder.tvClearFilepath=(TextView)convertView.findViewById(R.id.tv_clear_filepath);
////        viewHolder.tvClearStype=(TextView)convertView.findViewById(R.id.tv_clear_stype);
//        convertView.setTag(viewHolder);
//    }else{
//        viewHolder = (ViewHolder) convertView.getTag();
//    }
//    JunkClearInfo junkClearInfo=(JunkClearInfo) super.datas.get(position);
//    viewHolder.tvClearEnglishName.setText(junkClearInfo.getEnglishName());
//    viewHolder.tvClearApkName.setText(junkClearInfo.getApkName());
//    viewHolder.tvClearFilepath.setText(junkClearInfo.getFilePath());
////    viewHolder.tvClearStype.setText((int)junkClearInfo.getStype());
//    return convertView;
//}
//class ViewHolder{
//    TextView tvClearEnglishName,tvClearApkName,tvClearFilepath,tvClearStype;
//}
    /**
     * 没有做相应的ViewHolder优化
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if(convertView == null){
            view = super.layoutInflater.inflate(R.layout.item_junk_clear_type,null);
        }else{
            view = convertView;
        }
        JunkClearInfo junkClearInfo=getItem(position);
        CheckBox checkBox = (CheckBox)view.findViewById(R.id.clear_cb_item_checked);
        TextView tvEnglishName = (TextView)view.findViewById(R.id.tv_clear_soft_name);
        TextView tvFlieSize = (TextView)view.findViewById(R.id.tv_clear_soft_file_size);
        ImageView ivClearIcon = (ImageView)view.findViewById(R.id.iv_clear_soft_icon);
        tvEnglishName.setText(junkClearInfo.englishName);
        tvFlieSize.setText(junkClearInfo.stype+"");
        ivClearIcon.setImageDrawable(junkClearInfo.drawable);
        //todo 设置上Tag
        checkBox.setTag(junkClearInfo);
        checkBox.setChecked(junkClearInfo.isChecked);
        checkBox.setOnCheckedChangeListener(checkedChangeListener);
        return view;
    }
    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            //todo 根据Tag中的值去修改相应的操作
            ((JunkClearInfo)buttonView.getTag()).isChecked = isChecked;
        }
    };
}
