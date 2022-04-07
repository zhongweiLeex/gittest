//import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * @author ZhongweiLeex
 * @Date 2022-04-07 16:02
 */
public class ConnectionTest {
    //四个基本信息：
    /*
    * 1.  具体的厂商驱动
    * 2. 数据库服务器 URL
    * 3. 用户名
    * 4. 密码
    *
    * */
    //方式1:
    @Test
    public void testConnection1() throws SQLException {
        //获取Driver实现类对象
        Driver driver = new com.mysql.jdbc.Driver();
        String url ="jdbc:mysql://localhost:3306/jdbc_learn?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
        //设置用户名 密码
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","@lz15195248827");
        Connection connection = driver.connect(url, info);
        System.out.println(connection);
    }

    //不能出现第三方API
    //方式2： 对方式 1 的迭代， 在如下程序中不出现第三方的API 使得程序可移植性
    //使用反射
    
    @Test
    public void testConnection2() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, SQLException {
        //1.获取实现类对象
        Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.getDeclaredConstructor().newInstance();

        //2. 提供要链接的数据库
        String url = "jdbc:mysql://localhost:3306/jdbc_learn?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";

        //3. 设置要链接的数据库的用户名和密码
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","@lz15195248827");
        //4.获取连接
        Connection connection = driver.connect(url, info);
        System.out.println(connection);

    }

    //方式3： 使用DriverManager 替换Driver
    @Test
    public void testConnection3() throws Exception{
        //1. 获取Driver实现类对象
        Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.getDeclaredConstructor().newInstance();

        //2 另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/jdbc_learn?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
        String user = "root";
        String password = "@lz15195248827";
        //注册驱动
        DriverManager.registerDriver(driver);
        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);

    }

    //方式4
    // 只加载驱动 不显式注册驱动
    @Test
    public void testConnection4() throws Exception{
        //1 另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/jdbc_learn?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
        String user = "root";
        String password = "@lz15195248827";

        //2. 获取Driver实现类对象
        //加载了 Driver类的静态代码块 直接自动注册了驱动

/*        * static {
            System.err.println("Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.");
        }
        * */
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);

    }

    //方式5  最终 使用配置文件
    /*
    * 好处：
    * 1. 使用此种方式 只需要改动配置文件内容就可以 ，实现了 数据与代码分离，实现了解耦
    * 2.
    * */
    @Test
    public void getConnection5() throws IOException, ClassNotFoundException, SQLException {
        //1. 使用反射 getClassLoader 读取配置文件中四个基本信息
        //读取配置文件到 输入流
        InputStream resourceAsStream = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        //从资源输入流中 加载到基本信息
        Properties pros = new Properties();
        pros.load(resourceAsStream);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        //2. 加载驱动
        Class.forName(driverClass);

        //3.获取连接
        Connection connection = DriverManager.getConnection(url,user,password);
        System.out.println(connection);
//        System.out.println("ac");

    }



}






























