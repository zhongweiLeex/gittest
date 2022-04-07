package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 操作数据库的工具类
 * @author ZhongweiLeex
 * @date 2022-04-07 18:34
 */
public class JDBCUtils {
    /* *
     * @Author zhongweiLee
     * @Description  建立连接返回连接对象
     * @Date 20:25 2022/4/7
     * @Param []
     * @return java.sql.Connection
     **/
    public static Connection getConnection(){
        Connection connection = null;
        try {
            InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

            Properties properties = new Properties();
            properties.load(resourceAsStream);
            //获取四个基本信息
            /*
            * 1. 登陆用户名
            * 2. 数据库连接URL
            * 3. 登录密码
            * 4. 数据库驱动
            * */
            String user = properties.getProperty("user");
            String url = properties.getProperty("url");
            String password = properties.getProperty("password");
            String driverClass = properties.getProperty("driverClass");
            //加载驱动
            Class.forName(driverClass);

            connection = DriverManager.getConnection(url, user, password);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        System.out.println("连接成功");
        return connection;
    }

    /**
     * 关闭资源的操作
     * @param connection 连接
     * @param preparedStatement statement语句资源
     */
    public static void closeConnection(Connection connection, Statement preparedStatement){
        // 资源关闭
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /* *
     * @Author zhongweiLee
     * @Description 为查询操作 新建一个关闭结果集的方法
     * @Date 20:06 2022/4/7
     * @Param Connection conn,Statement statement preparedStatement,ResultSet resultSet
     * @return void
     **/
    public static void closeResource(Connection connection,Statement preparedStatement,ResultSet resultSet){
        // 资源关闭
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}
