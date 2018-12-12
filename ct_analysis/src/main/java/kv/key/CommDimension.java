package kv.key;

import kv.base.BaseDimension;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

//可以把这个类当做key的输出的类型，此类是key的组合类型
public class CommDimension extends BaseDimension {
    //成员变量
    private ContactDimension contactDimension=new ContactDimension();
    private  DateDimension dateDimension=new DateDimension();

    //构造方法
    public CommDimension(){
        super();
    }

    //get与set方法

    public ContactDimension getContactDimension() {
        return contactDimension;
    }

    public void setContactDimension(ContactDimension contactDimension) {
        this.contactDimension = contactDimension;
    }

    public DateDimension getDateDimension() {
        return dateDimension;
    }

    public void setDateDimension(DateDimension dateDimension) {
        this.dateDimension = dateDimension;
    }

    //hashcode与equals方法

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommDimension that = (CommDimension) o;
        return Objects.equals(contactDimension, that.contactDimension) &&
                Objects.equals(dateDimension, that.dateDimension);
    }

    @Override
    public int hashCode() {

        return Objects.hash(contactDimension, dateDimension);
    }

    @Override
    public int compareTo(BaseDimension o) {
        CommDimension ancommDimension=(CommDimension) o;

        int result =this.dateDimension.compareTo(ancommDimension.dateDimension);

        if(result!=0) return result;

        result=this.contactDimension.compareTo(ancommDimension.contactDimension);

        return result;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        contactDimension.write(out);
        dateDimension.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
       contactDimension.readFields(in);
        dateDimension.readFields(in);
    }
}
