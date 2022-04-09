package bean;

/**
 * @ClassName User
 * @Description User类
 * @Author Administrator
 * @Date 2022/4/8 14:41
 * @Version 1.0
 **/
public class User {
    private int id;
    private String name;
    private String address;
    private String password;
    private String phone;
    public User() {
    }

    public User(int id, String name, String address, String password, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.password = password;
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
