package connetion;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @ClassName DruidTest
 * @Description Druid 连接池技术
 * @Author Administrator
 * @Date 2022/4/9 20:29
 * @Version 1.0
 **/
public class DruidTest {
    @Test
    public void getConnection() throws Exception {
/*        source.setUrl();
        source.setDriverClassName();
        source.setUsername();
        source.setPassword();
        DruidDataSource source = new DruidDataSource();
*/
        Properties pros = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        pros.load(is);

        DataSource source = DruidDataSourceFactory.createDataSource(pros);
        Connection connection = source.getConnection();
        System.out.println(connection);

    }

}
