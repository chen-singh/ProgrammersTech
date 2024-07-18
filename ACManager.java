package chen;

 import java.math.BigDecimal;
import java.sql.*;
        import java.util.Scanner;

public class ACManager {
    private Connection con;
    private Scanner sc;
    ACManager(Connection connection, Scanner scanner){
        this.con = connection;
        this.sc = scanner;
    }


    public void deposit(String accountnumber)throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        int amount = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        int pin = sc.nextInt();

        try {
            con.setAutoCommit(false);
            if(!accountnumber.equals("0")) {
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM accounts WHERE accountnumber = ? and pin = ? ");
                preparedStatement.setString(1, accountnumber);
                preparedStatement.setInt(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String credit_query = "UPDATE accounts SET balance = balance + ? WHERE accountnumber = ?";
                    PreparedStatement preparedStatement1 = con.prepareStatement(credit_query);
                    preparedStatement1.setInt(1, amount);
                    preparedStatement1.setString(2, accountnumber);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Rs."+amount+" credited Successfully");
                        con.commit();
                        con.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed!");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                }else{
                    System.out.println("Invalid Security Pin!");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

    public void withdraw(String accountnumber) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        int amount = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        int pin = sc.nextInt();
        try {
            con.setAutoCommit(false);
            if(!accountnumber.equals("0")) {
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM accounts WHERE accountnumber = ? and pin = ? ");
                preparedStatement.setString(1, accountnumber);
                preparedStatement.setInt(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount<=current_balance){
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE accountnumber = ?";
                        PreparedStatement preparedStatement1 = con.prepareStatement(debit_query);
                        preparedStatement1.setInt(1, amount);
                        preparedStatement1.setString(2, accountnumber);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Rs."+amount+" debited Successfully");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient Balance!");
                    }
                }else{
                    System.out.println("Invalid Pin!");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

    public void transfer_money(String sender_accountnumber) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        String receiver_account_number = sc.nextLine();
        System.out.print("Enter Amount: ");
        int amount = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        int pin = sc.nextInt();
        try{
            con.setAutoCommit(false);
            if(!sender_accountnumber.equals("0") && !receiver_account_number.equals("0")){
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM accounts WHERE accountnumber = ? AND pin = ? ");
                preparedStatement.setString(1, sender_accountnumber);
                preparedStatement.setInt(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    int current_balance = resultSet.getInt("balance");
                    if (amount<=current_balance){

                        // Write debit and credit queries
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE accountnumber = ?";
                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE accountnumber = ?";

                        // Debit and Credit prepared Statements
                        PreparedStatement creditPreparedStatement = con.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStatement = con.prepareStatement(debit_query);

                        // Set Values for debit and credit prepared statements
                        creditPreparedStatement.setInt(1, amount);
                        creditPreparedStatement.setString(2, receiver_account_number);
                        debitPreparedStatement.setInt(1, amount);
                        debitPreparedStatement.setString(2, sender_accountnumber);
                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
                        int rowsAffected2 = creditPreparedStatement.executeUpdate();
                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                            System.out.println("Transaction Successful!");
                            System.out.println("Rs."+amount+" Transferred Successfully");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient Balance!");
                    }
                }else{
                    System.out.println("Invalid Security Pin!");
                }
            }else{
                System.out.println("Invalid account number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

    public void getBalance(String accountnumber){
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();
        try{
            PreparedStatement preparedStatement = con.prepareStatement("SELECT balance FROM accounts WHERE accountnumber = ? AND pin = ?");
            preparedStatement.setString(1, accountnumber);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance: "+balance);
            }else{
                System.out.println("Invalid Pin!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
