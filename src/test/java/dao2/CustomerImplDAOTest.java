package dao2;

import bean.Customer;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

class CustomerImplDAOTest {
    private CustomerImplDAO dao = new CustomerImplDAO();
    @Test
    void insert() {
        Connection conn = JDBCUtils.getConnection1();

        try {
            String date = "2010-12-22T10:49:18+08:00";
            DateTime dt = new DateTime();
            dt = DateTime.parse(date);
            dao.insert(conn,new Customer(1,"testname","testemail@Gmail",new Date(dt.getMillis())));
            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeConnection(conn,null);
        }

    }

    @Test
    void deleteById() {
        Connection conn = JDBCUtils.getConnection1();

        try {
            dao.deleteById(conn,22);
            System.out.println("删除成功");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeConnection(conn,null);
        }
    }

    @Test
    void update() {
        Connection conn = JDBCUtils.getConnection1();

        try {

            String date = "1998-9-9";
            DateTime dt = new DateTime();
            dt = DateTime.parse(date);

            Customer customer = new Customer(20,"lizhongwei","lzw10033@Gmail",new Date(dt.getMillis()));
            dao.update(conn,customer);

            System.out.println("更新成功");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeConnection(conn,null);
        }
    }

    @Test
    void getCustomerById() {
        Connection conn = JDBCUtils.getConnection1();

        try {
            Customer customer = dao.getCustomerById(conn, 20);
            System.out.println(customer);
            System.out.println("获取成功");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeConnection(conn,null);
        }
    }

    @Test
    void getAll() {
        Connection conn = JDBCUtils.getConnection1();

        try {
            List<Customer> list = dao.getAll(conn);
            list.forEach(System.out::println);
            System.out.println("获取成功");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeConnection(conn,null);
        }
    }

    @Test
    void getCount() {
        Connection conn = JDBCUtils.getConnection1();

        try {
            Long count = dao.getCount(conn);
            System.out.println(count);
            System.out.println("打印成功");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeConnection(conn,null);
        }
    }

    @Test
    void getMaxBirth() {
        Connection conn = JDBCUtils.getConnection1();

        try {
            Date maxBirth = dao.getMaxBirth(conn);
            System.out.println(maxBirth);
            System.out.println("获取成功");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeConnection(conn,null);
        }
    }
}