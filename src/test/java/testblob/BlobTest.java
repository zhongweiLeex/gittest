package testblob;

import bean.Customer;
import org.junit.jupiter.api.Test;
import util.JDBCUtils;

import java.io.*;
import java.sql.*;

/**
 * @ClassName BlobTest
 * @Description 测试使用PreparedStatement操作Blob类型数据
 * @Author Administrator
 * @Date 2022/4/8 16:22
 * @Version 1.0
 **/
public class BlobTest {
    @Test
    /* *
     * @Author zhongweiLee
     * @Description 插入Blob字段数据
     * @Date 16:42 2022/4/8
     * @Param []
     * @return void
     **/
    public void testInsert(){
        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";
        PreparedStatement ps = null;
        FileInputStream is = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setObject(1,"lizhgonwg");
            ps.setObject(2,"lon@Gamil.com");
            ps.setObject(3,"1998-10-28");
           is = new FileInputStream(new File("D:\\b.jpg"));
            ps.setBlob(4,is);
            ps.execute();
        } catch (SQLException | FileNotFoundException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeConnection(conn,ps);
        }
    }
    @Test
    /* *
     * @Author zhongweiLee
     * @Description 查询Blob字段数据
     * @Date 17:05 2022/4/8
     * @Param []
     * @return void
     **/
    public void testQuery(){
        Connection connection = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth,photo from customers where id = ?";

        PreparedStatement ps = null;
        FileOutputStream fileOutputStream = null;
        InputStream is = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1,16);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

                Customer customer = new Customer(id, name, email, birth);

                Blob blob = resultSet.getBlob(5);
                is = blob.getBinaryStream();//从数据库输入到 内存中
                //建立一个输出流 从内存输出到本地文件中
                 fileOutputStream = new FileOutputStream(new File("testpicture.jpg"));

                byte[] buffer = new byte[1024];
                int len;
                while((len=is.read(buffer))!=-1){
                    fileOutputStream.write(buffer,0,len);
                }
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeConnection(connection,ps);
        }

    }

}
















































