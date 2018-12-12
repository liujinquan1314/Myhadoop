package kafka;

import hbase.HBaseDAO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import utils.PropertiesUtil;

import java.io.IOException;
import java.util.Arrays;

public class HBaseConsumer {
    public static void main(String[] args) throws IOException {
        //获得消费者的配置文件
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(PropertiesUtil.properties);
       //加载消费主题
        kafkaConsumer.subscribe(Arrays.asList(PropertiesUtil.getProperty("kafka.topics")));
        HBaseDAO hBaseDAO = new HBaseDAO();
        while(true){
            //从kafka拉取放到ConsumerRecords键值对中。
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for(ConsumerRecord<String,String> cr : records){

                String orivalue=cr.value();
                System.out.println(cr.value());
                hBaseDAO.put(orivalue);

            }
        }
    }
}
