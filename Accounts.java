package chen;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection con;
    private Scanner sc;

    public Accounts(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public boolean account_exist(String accountnumber) {
        String sql = "select accountnumber from accounts where accountnumber=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, accountnumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public String getAccount_number(String accountnumber) {
        String query = "SELECT accountnumber from accounts WHERE accountnumber = ?";
        try{
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, accountnumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("accountnumber");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account Number Doesn't Exist!");
    }

    /*private String generateAccountnumber(){
        try
        {
            Statement stm= con.createStatement();
            ResultSet rs=stm.executeQuery("select accountnumber from accounts order by accountnumber desc limit 1");
        if (rs.next()){
            String lastaccountnumber= rs.getString("accountnumber");
           return lastaccountnumber;
        }else {
            return null;
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }*/
    public String openAccount(String accountnumber) {
        if (!account_exist(accountnumber)) ;
        String sql = "insert into accounts(accountnumber ,name ,balance, pin) values (?,?,?,?)";
        sc.nextLine();
        System.out.println("enter accountnumber");
        accountnumber = sc.nextLine();
        System.out.println("enter your name");
        String ful_name = sc.nextLine();
        System.out.println("enter balance");
        int balance = sc.nextInt();
        System.out.println("enter pin");
        int pin = sc.nextInt();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, accountnumber);
            ps.setString(2, ful_name);
            ps.setInt(3, balance);
            ps.setInt(4, pin);
            int affectrows = ps.executeUpdate();
            if (affectrows > 0) {
                return accountnumber;
            } else {
                throw new RuntimeException("account creation fail");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Account already exist");
    }
}

