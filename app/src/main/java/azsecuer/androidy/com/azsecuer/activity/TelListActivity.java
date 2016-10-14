package azsecuer.androidy.com.azsecuer.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.adapter.TelListAdapter;
import azsecuer.androidy.com.azsecuer.activity.base.BaseActionBarActivity;
import azsecuer.androidy.com.azsecuer.activity.db.DBManager;
import azsecuer.androidy.com.azsecuer.activity.entity.TelNumberInfo;

/**
 * Created by lenovo on 2016/8/9.
 */
public class TelListActivity extends BaseActionBarActivity implements AdapterView.OnItemClickListener {
    private ListView list_tel_list;
    private Bundle bundle;
    private TelListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel_list);
        initData();
        initView();
        listenerView();
    }
    /**
    解析数据
    */
    private void initData(){
        int idx = this.getIntent().getExtras().getInt("idx");
        adapter=new TelListAdapter(this);
        adapter.setDatas(DBManager.readTabelNumber(idx));
//        Log.i("TelNumberInfos",datas.toString());
//        bundle = this.getIntent().getExtras();//todo 拿到Bundle
//        LogUtil.p("Bundle----------->", bundle.getString("type"));
    }
    @Override
    public void initView(){
        setActionBarBack("通讯大全");
        list_tel_list = (ListView) findViewById(R.id.lv_tel_list);
        list_tel_list.setAdapter(adapter);
    }
    @Override
    public void listenerView(){
        list_tel_list.setOnItemClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_actionbar_left_icon:
                finish();
                break;
        }
    }
    /**
    系统的Dialog的获取方式
    Builder
     */
    private void showDiaLog(int position) {
        //todo 创造者
        final TelNumberInfo telNumberInfo=(TelNumberInfo) adapter.getItem(position);
        final String number = telNumberInfo.getNumber();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("准备拨号中......")
                .setMessage("确定要拨打"+telNumberInfo.getName()+"号码吗?"+"\n"+"tel:" + number)//todo 标题
                .setCancelable(true)//todo 是否能被取消
                .setPositiveButton("确定",new DialogInterface.OnClickListener(){//todo 确定按钮
                    /**
                    需要注意的是监听事件 这里的事件DialogInterface 不要和View的混淆
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //todo 开启打电话操作
                        //todo 1.申请权限
                        Intent intent = new Intent(Intent.ACTION_CALL); //todo 创建一个打电话的意图
                        intent.setData(Uri.parse("tel://" + number));//todo 将电话的Number转化为uri设置给Intent
                        if (ActivityCompat.checkSelfPermission(TelListActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);//todo 开启打电话的操作
                }
            })
                .setNegativeButton("取消", null) //todo 取消按钮
        ;
    builder.show(); //todo 呈现Dialog  和Toast非常相似 也需要你去show() 出来
}
/**
根据选项做拨打电话的操作
 */
@Override
public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    showDiaLog(position);
}
}
