package utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.TreeSet;


public class HBaseUtil {
    /**
     * 判断 HBase 表是否存在
     */
    public static boolean isExistTable(Configuration conf, String tableName) throws IOException {
       // 操作 HBase 表必须创建 HBaseAdmin 对象，使表进入到编辑模式
        HBaseAdmin admin = null;
        admin = new HBaseAdmin(conf);
        boolean result=admin.tableExists(tableName);
        admin.close();
        return result;
    }

    /**
     * 初始化命名空间
     */
    public static void initNamespace(Configuration conf, String namespace) {
        HBaseAdmin admin = null;
        try {
            admin = new HBaseAdmin(conf);
            //命名空间类似于关系型数据库中的 schema，可以想象成文件夹
            NamespaceDescriptor ns = NamespaceDescriptor
                    .create(namespace)
                    .addConfiguration("creator", "liujinquan")
                    .addConfiguration("create_time", String.valueOf(System.currentTimeMillis()))
                    .build();
            admin.createNamespace(ns);
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            System.out.println("命名空间已存在~");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != admin) {
                try {
                    admin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 创建表
     */
    public static void createTable(Configuration conf, String tableName, String... columnFamily)
    {
        HBaseAdmin admin = null;
        try {
            admin = new HBaseAdmin(conf);
            // 判断表是否存在
            if (isExistTable(conf, tableName)) {
                // 存在
                System.out.println("表已经存在：" + tableName);
                System.exit(0);
            } else {
                // 不存在
                // 通过表名实例化“表描述器”
                HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
                for (String cf : columnFamily) {
                    //添加列并且设置数据最大的版本数
                    tableDescriptor.addFamily(new HColumnDescriptor(cf).setMaxVersions(3));
                }

                tableDescriptor.addCoprocessor("hbase.CalleeWriteObserver");
                int regions = Integer.valueOf(PropertiesUtil.getProperty("hbase.calllog.regions"));
                admin.createTable(tableDescriptor, getSplitKeys(regions));
                System.out.println("表创建成功：" + tableName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (admin != null) admin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 预分区键创建分区
     * 例如：{"00|", "01|", "02|", "03|", "04|", "05|"}
     * regions分几个区  6
     * @return
     */
    private static byte[][] getSplitKeys(int regions) {
        String[] keys = new String[regions];
        //这里默认不会超过两位数的分区，如果超过，需要变更设计，如果需要灵活操作，
        //也需要变更设计
        DecimalFormat df = new DecimalFormat("00");
        for(int i = 0; i < regions; i++){
            //例如：如果 regions = 6，则：{"00|", "01|", "02|", "03|", "04|", "05|"}
            keys[i] = df.format(i) + "|";
        }
        byte[][] splitKeys = new byte[keys.length][];
        TreeSet<byte[]> rows = new TreeSet<byte[]>(Bytes.BYTES_COMPARATOR);// 升序
        //排序
        for (int i = 0; i < keys.length; i++) {
            rows.add(Bytes.toBytes(keys[i]));
        }
        Iterator<byte[]> rowKeyIter = rows.iterator();
        int i = 0;
        while (rowKeyIter.hasNext()) {
            byte[] tempRow = rowKeyIter.next();
            rowKeyIter.remove();
            splitKeys[i] = tempRow;
            i++;
        }
        //输出的是48 48 124
        //       48 49 124
        return splitKeys;
    }
    /**
     * 生成 rowkey
     *
     */
    public static String genRowKey(String regionHash, String call1, String dateTime, String
            call2, String flag, String duration) {
        StringBuilder sb = new StringBuilder();
        sb.append(regionHash + "_")
                .append(call1 + "_")
                .append(dateTime + "_")
                .append(call2 + "_")
                .append(flag + "_")
                .append(duration);
        return sb.toString();
    }
    /**
     * 生成分区号regionHash
     * 通过主叫、通话日期、主被叫标志生成分区号
     * @return
     */
    public static String genPartitionCode(String call1, String callTime, int regions) {
        int len = call1.length();
        //取出后 4 位电话号码
        String last4Num = call1.substring(len - 4);
        //取出年月
        String first4Num = callTime.replace("-", "").substring(0, 6);
        //亦或后与初始化设定的 region 个数求模
        int hashCode = (Integer.valueOf(last4Num) ^ Integer.valueOf(first4Num)) % regions;
        return new DecimalFormat("00").format(hashCode);
    }
}
