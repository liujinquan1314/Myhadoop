package maper;

import kv.key.CommDimension;
import kv.key.ContactDimension;
import kv.key.DateDimension;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CountDurctionMapper extends TableMapper<CommDimension,Text> {

    //用于存放value的值
    private  Text durctionText=new Text();

    //关于存储key的类
    private CommDimension comDimension = new CommDimension();

    //存放联系人电话与姓名的映射
    private Map<String, String> phoneNumMap;
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        phoneNumMap = new HashMap<String, String>();
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
    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context)
            throws IOException, InterruptedException {

        //02_19975534265_20180327083519_16874904884_0_1231

        String rowKey= Bytes.toString(key.get());
            String[] splits=rowKey.split("_");
            //过滤被叫数据，以下数据全部是主叫数据，主叫数据的所有信息都包含了
            if(splits[4].equals("0")) return;
            String caller=splits[1];
            String callee=splits[3];
            String buildTime=splits[2];
            String duration=splits[5];
            //设置kvalue的值
            durctionText.set(duration);

            String year=buildTime.substring(0,4);
            String month=buildTime.substring(4,6);
            String day=buildTime.substring(6,8);
        //组装CommDimension

        //组装DateDimension
        DateDimension yearDimension=new DateDimension(year,"-1","-1");
        DateDimension monthDimension=new DateDimension(year,month,"-1");
        DateDimension dayDimension=new DateDimension(year,month,day);
        //组装ContactDimension
        ContactDimension callercontactDimension=new ContactDimension(caller,phoneNumMap.get(caller));

        //开始聚合主叫数据
        comDimension.setContactDimension(callercontactDimension);
        //年
        comDimension.setDateDimension(monthDimension);
        context.write(comDimension,durctionText);
        //月
        comDimension.setDateDimension(yearDimension);
        context.write(comDimension,durctionText);
        //日
        comDimension.setDateDimension(dayDimension);
        context.write(comDimension,durctionText);

        //组装ContactDimension
        ContactDimension calleecontactDimension=new ContactDimension(callee,phoneNumMap.get(caller));
        //开始聚合被叫数据
        comDimension.setContactDimension(calleecontactDimension);
        //年
        comDimension.setDateDimension(monthDimension);
        context.write(comDimension,durctionText);
        //月
        comDimension.setDateDimension(yearDimension);
        context.write(comDimension,durctionText);
        //日
        comDimension.setDateDimension(dayDimension);
        context.write(comDimension,durctionText);

    }
}
