package kv.key;

import kv.base.BaseDimension;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class ContactDimension extends BaseDimension {

  //三个全局变量的属性
   private String telephone;
   private String name;
    //两个构造方法
   public ContactDimension(){
       super();
   }

   public ContactDimension(String telephone,String name){

       super();
       this.telephone=telephone;
       this.name=name;

   }
    //属性的get与set的方法

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //判断两个对象相等的方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactDimension that = (ContactDimension) o;
        return Objects.equals(telephone, that.telephone) &&
                Objects.equals(name, that.name);
    }
    //将对象映射成为一个散列值，方便比较
    @Override
    public int hashCode() {

        return Objects.hash(telephone, name);
    }
    //实现conpareTo方法可以对对象进行比较
    @Override
    public int compareTo(BaseDimension o) {
       ContactDimension ancontactDimension =(ContactDimension) o;
       int result=this.name.compareTo(ancontactDimension.name);

       if(result!=0) return result;

       result=this.telephone.compareTo(ancontactDimension.telephone);
        return result;
    }
    //序列化
    @Override
    public void write(DataOutput out) throws IOException {


       out.writeUTF(this.name);
       out.writeUTF(this.telephone);

    }
    //反序列化
    @Override
    public void readFields(DataInput in) throws IOException {

        this.name=in.readUTF();
        this.telephone=in.readUTF();
    }


}
