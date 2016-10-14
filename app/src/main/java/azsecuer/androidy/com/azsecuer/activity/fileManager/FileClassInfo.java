package azsecuer.androidy.com.azsecuer.activity.fileManager;

/**
 * Created by lenovo on 2016/8/24.
 */
public class FileClassInfo{
    /** 当前种类文件分类名称(音频文件?图像文件?APK文件?) */
    public String typeName;
    /** 当前种类文件类型<KEY> (音频?图像?APK?) */
    public String fileType;
    /** 当前种类文件大小 */
    public long size;
    /** 当前种类文件加载是否结束 */
    public boolean loadingOver;
    public FileClassInfo(String typeName, String fileType) {
        super();
        this.typeName = typeName;
        this.fileType = fileType;
        this.loadingOver = false;
        this.size = 0;
    }
}
