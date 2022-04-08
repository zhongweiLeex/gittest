package testtransaction;

import org.junit.jupiter.api.Test;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        Connection conn = JDBCUtils.getConnection();
        try {

            //2. 开启事务 关闭自动提交
            conn.setAutoCommit(false);

            //3. 操作数据库
            String sql = "update user_table set balance = balance-100 where user = ?";
            update(conn,sql,"AA");

            //模拟网络异常
            System.out.println(10/0);

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
            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeConnection(null,ps);
        }


    }
}
