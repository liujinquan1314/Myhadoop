package outputformat;


import converter.DimensionConverterImpl;
import kv.key.CommDimension;
import kv.value.CountDurationValue;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import utils.JDBCinstance;
import utils.JDBCutils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//
//看视频的类
public class MySQLOutputFormat extends OutputFormat<CommDimension,CountDurationValue> {
//定义作业的输出提交
    private OutputCommitter committer=null;
    @Override


//1.初始化jdbc连接器对象,建立作业
    public RecordWriter<CommDimension, CountDurationValue> getRecordWriter(TaskAttemptContext context)
            throws IOException, InterruptedException {
        Connection conn=null;
        conn=JDBCinstance.getConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
           throw new RuntimeException(e.getMessage());
        }

        return new MysqlRecordWriter(conn);
    }

//2.输出校验，检查输出的空间
    @Override
    public void checkOutputSpecs(JobContext context) throws IOException, InterruptedException {

    }

//3.定义提交作业的方法，返回一个提交对象
    @Override
    public OutputCommitter getOutputCommitter(TaskAttemptContext context)
            throws IOException, InterruptedException {
        if(committer==null){
            String name=context.getConfiguration().get(FileOutputFormat.OUTDIR);

            Path outputPath=name==null? null:new Path(name);
            committer=new FileOutputCommitter(outputPath,context);
        }
        return committer;
    }

    //-------------------------------------------------------------------------------------------------

    static class MysqlRecordWriter extends RecordWriter<CommDimension,CountDurationValue> {

        private DimensionConverterImpl dic=new DimensionConverterImpl();
        private Connection connection=null;
        private PreparedStatement preparedStatement=null;
        private String insertSql =null;
        private int count = 0;
        private final int BATCH_SIZE = 500;

        public MysqlRecordWriter(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void write(CommDimension key, CountDurationValue value) throws IOException, InterruptedException {
            try {
            //tb_call
            //id_date_contact, id_date_dimension, id_contact, call_sum, call_duration_sum
            //year month day
            //getDateDimension()传过去的是年月日id的那个对象
            int idDateDimension=dic.getDimensionID(key.getDateDimension());
            //telephone_name与id
            int idContact = dic.getDimensionID(key.getContactDimension());
            //vchart类型
            String idDataContact=idDateDimension+"_"+idContact;
            //int类型
            int callSum = Integer.valueOf(value.getCallSum());
            //表中的是int类型
            int callDurationSum = Integer.valueOf(value.getCallDurationSum());

            if(insertSql==null){

                insertSql="INSERT INTO `tb_call` (`id_date_contact`, `id_date_dimension`, `id_contact`, `call_sum`, `call_duration_sum`) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE `id_date_contact` = ?;";
            }

            if(preparedStatement==null){

                    preparedStatement=connection.prepareStatement(insertSql);
            }

            int i =0;
                preparedStatement.setString(++i, idDataContact);
                preparedStatement.setInt(++i, idDateDimension);
                preparedStatement.setInt(++i, idContact);
                preparedStatement.setInt(++i, callSum);
                preparedStatement.setInt(++i, callDurationSum);
                //无则插入，有则更新的判断依据
                preparedStatement.setString(++i, idDataContact);
                preparedStatement.addBatch();
                count++;
                if(count>=BATCH_SIZE){
                    preparedStatement.executeBatch();
                    connection.commit();
                    count=0;
                    preparedStatement.clearBatch();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void close(TaskAttemptContext context) throws IOException, InterruptedException {
            try {
            if(preparedStatement!=null){

                    preparedStatement.executeBatch();
                    this.connection.commit();
            }
         } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                JDBCutils.close(connection,preparedStatement,null);
            }
        }
    }




}