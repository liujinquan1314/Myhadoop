package kv.key;

import kv.base.BaseDimension;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class DateDimension extends BaseDimension {
    //全局变量
    private String year;
    private String month;
    private String day;
    //两个构造函数
    public DateDimension(){
        super();
    }

    public DateDimension(String year, String month,String day){
        super();
        this.year=year;
        this.month=month;
        this.day=day;
    }

    //get与set的方法



    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    //equare与hashcode方法


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateDimension that = (DateDimension) o;
        return Objects.equals(year, that.year) &&
                Objects.equals(month, that.month) &&
                Objects.equals(day, that.day);
    }

    @Override
    public int hashCode() {

        return Objects.hash(year, month, day);
    }


    @Override
    public int compareTo(BaseDimension o) {
    DateDimension dateDimension=(DateDimension) o;

    int result=this.year.compareTo(dateDimension.year);

    if(result!=0) return result;

    result=this.month.compareTo(dateDimension.month);

    if(result!=0) return  result;

    result=this.day.compareTo(dateDimension.day);

        return result;
    }

    @Override
    public void write(DataOutput out) throws IOException {

        out.writeUTF(this.year);
        out.writeUTF(this.month);
        out.writeUTF(this.day);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.year=in.readUTF();
        this.month=in.readUTF();
        this.day=in.readUTF();
    }
}
