package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    //创建配置文件对象
    public static Properties properties = null;

    static{
        //加载配置文件
        InputStream is = ClassLoader.getSystemResourceAsStream("hbase_consumer.properties");

        properties = new Properties();
        try {
            //将配置文件加载到配置对象中
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key){
        //因为配置文件是以键值对的形式存在所以，可以通过key获得配置文件的值
        return properties.getProperty(key);
    }
}
