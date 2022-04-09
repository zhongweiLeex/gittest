package util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
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
     *               四个基本信息 ：1. 登陆用户名    2. 数据库连接URL  3. 登录密码     4. 数据库驱动
     * @Date 20:25 2022/4/7
     * @Param []
     * @return java.sql.Connection
     **/
    public static Connection getConnection1(){
        Connection connection = null;
        try {
            //读取配置文件
            InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(resourceAsStream);

            //获取四个基本信息
            String user = properties.getProperty("user");
            String url = properties.getProperty("url");
            String password = properties.getProperty("password");
            String driverClass = properties.getProperty("driverClass");

            //加载驱动
            Class.forName(driverClass);

            //通过 DriverManager 获取相应数据库的连接
            connection = DriverManager.getConnection(url, user, password);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        System.out.println("连接成功");
        return connection;
    }

    /* *
     * @Author zhongweiLee
     * @Description 使用cpds数据库连接池技术 获取连接 将cpds 变为全局静态变量 数据库连接池 只造一个 所以必须放到外边
     * @Date 16:47 2022/4/9
     * @ParamsType []
     * @ParamsName []
     * @return java.sql.Connection
     **/
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
    public static Connection getConnection2() throws SQLException {
        return cpds.getConnection();
    }



    /* *
     * @Author zhongweiLee
     * @Description 使用dbcp 数据库连接池技术获取数据库连接
     * 申明一个数据库连接池
     * 放到外面 只是造一个数据库连接池
     * @Date 19:44 2022/4/9
     * @ParamsType []
     * @ParamsName []
     * @return java.sql.Connection
     **/
    private static DataSource source;
    static {
        try {
            Properties properties = new Properties();
            //方式1： 使用ClassLoader
            //InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            //方式2： 使用 FileInputStream
            FileInputStream is = new FileInputStream(new File("D:\\IDEA_project\\gittest\\target\\classes\\dbcp.properties"));
            properties.load(is);
            source = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection3() throws Exception{
        Connection connection = source.getConnection();
        System.out.println(connection);
        return connection;
    }

    /* *
     * @Author zhongweiLee
     * @Description 使用druid数据库连接池技术
     * @Date 20:57 2022/4/9
     * @ParamsType
     * @ParamsName
     * @return
     **/
    private static DataSource source1;
    static{
        try {
            Properties pros = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            pros.load(is);

            DataSource source = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection4() throws Exception{
        Connection connection = source.getConnection();
        System.out.println(connection);
        return connection;

    }


    /* *
     * @Author zhongweiLee
     * @Description 关闭连接
     * @Date 20:18 2022/4/9
     * @ParamsType [java.sql.Connection, java.sql.Statement]
     * @ParamsName [connection, preparedStatement]
     * @return void
     **/
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
     * @Description 关闭资源
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
