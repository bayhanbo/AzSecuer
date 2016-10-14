package azsecuer.androidy.com.azsecuer.activity.fileManager;


import java.io.File;

/**
 * Created by lenovo on 2016/8/24.
 */
public class FileManagerInfo{
    public File file; //todo 文件对象
    public int romType;//todo 文件所在空间类型(内置0?外置1?)
    public String fileType; //todo 文件类型(图像?音频?)
    public String iconName;//todo 此文件所用图像名称
    public String openType;//todo 此文件的打开类型
    public boolean isSelect;
    /**
     * 文件实体类构造器
     * @param file 文件对象
     * @param romType 文件所在空间类型(内置0?外置1?)
     * @param fileType 文件类型(图像?音频?)
     * @param iconName  此文件对应所用图像名称
     * @param openType 此文件打开类型
     */
    public FileManagerInfo(File file,int romType,String fileType,String iconName,String openType){
        this.file = file;
        this.romType = romType;
        this.fileType = fileType;
        this.iconName = iconName;
        this.openType = openType;
        this.isSelect = false;
    }
}
