package connetion;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @ClassName DBCPTest
 * @Description DBCP数据库连接池技术获取数据库连接
 * @Author Administrator
 * @Date 2022/4/9 18:50
 * @Version 1.0
 **/
public class DBCPTest {
    /* *
     * @Author zhongweiLee
     * @Description 测试DBCP数据库连接技术 方式1 硬编码方式
     * @Date 18:51 2022/4/9
     * @ParamsType []
     * @ParamsName []
     * @return void
     **/
    @Test
    public void testGetConnection() throws SQLException {
        //ctrl + h 查看所有的实现类层次
        //创建DBCP数据库连接池
        BasicDataSource source = new BasicDataSource();

        //设置基本信息
        source.setDriverClassName("com.mysql.cj.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/jdbc_learn");
        source.setUsername("root");
        source.setPassword("@lz15195248827");
        //设置数据库连接池相关属性
        source.setInitialSize(10);
        //...

        Connection connection = source.getConnection();
        System.out.println(connection);
    }



    //方式2： 使用 配置文件
    /* *
     * @Author zhongweiLee
     * @Description 使用配置文件properties方式  （推荐）
     * @Date 19:42 2022/4/9
     * @ParamsType []
     * @ParamsName []
     * @return void
     **/
    @Test
    public void testGetConnection2() throws Exception{
        Properties properties = new Properties();
        //方式1： 使用ClassLoader
//        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        //方式2： 使用 FileInputStream
        FileInputStream is = new FileInputStream(new File("D:\\IDEA_project\\gittest\\src\\main\\resources\\dbcp.properties"));
        properties.load(is);
        BasicDataSource source = BasicDataSourceFactory.createDataSource(properties);
        Connection connection = source.getConnection();
        System.out.println(connection);
    }
}
