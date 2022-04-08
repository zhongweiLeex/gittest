package testbatch;

import org.junit.jupiter.api.Test;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @ClassName InsertTest
 * @Description
 * 使用PreparedStatement 实现批量操作数据
 * 1. update 与 delete 本身就具有批量操作的效果
 * 2. insert 批量操作 演示， 使用preparedStatement 实现更加高效的批量插入
 *
 * 题目： 向goods表中插入 2w条数据
 * 方式1： 使用statement 形式                      最慢
 * 方式2： 使用preparedStatement 循环形式插入       较慢
 * 方式3：
 * @Author Administrator
 * @Date 2022/4/8 19:04
 * @Version 1.0
 **/
public class InsertTest {
    @Test
    /* *
     * @Author zhongweiLee
     * @Description 批量插入方式2  22230ms
     *          简单循环
     * @Date 19:16 2022/4/8
     * @Param []
     * @return void
     **/
    public void insert(){
        Connection connection = JDBCUtils.getConnection();
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            String sql = "insert into goods(name)values(?)";
            ps = connection.prepareStatement(sql);
            for (int i = 0; i <=20000; i++) {
                ps.setObject(1,"name_"+i);
                ps.execute();
            }
            System.out.println(System.currentTimeMillis()-start);//22230
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeConnection(connection,ps);
        }
    }
    //批量插入方式3：
    /* *
     * @Author zhongweiLee
     * @Description 批量插入方式3 ：243ms
     *              1. addBatch()       攒sql
     *              2. executeBatch()   执行batch
     *              3. clearBatch()     清空batch
     * @Date 19:23 2022/4/8
     * @Param []
     * @return void
     **/
    @Test
    public void insert2(){
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <=20000; i++) {
                ps.setObject(1,"name_"+i);

                //1.攒 sql
                ps.addBatch();
                if (i%500 == 0){
                    //2。执行batch
                    ps.executeBatch();

                    //3. 清空batch
                    ps.clearBatch();
                }
            }
            System.out.println(System.currentTimeMillis()-start);//22230
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeConnection(conn,ps);
        }
    }

    /* *
     * @Author zhongweiLee
     * @Description 批量插入方式4：终极方案  6351ms  100w条数据
     *              1. 设置不允许自动提交数据 ， 所有数据写入完成后再提交数据 conn.setAutoCommit(false);
     *
     * @Date 19:36 2022/4/8
     * @Param []
     * @return void
     **/
    @Test
    public void insert3(){
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement ps = null;
        try {
            //设置不允许自动提交数据
            conn.setAutoCommit(false);
            long start = System.currentTimeMillis();
            String sql = "insert into goods(name)values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <=1000000; i++) {
                ps.setObject(1,"name_"+i);

                //1.攒 sql
                ps.addBatch();
                if (i%500 == 0){
                    //2。执行batch
                    ps.executeBatch();

                    //3. 清空batch
                    ps.clearBatch();
                }
            }
            //提交数据
            conn.commit();
            System.out.println(System.currentTimeMillis()-start);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeConnection(conn,ps);
        }
    }
}











































