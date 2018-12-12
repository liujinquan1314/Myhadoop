package producer;

import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProductLog {

    //一年的开始时间与结束时间
    private String startTime="2018-1-1";
    private String endTime="2018-12-31";
    //存放手机号
    private List<String> phoneList = new ArrayList();
    //存放手机号以及对应的姓名
    private Map<String, String> phoneNumMap = new HashMap<String, String>();
    //初始化客户数据
    public void ininphone(){

        phoneList.add("14377580421");
        phoneList.add("17881822932");
        phoneList.add("17605528572");
        phoneList.add("14852116256");
        phoneList.add("16951700051");
        phoneList.add("15242822743");
        phoneList.add("18786787182");
        phoneList.add("18800254850");
        phoneList.add("18116379580");
        phoneList.add("18063676885");
        phoneList.add("17293514190");
        phoneList.add("14706207315");
        phoneList.add("16874904884");
        phoneList.add("15635617288");
        phoneList.add("16482060303");
        phoneList.add("17352284769");
        phoneList.add("18149735286");
        phoneList.add("13881946250");
        phoneList.add("15828763759");
        phoneList.add("19975534265");

        phoneNumMap.put("14377580421","钱易梦");
        phoneNumMap.put("17881822932","施丽");
        phoneNumMap.put("17605528572","郑翡翠");
        phoneNumMap.put("14852116256","金晓云");
        phoneNumMap.put("16951700051","许珊");
        phoneNumMap.put("15242822743","韩秀");
        phoneNumMap.put("18786787182","李瑾");
        phoneNumMap.put("18800254850","孙永珍");
        phoneNumMap.put("18116379580","朱冰露");
        phoneNumMap.put("18063676885","魏妍");
        phoneNumMap.put("17293514190","孔倩");
        phoneNumMap.put("14706207315","华妙海");
        phoneNumMap.put("16874904884","潘艳红");
        phoneNumMap.put("15635617288","彭招弟");
        phoneNumMap.put("16482060303","冯艺");
        phoneNumMap.put("17352284769","蒋岚");
        phoneNumMap.put("18149735286","严美丽");
        phoneNumMap.put("13881946250","王小丽");
        phoneNumMap.put("15828763759","华黛");
        phoneNumMap.put("19975534265","吕媛");
    }

   //生产数据以字符串的形式返回
    public String product(){
        //定义主叫
        String phonenum="";
        //定义被叫
        String phonenumd="";
        //定义主叫的姓名
        String phoneName="";
        //定义被叫的姓名
        String phoneNamed="";

        //随机获得主叫的电话以及姓名
        int indexcaller=(int)(Math.random()*phoneList.size());
         phonenum= phoneList.get(indexcaller);
         phoneName=phoneNumMap.get(phonenum);

        //获得被叫的电话以及姓名，如果主叫等于被叫就继续循环，不等于就对被叫进行赋值
        while(true){
            int indexcalled=(int)(Math.random()*phoneList.size());
            phonenumd=phoneList.get(indexcalled);
            phoneNamed=phoneNumMap.get(phonenumd);
            if(!phonenum.equals(phonenumd)) break;
        }
        //获得随机日期
        String MyTime=getOneTime(startTime,endTime);
        DecimalFormat df=new DecimalFormat("0000");
        String callTime=df.format((int)(30*60*Math.random()));

        String result=phonenum+","+phonenumd+","+MyTime+","+callTime;

        return result;
    }

    //获取随机通话日期
    public String getOneTime(String startTime,String endTime){

        String MyDate=null;
        try {
            SimpleDateFormat formateTime=new SimpleDateFormat("yyyy-MM-dd");
            //将带格式的日期转换成毫秒数
            Date startdata=formateTime.parse(startTime);
            Date enddata=formateTime.parse(endTime);

            if(enddata.getTime()<=startdata.getTime()) return null;

            long radomdata=startdata.getTime()+(long)((enddata.getTime()-startdata.getTime())*Math.random());
            Date newDate=new Date(radomdata);
            SimpleDateFormat newformDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           //将毫秒数转换成格式化日期
            MyDate =newformDate.format(newDate);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return MyDate;
    }

    //生成日志文件存储磁盘
    public void logphone(String filepath){
        OutputStreamWriter out=null;
        try {
          out =new OutputStreamWriter(new FileOutputStream(filepath),"UTF-8");
          while(true){
              Thread.sleep(500);
              String log=product();
              System.out.println(log);
              out.write(log+"\r\n");
              out.flush();

          }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {

       //String path="e://aaa//a.txt";
        ProductLog pro=new ProductLog();
        pro.ininphone();
        //生产日志存放在某个路径中
        pro.logphone(args[0]);

    }
}
