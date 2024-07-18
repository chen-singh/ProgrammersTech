package chen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.stream.Stream;

public class User {
    private Connection con;
    private Scanner sc;

    public User(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public void Register() {
        sc.nextLine();
        System.out.println("Email");
        String email = sc.nextLine();
        System.out.println("Acountnumber");
        String accountnumber = sc.nextLine();
        System.out.println("password");
        String password = sc.nextLine();
        if (user_exist(accountnumber)) {
            System.out.println("user exists");
            return;
        }
        String sql = "insert into user(email,accountnumber,password) values(?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, accountnumber);
            ps.setString(3, password);
            int affectrows = ps.executeUpdate();
            if (affectrows > 0) {
                System.out.println("register success");
            } else {
                System.out.println("fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String Login() {
        sc.nextLine();
        System.out.println("accountnumber");
        String accountnumber = sc.nextLine();
        System.out.println("password");
        String password = sc.nextLine();
        String sql = "select * from User where accountnumber = ? and password = ? ";
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, accountnumber);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return accountnumber;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }return null;
    }
    public boolean user_exist(String accountnumber){
        String query="select * from User where accountnumber= ?";
        try {
            PreparedStatement ps= con.prepareStatement(query);
            ps.setString(1,accountnumber);
            ResultSet rst= ps.executeQuery();
            if (rst.next()){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;

    }
}
