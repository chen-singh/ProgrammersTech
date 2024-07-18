package chen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;


public class BankApp {

private static final String url="jdbc:mysql://localhost:3306/banking";
 private static final String user="root";
private static final String password="chen919191";
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Scanner sc = new Scanner(System.in);
            User u = new User(con, sc);
            Accounts accounts = new Accounts(con, sc);
            ACManager am = new ACManager(con, sc);

            String accountnumber;
            while (true) {
                System.out.println("*** WELCOME TO BANKING SYSTEM *** ");
                System.out.println();
                System.out.println("1. Register\n2. Login\n3. Exit ");
                int choice1=sc.nextInt();
                switch (choice1){
                    case 1:
                        u.Register();
                        break;
                    case 2:
                        accountnumber = u.Login();
                        if(accountnumber!=null){
                            System.out.println();
                            System.out.println("User Logged In!");
                            if(!accounts.account_exist(accountnumber)){
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                if(sc.nextInt() == 1) {
                                    accountnumber = accounts.openAccount(accountnumber);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is: " + accountnumber);
                                }else{
                                    break;
                                }

                            }
                            accountnumber = accounts.getAccount_number(accountnumber);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.print("1. Deposit Money\n 2. Withdraw Money\n 3. Transfer Money\n 4. Check Balance\n 5. Log Out\n" );

                                System.out.println("Enter your choice: ");
                                choice2 = sc.nextInt();
                                switch (choice2) {
                                    case 1:
                                        am.deposit(accountnumber);
                                        break;
                                    case 2:
                                        am.withdraw(accountnumber);
                                        break;
                                    case 3:
                                       am.transfer_money(accountnumber);
                                        break;
                                    case 4:
                                        am.getBalance(accountnumber);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Correct Choice!");
                                        break;
                                }
                            }

                        }
                        else{
                            System.out.println("Incorrect Account number or Password!");
                        }
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("You logged out from System!");
                        return;
                    default:
                        System.out.println("Enter Correct Choice");
                        break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
            }


