package test;

import bean.Customer;
import org.junit.jupiter.api.Test;
import util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName CustomerForQuery
 * @Description 查询customer表的字段类
 * @Author Administrator
 * @Date 2022/4/7 19:44
 * @Version 1.0
 **/
public class CustomerForQuery {
    @Test
    public void testQueryForCustomerCommon(){
        String sql = "select id,birth,email from customers where id = ? AND name = ?";

        Customer customer = queryForCustomersCommon(sql, 18,"lxy");
        System.out.println(customer);

        String sql1 = "select id,name,birth,email from customers where email = ? ";
        Customer customer1 = queryForCustomersCommon(sql, "sdf");
        System.out.println(customer1);
    }
    /* *
     * @Author zhongweiLee
     * @Description 通用查询操作 按照指定字段值查询记录 ，字段数量和字段名 可人为动态指定
     * @Date 20:21 2022/4/7
     * @Param String ... 字段名
     * @return Customer
     * 重点： 通过元数据 获得结果集的数量
     **/
    public Customer queryForCustomersCommon(String sql, Object... args){
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

            resultSet = preparedStatement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();//获取结果集的元数据

            int columnCount = metaData.getColumnCount();//通过元数据 获取结果集的列数量

            //返回查询的一条语句
            //如果是多条结果则写一个while
            if (resultSet.next()){
                Customer customer = new Customer();

                for (int i = 0; i < columnCount; i++) {
                    //获取每个指定列的列值
                    Object columnValue = resultSet.getObject(i + 1);

                    //获取每个指定列的列名
                    String columnName = metaData.getColumnName(i + 1);

                    //给customer对象的指定 columnName属性赋值为 columnValue : 通过 反射
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(customer,columnValue);
                }
                System.out.println(customer);
                return customer;
            }
        } catch (SQLException | NoSuchFieldException | IllegalAccessException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection,preparedStatement,resultSet);//关闭资源
        }
        return null;

    }


    @Test
    /* *
     * @Author zhongweiLee
     * @Description 只能按照id查询记录
     * @Date 19:45 2022/4/7
     * @Param
     * @return void
     **/
    public void queryForCustomersNoCommon(){
        Connection connection = JDBCUtils.getConnection();
        String sql = "select * from customers where id = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1,1);
            //执行sql,并返回结果集
            resultSet = preparedStatement.executeQuery();
            //处理结果集
        /*
        next() 方法 两个作用：
            1. 判断下一个位置是否有元素
            2. 指针下移
        */
            if (resultSet.next()) {
                //获取当前数据的各个字段
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);


                //方式1： 直接连接后输出
                //System.out.println("id = " + id + "name = " + name + "email = " + email + "birth =" + birth);

                //方式2： 使用数组
                //Object[] data = new Object[]{id,name,email,birth};

                //方式3： 将数据封装成一个对象（推荐）
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //关闭资源
        JDBCUtils.closeResource(connection,preparedStatement,resultSet);
    }

}
