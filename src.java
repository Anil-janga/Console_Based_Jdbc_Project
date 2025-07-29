package console_project;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class sample {
	public static void main(String[] args) {
        Connection con = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psDelete = null;
        PreparedStatement psSelect = null;
        PreparedStatement psLogin = null;
        Scanner sc = new Scanner(System.in);

        String insertQuery = "INSERT INTO user VALUES (?, ?, ?, ?, ?)";
        String updateQuery = "UPDATE user SET phone = ?, address = ? WHERE email= ?";
        String deleteQuery = "DELETE FROM user WHERE email = ?";
        String selectQuery = "SELECT * FROM user WHERE email= ?";
        String loginQuery = "SELECT * FROM user WHERE email = ? AND password = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/usermanagementsystem", "root", "root");

            while (true) {
                System.out.println("Choose option");
                System.out.println("1. Login \t 2. Create Account \t 3. Exit");
                int option = sc.nextInt();
                sc.nextLine(); // consume leftover newline

                if (option == 1) {
                    psLogin = con.prepareStatement(loginQuery);
                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter Password: ");
                    String password = sc.nextLine();
                    psLogin.setString(1, email);
                    psLogin.setString(2, password);

                    ResultSet rsLogin = psLogin.executeQuery();
                    if (rsLogin.next()) {
                        System.out.println("Login Successful!");

                        System.out.println("Select action:");
                        System.out.println("1. Check Account Exists");
                        System.out.println("2. Update Profile");
                        System.out.println("3. View Profile");
                        System.out.println("4. Delete Account");
                        System.out.println("5. Logout");
                        int key = sc.nextInt();
                        sc.nextLine(); // consume leftover newline

                        switch (key) {
                            case 1:
                                System.out.print("Enter Email to check: ");
                                String checkEmail = sc.nextLine();
                                psSelect = con.prepareStatement(selectQuery);
                                psSelect.setString(1, checkEmail);
                                ResultSet rsCheck = psSelect.executeQuery();
                                if (rsCheck.next()) {
                                    System.out.println("Account found!");
                                } else {
                                    System.out.println("Account does not exist.");
                                }
                                break;

                            case 2:
                                psUpdate = con.prepareStatement(updateQuery);
                                System.out.print("Enter new phone number: ");
                                long phone = sc.nextLong();
                                sc.nextLine();
                                System.out.print("Enter new address: ");
                                String address = sc.nextLine();
                                System.out.print("Enter your email to update: ");
                                String updateEmail = sc.nextLine();

                                psUpdate.setLong(1, phone);
                                psUpdate.setString(2, address);
                                psUpdate.setString(3, updateEmail);
                                int rowsUpdated = psUpdate.executeUpdate();
                                System.out.println(rowsUpdated + " record(s) updated.");
                                break;

                            case 3:
                                psSelect = con.prepareStatement(selectQuery);
                                System.out.print("Enter email to view: ");
                                String viewEmail = sc.nextLine();
                                psSelect.setString(1, viewEmail);
                                ResultSet rsView = psSelect.executeQuery();
                                if (rsView.next()) {
                                    System.out.println("Email: " + rsView.getString(1));
                                    System.out.println("Password: " + rsView.getString(2));
                                    System.out.println("Name: " + rsView.getString(3));
                                    System.out.println("Address: " + rsView.getString(4));
                                    System.out.println("Phone Number: " + rsView.getLong(5));
                                } else {
                                    System.out.println("No user found.");
                                }
                                break;

                            case 4:
                                psDelete = con.prepareStatement(deleteQuery);
                                System.out.print("Enter email to delete: ");
                                String delEmail = sc.nextLine();
                                psDelete.setString(1, delEmail);
                                int rowsDeleted = psDelete.executeUpdate();
                                System.out.println(rowsDeleted + " record(s) deleted.");
                                break;

                            case 5:
                                System.out.println("Logging out...");
                                break;

                            default:
                                System.out.println("Invalid option.");
                                break;
                        }
                    } else {
                        System.out.println("Invalid login credentials.");
                    }

                } else if (option == 2) {
                    psInsert = con.prepareStatement(insertQuery);
                    System.out.print("Enter Email: ");
                    String newEmail = sc.nextLine();
                    System.out.print("Enter Password: ");
                    String newPassword = sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Address: ");
                    String newAddress = sc.nextLine();
                    System.out.print("Enter Phone Number: ");
                    long phone = sc.nextLong();

                    psInsert.setString(1, newEmail);
                    psInsert.setString(2, newPassword);
                    psInsert.setString(3, name);
                    psInsert.setString(4, newAddress);
                    psInsert.setLong(5, phone);
                    psInsert.executeUpdate();
                    System.out.println("Account created successfully.");

                } else if (option == 3) {
                    System.out.println("Exiting program.");
                    break;
                } else {
                    System.out.println("Invalid option. Try again.");
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (psInsert != null) psInsert.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (psUpdate != null) psUpdate.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (psDelete != null) psDelete.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (psSelect != null) psSelect.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (psLogin != null) psLogin.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
            sc.close();
        }
    }
}
