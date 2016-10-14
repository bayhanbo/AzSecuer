package azsecuer.androidy.com.azsecuer.activity.entity;

/**
 * Created by lenovo on 2016/8/11.
 */
public class TelTypeInfo {
    private String typeName;
    private int idx;

    public TelTypeInfo(String typeName, int idx) {
        this.typeName = typeName;
        this.idx = idx;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "TelTypeInfo{" +
                "typeName='" + typeName + '\'' +
                ", idx=" + idx +
                '}';
    }
}
