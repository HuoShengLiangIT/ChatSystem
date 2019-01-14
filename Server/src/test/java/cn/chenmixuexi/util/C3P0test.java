package cn.chenmixuexi.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class C3P0test {
    Connection connection;
    @Before
    public void connection(){
        connection = C3P0Utils.getConnection();
    }

    @Test
    public void select() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from user");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            System.out.print(resultSet.getObject(1)+" ");
            System.out.print(resultSet.getObject(2)+" ");
            System.out.print(resultSet.getObject(3)+" ");
            System.out.println(resultSet.getObject(4)+" ");
        }
    }

    @After
    public void end() throws SQLException {
        connection.close();
    }
}
