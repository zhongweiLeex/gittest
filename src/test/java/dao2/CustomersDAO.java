package dao2;

import bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/* *
 * @Author zhongweiLee
 * @Description 规范针对 Customers表的常用操作
 * @Date 12:44 2022/4/9
 * @Param
 * @return
 **/
public interface CustomersDAO {
    /* *
     * @Author zhongweiLee
     * @Description 将cust对象添加到数据库中形成一条记录
     * @Date 12:59 2022/4/9
     * @ParamsType [java.sql.Connection, bean.Customer]
     * @ParamsName [conn, cust]
     * @return void
     **/
    void insert(Connection conn, Customer cust);

    /* *
     * @Author zhongweiLee
     * @Description 根据指定Id删除表中记录
     * @Date 12:59 2022/4/9
     * @ParamsType [java.sql.Connection, int]
     * @ParamsName [conn, id]
     * @return void
     **/
    void deleteById(Connection conn,int id);

    /* *
     * @Author zhongweiLee
     * @Description 根据 cust对象修改 表中的对应记录
     * @Date 13:01 2022/4/9
     * @ParamsType [java.sql.Connection, bean.Customer]
     * @ParamsName [conn, cust]        
     * @return void
     **/
    void update(Connection conn,Customer cust);

    /* *
     * @Author zhongweiLee
     * @Description 根据指定ID查询对应Customer
     * @Date 13:02 2022/4/9
     * @ParamsType [java.sql.Connection, int]
     * @ParamsName [conn, id]
     * @return Customer
     **/
    Customer getCustomerById(Connection conn,int id);
    /* *
     * @Author zhongweiLee
     * @Description 查询表中所有记录
     * @Date 13:03 2022/4/9
     * @ParamsType [java.sql.Connection]
     * @ParamsName [conn]        
     * @return java.util.List<bean.Customer>
     **/    
    List<Customer> getAll(Connection conn);
    
    /* *
     * @Author zhongweiLee
     * @Description 返回数据表中的数据条目数
     * @Date 13:04 2022/4/9
     * @ParamsType [java.sql.Connection]
     * @ParamsName [conn]        
     * @return java.lang.Long
     **/
    Long getCount(Connection conn);
    /* *
     * @Author zhongweiLee
     * @Description 返回数据表中最大生日
     * @Date 13:04 2022/4/9
     * @ParamsType [java.sql.Connection]
     * @ParamsName [conn]        
     * @return java.sql.Date
     **/
    Date getMaxBirth(Connection conn);
}



