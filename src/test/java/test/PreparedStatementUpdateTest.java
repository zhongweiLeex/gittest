package test;

import org.junit.jupiter.api.Test;
import util.JDBCUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author ZhongweiLeex
 * @Date 2022-04-07 18:07
 */
/*
* 使用PreparedStatement 替换Statement,实现对数据表的增删改查操作
*
* */
public class PreparedStatementUpdateTest {
    //添加
    //向customers表中添加一条记录
    @Test
    /* *
     * @Author zhongweiLee
     * @Description 未使用工具类，普通update操作
     * @Date 15:44 2022/4/8
     * @Param []
     * @return void
     **/
    public void testInsert(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
            Properties pros = new Properties();
            pros.load(resourceAsStream);
            //1. 获取配置文件的信息
            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");
            //2.加载驱动
            Class.forName(driverClass);
            //3. 获取连接对象
            connection = DriverManager.getConnection(url,user,password);
            System.out.println(connection);

            //4. 预编译sql语句，返回PreparedStatement实例
            String sql = "insert into customers(name,email,birth) values(?,?,?)";
            preparedStatement = connection.prepareStatement(sql);

            //5. 填充占位符  parameterIndex 索引是从1开始的，不是从0开始的
            preparedStatement.setString(1,"李忠伟");
            preparedStatement.setString(2,"lzw10033@Gmail.com");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse("1998-10-28");
            preparedStatement.setDate(3,new java.sql.Date(date.getTime()));

            //6. 执行sql操作
            preparedStatement.execute();
        } catch (IOException | ClassNotFoundException | SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            //7. 资源关闭
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
    }
    @Test
    /* *
     * @Author zhongweiLee
     * @Description 普通update操作操作
     * @Date 15:44 2022/4/8
     * @Param []
     * @return void
     **/
    public void testUpdate(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        //1.获取数据库连接
        connection = JDBCUtils.getConnection1();
        try {
            //2.预编译SQL语句
            String sql = "update customers set name = ? where id = ?";
            preparedStatement = connection.prepareStatement(sql);
            // 3. 填充占位符
            preparedStatement.setObject(1,"lxy");
            preparedStatement.setObject(2,18);
            //4.执行
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            //5. 关闭资源
            JDBCUtils.closeConnection(connection,preparedStatement);
        }
    }

    @Test
    /* *
     * @Author zhongweiLee
     * @Description 测试通用update操作
     * @Date 15:49 2022/4/8
     * @Param []
     * @return void
     **/
    public void testGenerateUpdate(){
        String sql = "delete from customers where id = ?";
        generateUpdate(sql,3);
    }
    /* *
     * @Author zhongweiLee
     * @Description 通用update操作
     * @Date 15:50 2022/4/8
     * @Param [sql, args]
     * @return void
     **/
    public void generateUpdate(String sql,Object ... args){
        Connection connection = null;
        PreparedStatement statement = null;
        //1.获取连接
        connection = JDBCUtils.getConnection1();
        try {
            //2.预编译语句
            statement = connection.prepareStatement(sql);

            //3. 填充占位符
            for (int i = 1; i <= args.length; i++) {
                statement.setObject(i,args[i-1]);
            }
            //4.执行操作
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //5.关闭资源
        JDBCUtils.closeConnection(connection,statement);
    }



}
