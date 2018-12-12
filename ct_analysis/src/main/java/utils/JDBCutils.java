package utils;


import org.slf4j.LoggerFactory;

import java.sql.*;

//操作mysql返回一个mysql的操作对象
public class JDBCutils {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JDBCutils.class);
    private static final String MSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String MYSQL_USER = "root";
    private static final String MSQL_PASSWORD = "root";
    private static final String MYSQL_URL = "jdbc:mysql://had01:3306/db_telecom?useUnicode=true&characterEncoding=UTF-8";

    //实例化JDBC连接,获得数据库的链接

    public static Connection getconnection() throws SQLException {

        try {
            Class.forName(MSQL_DRIVER);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MSQL_PASSWORD);
    }


    //关闭JDBC链接
    public static void close(Connection connection, Statement statement, ResultSet resultSet) {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public static void main(String[] args) {

        try {
            System.out.println(JDBCutils.getconnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
