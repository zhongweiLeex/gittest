package bean;


import java.sql.Date;

/**
 * @ClassName Customer
 * @Description ORM编程思想 （object relational mapping 对象关系映射思想） 一个数据表对应一个类 ， 一个字段对应一个属性
 * @Author Administrator
 * @Date 2022/4/7 19:55
 * @Version 1.0
 *
 * | Java类型           | SQL类型                  |
 * | ------------------ | ------------------------ |
 * | boolean            | BIT                      |
 * | byte               | TINYINT                  |
 * | short              | SMALLINT                 |
 * | int                | INTEGER                  |
 * | long               | BIGINT                   |
 * | String             | CHAR,VARCHAR,LONGVARCHAR |
 * | byte   array       | BINARY  ,    VAR BINARY  |
 * | java.sql.Date      | DATE                     |
 * | java.sql.Time      | TIME                     |
 * | java.sql.Timestamp | TIMESTAMP                |
 **/
public class Customer {
    private int id;
    private String name;
    private String email;
    private Date birth;

    public Customer() {
        super();
    }

    public Customer(int id, String name, String email, Date birth) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                '}';
    }
}

