package dao2;

import util.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName BaseDAO
 * @Description 封装针对于数据表的通用操作
 * @Author Administrator
 * @Date 2022/4/9 10:52
 * @Version 1.0
 **/
public abstract class BaseDAO<T> {

    private Class<T> clazz = null;

    //获取当前BaseDAO 子类继承父类中的泛型
    {
        Type genericSuperclass = this.getClass().getGenericSuperclass();//子类自己获取父类的泛型
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;//将父类泛型转换成参数类型
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();//获取父类泛型参数

        clazz = (Class<T>) actualTypeArguments[0]; //泛型的第一个参数就是 对应表的类 customer
    }

    /* *
     * @Author zhongweiLee
     * @Description 通用增删改 --- version 2.0 考虑事务
     * @Date 13:12 2022/4/9
     * @ParamsType [java.sql.Connection, java.lang.String, java.lang.Object...]
     * @ParamsName [conn, sql, args]
     * @return int
     **/
    public int update(Connection conn, String sql, Object...args){
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


    /* *
     * @Author zhongweiLee
     * @Description 通用查询操作 返回一个对象 考虑事务
     * @Date 13:11 2022/4/9
     * @ParamsType [java.sql.Connection, java.lang.Class<T>, java.lang.String, java.lang.Object...]
     * @ParamsName [connection, clazz, sql, args]
     * @return T
     **/
    public T getInstance(Connection connection,String sql,Object...args){
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
                //通过反射获取对象实例
                T t = clazz.getDeclaredConstructor().newInstance();

                for (int i = 0; i < columnCount; i++) {
                    //获取每个指定列的列值
                    Object columnValue = resultSet.getObject(i + 1);

                    //获取每个指定列的列名 -- 不推荐使用
                    //String columnName = metaData.getColumnName(i + 1);
                    //获取每个指定列的别名 -- 推荐使用


                    //通过结果集元数据获取列别名
                    String columnLabel = metaData.getColumnLabel(i+1);

                    //通过反射 设定对象的属性值
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

    /* *
     * @Author zhongweiLee
     * @Description 通用查询操作 返回多个对象 考虑事务
     * @Date 13:11 2022/4/9
     * @ParamsType [java.sql.Connection, java.lang.Class<T>, java.lang.String, java.lang.Object...]
     * @ParamsName [connection, clazz, sql, args]
     * @return java.util.List<T>
     **/
    public List<T> getForList(Connection connection, String sql, Object...args){

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
            JDBCUtils.closeResource(null,preparedStatement,resultSet);//关闭资源
        }
        return null;
    }


    /* *
     * @Author zhongweiLee
     * @Description 用于查询特殊值的通用方法
     * @Date 13:12 2022/4/9
     * @ParamsType [java.sql.Connection, java.lang.String, java.lang.Object...]
     * @ParamsName [conn, sql, args]
     * @return E
     **/
    public <E> E getValue(Connection conn,String sql,Object... args){

        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            resultSet = ps.executeQuery();
            if (resultSet.next()){
                return (E) resultSet.getObject(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,resultSet);
        }
        return null;

    }

}
