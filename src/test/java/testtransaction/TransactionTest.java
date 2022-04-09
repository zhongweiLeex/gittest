package testtransaction;

import org.junit.jupiter.api.Test;
import util.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import bean.User;

/**
 * @ClassName TransactionTest
 * @Description 事务
 * @Author Administrator
 * @Date 2022/4/8 20:26
 * @Version 1.0
 **/
public class TransactionTest {
    @Test
    public void transactionTest(){
        //1. 获取数据库连接
        Connection conn = JDBCUtils.getConnection1();
        try {

            //2. 开启事务 关闭自动提交
            conn.setAutoCommit(false);

            //3. 操作数据库
            String sql = "update user_table set balance = balance-100 where user = ?";
            update(conn,sql,"AA");

            //模拟网络异常
//            System.out.println(10/0);

            String sql1 = "update user_table set balance = balance+100 where user = ?";
            update(conn,sql1,"BB");
            System.out.println("转账成功");
            //4.若没有异常，则 提交事务
            conn.commit();
        } catch (Exception throwables) {
//            throwables.printStackTrace();
            try {
                //如果存在异常情况，回滚
                System.out.println("出现异常，回滚事务");
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            //6. 每次都要手动恢复DML操作的自动提交功能
            try {
                conn.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            JDBCUtils.closeResource(conn,null,null);

        }


    }


    public void update(Connection conn,String sql,Object...args){
        PreparedStatement ps = null;
        try {
            //1. 获取 PreparedStatement 实例  预编译 SQL语句
            ps = conn.prepareStatement(sql);
            //2.填充SQL语句占位符
            for(int i = 0 ; i< args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            //3. 执行SQL语句
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeConnection(null,ps);
        }
    }
    //通用增删改 --- version 2.0 考虑事务
    public int update2(Connection conn,String sql,Object...args){
        PreparedStatement ps = null;
        try {
            //1. 获取 PreparedStatement 实例  预编译 SQL语句
            ps = conn.prepareStatement(sql);
            //2.填充SQL语句占位符
            for(int i = 0 ; i< args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            //3. 执行SQL语句
            return ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeConnection(null,ps);
        }
        return 0;
    }

    @Test
    public void testTxSelect() throws ClassNotFoundException, SQLException {
        Connection connection = JDBCUtils.getConnection1();
        //获取事务隔离级别
        System.out.println(connection.getTransactionIsolation());
        //设置数据库隔离级别
//        connection.setTransactionIsolation(connection.TRANSACTION_REPEATABLE_READ);
        Class<User> clazz = User.class;
        String sql = "select name,password,address from user where id = ?";
        Object[] args = new Object[]{5};
        User user = (User) getInstance(connection, clazz, sql, args);
        System.out.println(user);
        connection.setAutoCommit(true);
    }
    @Test
    public void testTxUpdate() throws InterruptedException, SQLException {
        Connection connection = JDBCUtils.getConnection1();
        connection.setAutoCommit(false);
        String sql = "update user set address = ? where name = ?";
        update(connection,sql,"American","LadyGaga");
        Thread.sleep(15000);
        connection.commit();
    }

    /* *
     * @Author zhongweiLee
     * @Description 通用查询操作，用于返回数据表中的一条记录 version 2.0 考虑事务
     * @Date 10:12 2022/4/9
     * @Param [connection, clazz, sql, args]
     * @return T
     **/
    public <T> T getInstance(Connection connection,Class<T> clazz,String sql,Object...args){
//        Connection connection = JDBCUtils.getConnection1();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //执行查询操作
        try {
            preparedStatement = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 1; i <= args.length; i++) {
                preparedStatement.setObject(i, args[i-1]);//将指定字段赋给 preparedStatement 填充占位符
            }
            //获取结果集
            resultSet = preparedStatement.executeQuery();
            //获取结果集元数据
            ResultSetMetaData metaData = resultSet.getMetaData();//获取结果集的元数据
            //通过结果集元数据获取列个数
            int columnCount = metaData.getColumnCount();//通过元数据 获取结果集的列数量


            if (resultSet.next()){

                //Customer customer = new Customer();
                T t = clazz.getDeclaredConstructor().newInstance();

                for (int i = 0; i < columnCount; i++) {
                    //获取每个指定列的列值
                    Object columnValue = resultSet.getObject(i + 1);

                    //获取每个指定列的列名 -- 不推荐使用
                    //String columnName = metaData.getColumnName(i + 1);
                    //获取每个指定列的别名 -- 推荐使用
                    //通过结果集元数据获取列别名
                    String columnLabel = metaData.getColumnLabel(i+1);
                    //通过反射
                    //给customer对象的指定 columnName属性赋值为 columnValue : 通过 反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
//                System.out.println(t);
                return t;
            }
        } catch (SQLException | NoSuchFieldException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,preparedStatement,resultSet);//关闭资源
        }
        return null;
    }

}








































