package utils;

import org.apache.hadoop.hbase.util.Bytes;

public class Mybyte {
    public static void main(String[] args) {
        String str="00|";
       byte[] b =Bytes.toBytes(str);
        for(int i=0;i<b.length;i++){
            System.out.println(b[i]);
        }

    }
}
