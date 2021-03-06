package azsecuer.androidy.com.azsecuer.activity.fileManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.util.PublicUtils;

/**
 * Created by lenovo on 2016/8/24.
 */
public class FileManagerAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<FileManagerInfo> dataList = new ArrayList<>();
    private Bitmap defBitmap;
    private AbsListView absListView;
    private boolean isFling;

    public FileManagerAdapter(Context context, AbsListView absListView) {
        this.context = context;
        this.absListView = absListView;
        layoutInflater = LayoutInflater.from(context);
        defBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        absListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isFling = false;
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    isFling = true;
                }
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    public void addDataToAdapter(ArrayList<FileManagerInfo> fileInfos) {
        dataList.addAll(fileInfos);
    }
    public void setDatas(ArrayList<FileManagerInfo> dataList){
        this.dataList = dataList;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataList.size();
    }

    @Override
    public FileManagerInfo getItem(int position) {
        // TODO Auto-generated method stub
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.file_manager_item_list,null);
        }
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_filebrowse_listitem_name);
        TextView tv_time = (TextView) convertView.findViewById(R.id.tv_filebrowse_listitem_time);
        TextView tv_size = (TextView) convertView.findViewById(R.id.tv_filebrowse_listitem_size);
        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.iv_filebrowse_listitem_icon);
        CheckBox cb_del = (CheckBox) convertView.findViewById(R.id.cb_filebrowse_listitem_del);
        final FileManagerInfo fileInfo = getItem(position);
        //todo 设置文件图像
        iv_icon.setImageBitmap(getFileIconBitmap(position,fileInfo));
        tv_name.setText(fileInfo.file.getName());
        tv_time.setText(PublicUtils.formatDate(fileInfo.file.lastModified()));
        tv_size.setText(PublicUtils.formatSize(fileInfo.file.length()));
        cb_del.setChecked(fileInfo.isSelect);
        cb_del.setTag(fileInfo);
        cb_del.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               FileManagerInfo fileInfo=(FileManagerInfo) buttonView.getTag();
                fileInfo.isSelect=isChecked;
            }
        });
        return convertView;
    }

    /** 用来缓存文件图像 */
    private LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(1024 * 1024 * 4) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        };
    };
    /** 用来异步加载图像文件的线程池 */
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    /** 获取此文件的图像Bitmap对象 (根据图像名称) */
    private Bitmap getFileIconBitmap(int position, FileManagerInfo fileInfo) {
        //todo 快速滑动时直接返回默认图像
        if (isFling) {
            return defBitmap;
        }
        String filePath = fileInfo.file.getAbsolutePath();
        //todo #1 到缓存中取图像(文件路径做为key)
        Bitmap bitmap = lruCache.get(filePath);
        if (bitmap != null) {
            return bitmap;
        }
        //todo #2 缓存中没有图像
        String fileType = fileInfo.fileType;
        //todo 不是图像的文件 - 根据iconName到drawable中加载
        if (!fileType.equals(FileSearchTypeEvent.TYPE_IMAGE)) {
            String iconName = fileInfo.iconName;
            int id = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
            bitmap = BitmapFactory.decodeResource(context.getResources(), id);
            if (bitmap == null) {
                bitmap = defBitmap;
            } else {
                lruCache.put(filePath, bitmap);
            }
            return bitmap;
        }
        //todo 是图像的文件 - 异步-到本地去加载
        else {
            asyncLoadImageFromDisk(position,fileInfo);
            //todo 返回一张默认图像
            return defBitmap;
        }
    }

    private Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if (msg.what == 0) {
                int position = msg.arg1;
                int cfPosition = absListView.getFirstVisiblePosition();
                int clPosition = absListView.getLastVisiblePosition();
                //todo 图像还在屏幕上
                if (position >= cfPosition && position <= clPosition){
                    notifyDataSetChanged();
                }
            }
        }
    };

    private void asyncLoadImageFromDisk(final int position, final FileManagerInfo fileInfo) {
        //todo 线程池控制线程并发数量
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //todo 本地加载图像
                String filePath = fileInfo.file.getAbsolutePath();
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;//todo 打开加载边界处理
                BitmapFactory.decodeFile(filePath, opts);
                int wp = opts.outWidth / 40;
                int hp = opts.outHeight / 40;
                int p = wp > hp ? wp : hp;
                if (p < 1) {
                    p = 1;
                }
                opts.inSampleSize = p;//todo 计算设置缩放比
                opts.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeFile(filePath, opts);
                if (bitmap == null) {
                    bitmap = defBitmap;
                } else {
                    lruCache.put(filePath, bitmap);
                    Message message = mainHandler.obtainMessage();
                    message.what = 0;
                    message.arg1 = position;
                    mainHandler.sendMessage(message);
                }
            }
        });
    }
}
