package cn.chenmixuexi.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class C3P0Utils {
    private static ComboPooledDataSource dataSource=new ComboPooledDataSource("mysql");
    public static Connection getConnection(){
        try {
            return dataSource.getConnection();

        } catch (Exception e) {
            System.out.println("连接出错");
        }
        return null;
    }
    //释放连接回连接池
    public static void close(Connection conn, PreparedStatement pst, ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                System.out.println("连接出错");
            }
        }
        if(pst!=null){
            try {
                pst.close();
            } catch (SQLException e) {
                System.out.println("连接出错");
            }
        }

        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("连接出错");
            }
        }
    }
}
