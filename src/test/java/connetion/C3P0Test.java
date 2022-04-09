package connetion;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.junit.jupiter.api.Test;
import com.mchange.*;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @ClassName C3P0Test
 * @Description 测试C3P0Test   网站https://www.mchange.com/projects/c3p0/
 * @Author Administrator
 * @Date 2022/4/9 15:39
 * @Version 1.0
 **/
public class C3P0Test {
    /* *
     * @Author zhongweiLee
     * @Description 方式1 使用硬编码方式使用数据库连接池技术
     * @Date 15:56 2022/4/9
     * @ParamsType []
     * @ParamsName []        https://www.mchange.com/projects/c3p0/
     * @return void
     **/
    @Test
    public void testC3P0Connection() throws PropertyVetoException, SQLException {
        //获取c3p0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.cj.jdbc.Driver");
        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/jdbc_learn");
        cpds.setUser("root");
        cpds.setPassword("@lz15195248827");
        //通过设置相关参数对数据库连接池进行管理
        //设置初始时 数据库连接池中的连接数量
        cpds.setInitialPoolSize(10);

        Connection conn = cpds.getConnection();
        System.out.println(conn);

        //销毁连接池。一般不会做
        DataSources.destroy(cpds);

    }

    /* *
     * @Author zhongweiLee
     * @Description 方式2 ： 使用xml配置文件 配置数据库连接池
     * @Date 16:38 2022/4/9
     * @ParamsType []
     * @ParamsName []
     * @return void
     **/
    @Test
    public void testC3P0Connection1() throws SQLException {
        //configName与自定义配置名相同
        ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
        Connection connection = cpds.getConnection();
        System.out.println(connection);
    }
}
