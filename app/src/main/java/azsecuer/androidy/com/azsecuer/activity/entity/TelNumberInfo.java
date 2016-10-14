package azsecuer.androidy.com.azsecuer.activity.entity;

/**
 * Created by lenovo on 2016/8/11.
 */
public class TelNumberInfo {
    private String name;
    private String number;

    public TelNumberInfo(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return "TelNumberInfo{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
