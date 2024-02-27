import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;

class User {
    String name;
    int accountNumber;
    double balance;

    public User(String name, int accountNumber, double balance) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit of Le" + amount + " successful.");
        } else {
            System.out.println("Invalid deposit amount. Please enter a positive amount.");
        }
    }
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawal of Le" + amount + " successful.");
            return true;
        } else if (amount <= 0) {
            System.out.println("Invalid withdrawal amount. Please enter a positive amount.");
            return false;
        } else {
            System.out.println("Insufficient balance for withdrawal.");
            return false;
        }
    }
}

class Account {
    User[] users;
    int totalUsers;

    private int lastAssignedAccountNumber = 1000;

    public Account() {
        users = new User[100];
        totalUsers = 0;
    }

    public int generateUniqueAccountNumber() {
        lastAssignedAccountNumber++;
        return lastAssignedAccountNumber;
    }

    public void addUser(User user) {
        if (totalUsers >= users.length) {
            users = Arrays.copyOf(users, users.length * 2);
        }
        user.accountNumber = generateUniqueAccountNumber();
        users[totalUsers] = user;
        totalUsers++;
    }

    public User findUserByName(String name) {
        for (int i = 0; i < totalUsers; i++) {
            if (users[i].name.equalsIgnoreCase(name)) {
                return users[i];
            }
        }
        return null;
    }
    public User findUserByAccountNumber(int accountNumber) {
        for (int i = 0; i < totalUsers; i++) {
            if (users[i].accountNumber == accountNumber) {
                return users[i];
            }
        }
        return null;
    }
}

public class Bankingapplication_1 {
    public static void main(String[] args) {
        Account bank = loadDataFromFile();

        while (true) {
            String[] options = {
                "Create Account",
                "Deposit",
                "Withdraw",
                "Transfer",
                "Check Account",
                "Exit",
                "All Users"
            };

            int choice = showOptionDialog(
                null,
                "Select an option:",
                "WELCOME TO LIMBANK",
                options
            );

            switch (choice) {
                case 0:
                    createAccount(bank);
                    break;

                case 1:
                    depositFunds(bank);
                    break;

                case 2:
                    withdrawFunds(bank);
                    break;

                case 3:
                    transferFunds(bank);
                    break;

                case 4:
                    checkAccount(bank);
                    break;

                case 5:
                    saveDataToFile(bank);
                    JOptionPane.showMessageDialog(null, "Exiting the application.");
                    System.exit(0);
                    break;
                case 6:
                    viewAllUsers(bank);
                    break;
            }
        }
    }
    private static int showOptionDialog(Component parentComponent, Object message, String title, Object[] options) {
        CustomDialog dialog = new CustomDialog(parentComponent, message, title, options);
        dialog.setVisible(true);

        return dialog.getResult();
    }

    private static class CustomDialog extends JDialog {
        private int result;

        public CustomDialog(Component parentComponent, Object message, String title, Object[] options) {
            super((Frame) parentComponent, title, true);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JLabel label = new JLabel(message.toString());
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);

