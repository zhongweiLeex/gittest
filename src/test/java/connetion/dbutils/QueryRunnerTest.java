package connetion.dbutils;

import bean.Customer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.jupiter.api.Test;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName QueryRunnerTest
 * @Description TODO
 * @Author Administrator
 * @Date 2022/4/9 21:31
 * @Version 1.0
 **/
public class QueryRunnerTest {
    /* *
     * @Author zhongweiLee
     * @Description 查询操作使用 DBUtils
     * @Date 21:39 2022/4/9
     * @ParamsType []
     * @ParamsName []
     * @return void
     **/
    @Test
    public void testInsert(){
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            String sql = "insert into customers(name,email,birth)values(?,?,?)";
            connection = JDBCUtils.getConnection4();
            int insertCount = runner.update(connection,sql,"须","asbc@qq.com","1990-2-3");
            System.out.println(insertCount);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeConnection(connection,null);
        }
    }
    /* *
     * @Author zhongweiLee
     * @Description 测试查询，使用BeanHandler: 是ResultSetHandler接口的实现类，用于封装表中的一条记录
     * @Date 21:49 2022/4/9
     * @ParamsType []
     * @ParamsName []
     * @return void
     **/
    @Test
    public void testQuery1(){
        QueryRunner runner = new QueryRunner();
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection4();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = "select id,name,email,birth from customers where id = ?";

        ResultSetHandler<Customer> handler = new BeanHandler<>(Customer.class);
        Customer customer = null;
        try {
            customer = runner.query(connection, sql, handler, 23);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {

            JDBCUtils.closeConnection(connection,null);
        }
        System.out.println(customer);
    }

    /* *
     * @Author zhongweiLee
     * @Description BeanListHandler 是ResultSetHandler接口的实现类，用于查询封装表中的多条记录的集合
     * @Date 21:54 2022/4/9
     * @ParamsType []
     * @ParamsName []        
     * @return void
     **/
    @Test
    public void testQuery2(){
        QueryRunner runner = new QueryRunner();
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection4();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = "select id,name,email,birth from customers where id < ?";

        BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
        List<Customer> customer = null;
        try {
            customer = runner.query(connection, sql, handler, 23);
            if (customer != null) {
                customer.forEach(System.out::println);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {

            JDBCUtils.closeConnection(connection,null);
        }
    }

    /* *
     * @Author zhongweiLee
     * @Description MapHandler: 是resultSetHandler 接口的实现类，对应表中的一条记录 将字段相应字段对应位键值对
     * @Date 22:00 2022/4/9
     * @ParamsType []
     * @ParamsName []        
     * @return void
     **/
    @Test
    public void testQuery3(){
        QueryRunner runner = new QueryRunner();
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection4();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = "select id,name,email,birth from customers where id = ?";

        MapHandler handler = new MapHandler();
        Map<String,Object> customer = null;
        try {
            customer = runner.query(connection, sql, handler, 23);
            System.out.println(customer);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {

            JDBCUtils.closeConnection(connection,null);
        }
    }

    @Test
    public void testQuery4(){
        QueryRunner runner = new QueryRunner();
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection4();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql = "select id,name,email,birth from customers where id < ?";

        MapListHandler handler = new MapListHandler();
        List<Map<String,Object>> customer = null;
        try {
            customer = runner.query(connection, sql, handler, 20);
            customer.forEach(System.out::println);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {

            JDBCUtils.closeConnection(connection,null);
        }
    }


}
