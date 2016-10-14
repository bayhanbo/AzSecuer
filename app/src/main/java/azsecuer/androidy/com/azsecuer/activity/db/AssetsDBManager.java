package azsecuer.androidy.com.azsecuer.activity.db;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lenovo on 2016/8/11.
 */
public class AssetsDBManager {
    //todo 提供一个COPY方法
    public static void copyDBFileToDB(Context context, String assetsFilePath, File toFile)throws IOException {
        //todo 获取输入流
        InputStream inputStream=context.getResources().getAssets().open(assetsFilePath);
        //todo 创建缓冲区
        byte[]buff=new byte[1024];int len=0;
        //todo 缓冲输入流
        BufferedInputStream bufferedInputStream=new BufferedInputStream(inputStream);
        //todo 文件流
        FileOutputStream fileOutputStream=new FileOutputStream(toFile);
        //todo 缓冲输出流
        BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(fileOutputStream);
        // 进行拷贝
        while((len = bufferedInputStream.read(buff))!=-1){
            bufferedOutputStream.write(buff,0,len);//todo 有多少写多少
        }
        bufferedOutputStream.flush();//todo 要记得flush()
        bufferedOutputStream.close();
        bufferedInputStream.close();
        fileOutputStream.close();
        inputStream.close();//todo close
    }
}
