package azsecuer.androidy.com.azsecuer.activity.util;

import java.io.File;

/**
 * Created by lenovo on 2016/8/18.
 */
public class FileUtils {
    /**
     * 获取到文件的大小
     * @param file
     * @return
     */
    public static long getFileSize(File file){
        long size=0,size1=0;
        if(file.exists()&&file!=null){//todo 判定当前对象是否有效
            if(file.isFile()){//todo 判定是否是文件
                return size+=file.length();
            }
            if(file.isDirectory()){//todo 判断是否是文件夹
                File[]files=file.listFiles();//todo 获取这个文件夹里面的所有目录 文件
                if(files!=null) {//todo 保证不会出现NullPointerException
                    for (File temp : files) {//todo 遍历这个文件数组
                        size1 += getFileSize(temp);//todo 调用自己
                    }
                }
                return size1;
            }
        }
        return  size;
    }
    /**
     *
     * @param file 删除的文件对象 可能是文件夹 也可能是文件 也可能是null
     */
//    public static void deleteFile(File file){
//        if(file!=null){
//            if(!file.isDirectory()){
//                file.delete();
//            }
//            else{//todo 是目录、
//                File[] files = file.listFiles(); //todo 获取当前目录下的所有File对象
//                if(files!=null){
//                    //todo 遍历并且调用 递归 进行删除操作
//                    for(File file1: files){
//                        deleteFile(file1);
//                    }
//                    //todo 文件删除完以后才能清理掉文件夹
//                    file.delete();
//                }
//            }
//        }
//    }
    /**
     *
     * @param file 删除的对象
     */
    public static void deleteFile(File file){
        if(file!=null && file.exists()){
            if(file.isFile()){//todo 是一个文件
                file.delete(); //todo 删除
            }
            if(file.isDirectory()){//todo 是一个文件夹
                File[] files = file.listFiles(); //todo 当前文件夹内的所有文件 包括文件夹
                if(files!= null){
                    for (File temp:
                            files) {
                        deleteFile(temp);//todo 遍历出来以后调用后方法本身
                    }
                }
            }
        }
    }
}
