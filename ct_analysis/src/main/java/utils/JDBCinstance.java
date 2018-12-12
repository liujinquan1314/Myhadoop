package utils;

import java.sql.Connection;
import java.sql.SQLException;

//本类设计的目的为了防止其他的对象过多的建立连接类
public class JDBCinstance {
    private static Connection connection=null;

    private JDBCinstance(){}

    public static Connection getConnection(){
        try {
            if(connection==null|| connection.isClosed()|| connection.isValid(3)){

                connection=JDBCutils.getconnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }


}
