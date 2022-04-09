package connetion.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * @ClassName JDBCUtils
 * @Description  使用数据库连接池技术的工具类
 * @Author Administrator
 * @Date 2022/4/9 16:37
 * @Version 1.0
 **/
public class JDBCUtils {

    //将cpds 变为全局静态变量 数据库连接池 只造一个 所以必须放到外边
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
    /* *
     * @Author zhongweiLee
     * @Description 使用cpds数据库连接池技术 获取连接
     * @Date 16:47 2022/4/9
     * @ParamsType []
     * @ParamsName []
     * @return java.sql.Connection
     **/
    public static Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }



    /* *
     * @Author zhongweiLee
     * @Description 使用dbcp 数据库连接池技术获取数据库连接
     * @Date 19:44 2022/4/9
     * @ParamsType []
     * @ParamsName []
     * @return java.sql.Connection
     **/
    //申明一个数据库连接池
    //放到外面 只是造一个数据库连接池
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
    public static Connection getConnection2() throws Exception{
        Connection connection = source.getConnection();
        System.out.println(connection);
        return connection;
    }

    /* *
     * @Author zhongweiLee
     * @Description 关闭增删改的链接资源
     * @Date 16:47 2022/4/9
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
     * @Description 关闭查询操作的资源
     * @Date 16:46 2022/4/9
     * @ParamsType [java.sql.Connection, java.sql.Statement, java.sql.ResultSet]
     * @ParamsName [connection, preparedStatement, resultSet]
     * @return void
     **/
    public static void closeResource(Connection connection, Statement preparedStatement, ResultSet resultSet){
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
