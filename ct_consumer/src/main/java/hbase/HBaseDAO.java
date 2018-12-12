package hbase;

//此类用于将数据存放到hbase表中，传过来的是数据的一行
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import utils.HBaseUtil;
import utils.PropertiesUtil;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class HBaseDAO {

    //分区数量
    private int regions;
    //命名空间
    private String namespace;
    //表明
    private String tableName;
    //主被叫标志
    private String flag;
    //时间格式化
    private SimpleDateFormat simpleDateFormat;
    //加载配置文件
    private static Configuration conf = null;
    //定义一个表
    private HTable callLogTable;
    static{
        //1.初始化表的配置加载site.xml文件信息
        conf = HBaseConfiguration.create();
    }

    public HBaseDAO() throws IOException {
        /**
         * hbase.calllog.regions=6
         * hbase.calllog.namespace=ns_ct
         * hbase.calllog.tablename=ns_ct:calllog
         */
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tableName = PropertiesUtil.getProperty("hbase.calllog.tablename");
        regions = Integer.valueOf(PropertiesUtil.getProperty("hbase.calllog.regions"));
        namespace = PropertiesUtil.getProperty("hbase.calllog.namespace");
        flag = PropertiesUtil.getProperty("hbase.caller.flag");
        if(!HBaseUtil.isExistTable(conf, tableName)){
            HBaseUtil.initNamespace(conf, namespace);
            HBaseUtil.createTable(conf, tableName, "f1", "f2");
        }

    }
    /**
     * 15596505995,17519874292,2017-03-11 00:30:19,0652
     * 将当前数据 put 到 HTable 中
     * @param log
     */
    public void put(String log){
        try {
            callLogTable = new HTable(conf, tableName);

            String[] splits = log.split(",");

            String call1 = splits[0];
            String call2 = splits[1];
            String dateAndTime = splits[2];
            String timestamp = null;
//rowKey  主叫  被叫  通话时间  通话时间的毫秒表示 通话时长 时间戳   主叫被叫的标志
//rowkey的设计 分区号 主叫 通话时间 被叫  主叫标志 通话时长
            try {
                //将时间格式化成毫秒并转成字符串的格式
                timestamp = String.valueOf(simpleDateFormat.parse(dateAndTime).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String date = dateAndTime.split(" ")[0].replace("-", "");
            String time = dateAndTime.split(" ")[1].replace(":", "");
            String duration = splits[3];

            String regionHash = HBaseUtil.genPartitionCode(call1, date, regions);
            String rowKey = HBaseUtil.genRowKey(regionHash, call1, date + time, call2, flag, duration);
            Put put = new Put(Bytes.toBytes(rowKey));

            put.add(Bytes.toBytes("f1"), Bytes.toBytes("call1"), Bytes.toBytes(call1));
            put.add(Bytes.toBytes("f1"), Bytes.toBytes("call2"), Bytes.toBytes(call2));
            //与尚硅谷不一样，生成的时间也不一样
            put.add(Bytes.toBytes("f1"), Bytes.toBytes("date_time"), Bytes.toBytes(date +
                    time));
            put.add(Bytes.toBytes("f1"), Bytes.toBytes("date_time_ts"),
                    Bytes.toBytes(timestamp));
            put.add(Bytes.toBytes("f1"), Bytes.toBytes("duration"), Bytes.toBytes(duration));
            put.add(Bytes.toBytes("f1"), Bytes.toBytes("flag"), Bytes.toBytes(flag));

            callLogTable.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                callLogTable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



}

