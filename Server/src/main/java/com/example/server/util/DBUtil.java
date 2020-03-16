package com.example.server.util;

import com.example.server.dto.FileDTO;

import java.sql.*;

/**
 * @author saysky
 * @date 2020/2/29 10:40 下午
 */

public class DBUtil {

    private static final String URL = "jdbc:derby://localhost:1527/file_system;create=true";
    private static final String DRIVER_CLASS_NAME = "org.apache.derby.jdbc.ClientDriver";
    public static Connection conn = null;

    //静态代码块最先执行，先执行一次
    static {
        try {
            //加载驱动
            Class.forName(DRIVER_CLASS_NAME);
            //获取连接
            conn = DriverManager.getConnection(URL);
            conn.setAutoCommit(true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单例模式，使对象唯一性
     *
     * @return
     */
    public static Connection getConnection() {
        return conn;
    }


}
