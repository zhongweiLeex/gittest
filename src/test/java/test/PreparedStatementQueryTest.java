package test;

import bean.Customer;
import bean.User;
import org.junit.jupiter.api.Test;
import util.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PreparedStatementQueryTest
 * @Description 使用PreparedStatement实现对不同表的查询操作
 * @Author Administrator
 * @Date 2022/4/8 14:29
 * @Version 1.0
 **/
public class PreparedStatementQueryTest {
    @Test
    public void testGetForList(){
        String sql = "select id,name,email from customers where id < ?";
        List<Customer> list = getForList(Customer.class, sql, 12);
        list.forEach(System.out::println);//方法引用
    }

    /* *
     * @Author zhongweiLee
     * @Description 返回多条查询结果
     * 步骤：
     * 1. 获取连接
     * 2. 通过sql语句创建 PreparedStatement
     * 3. 向preparedStatement对象中填充占位符 setObject
     * 4. 执行preparedStatement语句 获取 结果集 executeQuery
     * 5. 获取结果集元数据 getMetaData
     * 6. 通过元数据获取列数
     * 7. 通过列数,获取指定列的列值，给t对象指定的属性赋值
     * @Date 15:27 2022/4/8
     * @Param [clazz, sql, args]
     * @return java.util.List<T>
     **/
    public <T> List<T> getForList(Class<T> clazz,String sql,Object...args){

        Connection connection = JDBCUtils.getConnection();

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

            //创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while (resultSet.next()){

                //Customer customer = new Customer();
                T t = clazz.getDeclaredConstructor().newInstance();
                //处理结果集的一行数据中的每一列，给t对象指定的属性赋值
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
                list.add(t);
            }
            return list;
        } catch (SQLException | NoSuchFieldException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection,preparedStatement,resultSet);//关闭资源
        }
        return null;

    }
    @Test
    public void testGetInstance(){
        String sql = "select id,name,email from customers where id = ?";
        Customer customer = getInstance(Customer.class, sql, 12);
        System.out.println(customer);

        String sql1 = "select id,name,password from user where id = ?";
        User user = getInstance(User.class, sql1, 1);
        System.out.println(user);

    }
    /* *
     * @Author zhongweiLee
     * @Description 通用查询操作，对不同的表进行查询， 使用泛型方法， 返回一条记录
     * @Date 15:55 2022/4/8
     * @Param [clazz, sql, args]
     * @return T
     **/
    public <T> T getInstance(Class<T> clazz,String sql,Object...args){
        Connection connection = JDBCUtils.getConnection();

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
            JDBCUtils.closeResource(connection,preparedStatement,resultSet);//关闭资源
        }
        return null;

        
    }
}
