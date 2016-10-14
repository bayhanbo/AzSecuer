package azsecuer.androidy.com.azsecuer.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.entity.MobileChildInfo;
import azsecuer.androidy.com.azsecuer.activity.entity.MobileGroupInfo;

/**
 * Created by lenovo on 2016/8/16.
 */
public class MobileInfoAdapter extends BaseExpandableListAdapter{
    private LayoutInflater layoutInflater;
    private Context context;
    //todo 父项的数据源
    private List<MobileGroupInfo> groups;
    //todo 子项的数据源
    private Map<String,List<MobileChildInfo>> childs;
    public MobileInfoAdapter(Context context){
        super();this.context=context;
        this.layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groups=new ArrayList<>();
        this.childs=new HashMap<>();
    }
    public void addDataToAdapter(MobileGroupInfo group, List<MobileChildInfo> child){
        groups.add(group);
        this.childs.put(group.title,child);
    }
    @Override
    public int getGroupCount() {
        return groups.size();
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return childs.get(groups.get(groupPosition).title).size();
    }
    @Override
    public MobileGroupInfo getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }
    @Override
    public MobileChildInfo getChild(int groupPosition, int childPosition) {
        return childs.get(groups.get(groupPosition).title).get(childPosition);
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view=layoutInflater.inflate(R.layout.item_mobileinfo_group,null);
        MobileGroupInfo temp=getGroup(groupPosition);
        ((ImageView)view.findViewById(R.id.iv_mobile_info_group)).setImageDrawable(temp.drawable);
        ((TextView)view.findViewById(R.id.tv_mobile_info_group)).setText(temp.title);
        return view;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        MobileChildInfo temp=getChild(groupPosition,childPosition);
        View view=layoutInflater.inflate(R.layout.item_mobileinfo_child,null);
        ((TextView)view.findViewById(R.id.tv_mobile_info_child_title)).setText(temp.title);
        ((TextView)view.findViewById(R.id.tv_mobile_info_child_content)).setText(temp.centent);
        return view;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