            for (Object option : options) {
                JButton button = new JButton(option.toString());
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        result = Arrays.asList(options).indexOf(option);
                        dispose();
                    }
                });
                panel.add(button);
            }

            // Set layout
            setLayout(new BorderLayout());
            add(panel, BorderLayout.CENTER);

            // Set the size of the dialog to the screen size
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) screenSize.getWidth();
            int height = (int) screenSize.getHeight();
            setSize(width, height);

            // Center the dialog
            setLocationRelativeTo(parentComponent);
        }

        public int getResult() {
            return result;
        }
    }
    private static void viewAllUsers(Account bank) {
        StringBuilder userList = new StringBuilder("All Users:\n\n");
        for (int i = 0; i < bank.totalUsers; i++) {
            User user = bank.users[i];
            userList.append("Name: ").append(user.name).append("\n");
            userList.append("Account Number: ").append(user.accountNumber).append("\n");
            userList.append("Balance: $").append(user.balance).append("\n");
            userList.append("\n");
        }
        JOptionPane.showMessageDialog(null, userList.toString(), "All Users", JOptionPane.INFORMATION_MESSAGE);
    }
    private static User findUserByAccountNumber(Account bank, int accountNumber) {
        for (int i = 0; i < bank.totalUsers; i++) {
            if (bank.users[i].accountNumber == accountNumber) {
                return bank.users[i];
            }
        }
        return null;
    }

    private static void createAccount(Account bank) {
        String name = JOptionPane.showInputDialog("Enter the user's name:");
        if (name != null && !name.isEmpty()) {
            double balance = Double.parseDouble(JOptionPane.showInputDialog("Enter the initial balance:"));
            User newUser = new User(name, 0, balance); // Account number will be assigned automatically
            bank.addUser(newUser);
            JOptionPane.showMessageDialog(null, "Account created successfully!");
        }
    }

    private static void depositFunds(Account bank) {
        String accountNumberInput = JOptionPane.showInputDialog("Enter the account number:");
        int accountNumber = Integer.parseInt(accountNumberInput);
    
        User user = findUserByAccountNumber(bank, accountNumber);
    
        if (user != null) {
            String depositAmountInput = JOptionPane.showInputDialog("Enter the deposit amount:");
            double depositAmount = Double.parseDouble(depositAmountInput);
    
            if (depositAmount > 0) {
                user.deposit(depositAmount);
                JOptionPane.showMessageDialog(null, "Deposit of Le" + depositAmount + " successful.");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid deposit amount. Please enter a positive amount.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "User not found. Please check the account number.");
        }
    }
    
    private static void withdrawFunds(Account bank) {
        String accountNumberInput = JOptionPane.showInputDialog("Enter the account number:");
        int accountNumber = Integer.parseInt(accountNumberInput);
    
        User user = findUserByAccountNumber(bank, accountNumber);
    
        if (user != null) {
            String withdrawalAmountInput = JOptionPane.showInputDialog("Enter the withdrawal amount:");
            double withdrawalAmount = Double.parseDouble(withdrawalAmountInput);
    
            if (withdrawalAmount > 0) {
                if (user.withdraw(withdrawalAmount)) {
                    JOptionPane.showMessageDialog(null, "Withdrawal of Le" + withdrawalAmount + " successful.");
                } else {
                    JOptionPane.showMessageDialog(null, "Insufficient balance for withdrawal.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid withdrawal amount. Please enter a positive amount.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "User not found. Please check the account number.");
        }
    }
    
    private static void transferFunds(Account bank) {
        String senderAccountNumberInput = JOptionPane.showInputDialog("Enter your account number:");
        int senderAccountNumber = Integer.parseInt(senderAccountNumberInput);
    
        User sender = findUserByAccountNumber(bank, senderAccountNumber);
    
        if (sender != null) {
            String recipientAccountNumberInput = JOptionPane.showInputDialog("Enter the recipient's account number:");
            int recipientAccountNumber = Integer.parseInt(recipientAccountNumberInput);
    
            User recipient = findUserByAccountNumber(bank, recipientAccountNumber);
    
            if (recipient != null) {
                String transferAmountInput = JOptionPane.showInputDialog("Enter the transfer amount:");
                double transferAmount = Double.parseDouble(transferAmountInput);
    
                if (transferAmount > 0) {
                    if (sender.withdraw(transferAmount)) {
                        recipient.deposit(transferAmount);
                        JOptionPane.showMessageDialog(null, "Transfer of Le" + transferAmount + " successful.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Insufficient balance for transfer.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid transfer amount. Please enter a positive amount.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Recipient not found. Please check the recipient's account number.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Sender not found. Please check your account number.");
        }
    }
    
    private static void checkAccount(Account bank) {
        String[] options = {"By Account Number", "By User Name"};

        int choice = JOptionPane.showOptionDialog(
            null,
            "Select how you want to check the account:",
            "Check Account",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice == 0) {
            // Check by account number
            checkAccountByAccountNumber(bank);
        } else if (choice == 1) {
            // Check by user name
            checkAccountByName(bank);
        }
    }

    private static void checkAccountByAccountNumber(Account bank) {
        String accountNumberInput = JOptionPane.showInputDialog("Enter the account number:");
        int accountNumber = Integer.parseInt(accountNumberInput);

        User user = bank.findUserByAccountNumber(accountNumber);

        if (user != null) {
            displayAccountDetails(user);
        } else {
            JOptionPane.showMessageDialog(null, "User not found. Please check the account number.");
        }
    }

    private static void checkAccountByName(Account bank) {
        String name = JOptionPane.showInputDialog("Enter the user's name:");
        User user = bank.findUserByName(name);

        if (user != null) {
            displayAccountDetails(user);
        } else {
            JOptionPane.showMessageDialog(null, "User not found. Please check the user's name.");
        }
    }

    private static void displayAccountDetails(User user) {
        String accountInfo = "Name: " + user.name + "\nAccount Number: " + user.accountNumber + "\nBalance: Le" + user.balance;
        JOptionPane.showMessageDialog(null, accountInfo, "Account Details", JOptionPane.INFORMATION_MESSAGE);
    }
     
    

    private static void saveDataToFile(Account bank) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("user_data.txt"))) {
            for (int i = 0; i < bank.totalUsers; i++) {
                User user = bank.users[i];
                writer.println("Name: " + user.name);
                writer.println("Account Number: " + user.accountNumber);
                writer.println("Balance: Le"+ user.balance);
                writer.println(); // Blank line to separate entries
            }
            JOptionPane.showMessageDialog(null, "User data saved to file.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving user data to file: " + e.getMessage());
        }
    }

    private static Account loadDataFromFile() {
        Account bank = new Account();
        try (BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"))) {
            String line;
            String name = null;
            int accountNumber = 0;
            double balance = 0;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name: ")) {
                    name = line.substring("Name: ".length());
                } else if (line.startsWith("Account Number: ")) {
                    accountNumber = Integer.parseInt(line.substring("Account Number: ".length()));
                } else if (line.startsWith("Balance: Le")) {
                    balance = Double.parseDouble(line.substring("Balance: Le".length()));
                } else if (line.isEmpty()) {
                    if (name != null) {
                        User user = new User(name, accountNumber, balance);
                        bank.addUser(user);
                    }
                    name = null;
                    accountNumber = 0;
                    balance = 0;
                }
            }

            JOptionPane.showMessageDialog(null, "User data loaded from file.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading user data from file: " + e.getMessage());
        }
        return bank;
    }
}
