import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

class UserAccount {
    String name;
    int age;
    long salary;
    String pin; // use string to preserve leading zeros if any
    String mobile;
    String accountType;
    long balance;
    ArrayList<String> transactions;

    UserAccount(String name, int age, long salary, String pin, String mobile, String accountType) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.pin = pin;
        this.mobile = mobile;
        this.accountType = accountType;
        this.balance = 0L;
        this.transactions = new ArrayList<>();
    }

    void deposit(long amount) {
        balance += amount;
        transactions.add("Deposited ₹" + amount);
    }

    boolean withdraw(long amount) {
        if (amount > balance) return false;
        balance -= amount;
        transactions.add("Withdrew ₹" + amount);
        return true;
    }

    String getTransactionLog() {
        if (transactions.isEmpty()) return "No transactions yet.";
        StringBuilder sb = new StringBuilder();
        for (String t : transactions) {
            sb.append(t).append("\n");
        }
        return sb.toString();
    }
}

public class BankSystem extends JFrame implements ActionListener {
    // Data store: map PIN -> UserAccount
    private HashMap<String, UserAccount> users = new HashMap<>();
    private UserAccount currentUser = null;

    // Panels
    private JPanel createAccountPanel;
    private JPanel loginPanel;
    private JPanel userPanel;

    // Create account fields
    private JTextField nameField, ageField, salaryField, mobileField;
    private JPasswordField pinField;
    private JComboBox<String> accTypeCombo;
    private JButton createAccountButton;

    // Login fields
    private JTextField loginNameField;
    private JPasswordField loginPinField;
    private JButton loginButton;

    // User detail labels
    private JLabel nameLabel2, ageLabel2, salaryLabel2, mobileLabel2, accTypeLabel2, balanceLabel2;
    private JButton depositButton, withdrawButton, viewTransactionsButton, logoutButton;

    public BankSystem() {
        setTitle("Banking Management System");
        setSize(520, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(232, 240, 254));
        setLayout(new CardLayout());

        buildCreateAccountPanel();
        buildLoginPanel();
        buildUserPanel();

        // Start with create account panel visible (you can change to loginPanel if desired)
        add(createAccountPanel, "create");
        add(loginPanel, "login");
        add(userPanel, "user");

        showPanel("create");
    }

    private void buildCreateAccountPanel() {
        createAccountPanel = new JPanel(new GridBagLayout());
        createAccountPanel.setBackground(new Color(221, 238, 255));
        createAccountPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Create Account");
        heading.setFont(new Font("Helvetica", Font.BOLD, 20));
        heading.setForeground(new Color(0, 51, 102));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = new Insets(8, 8, 18, 8);
        createAccountPanel.add(heading, gbc);

        String[] labels = {"Name:", "Age:", "Salary:", "PIN (4 digits):", "Mobile:", "Account Type:"};
        JTextField[] fields = new JTextField[5];

        gbc.gridwidth = 1;
        gbc.insets = new Insets(6, 6, 6, 6);
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i + 1;
            createAccountPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            if (i == 0) { nameField = new JTextField(18); createAccountPanel.add(nameField, gbc); }
            else if (i == 1) { ageField = new JTextField(18); createAccountPanel.add(ageField, gbc); }
            else if (i == 2) { salaryField = new JTextField(18); createAccountPanel.add(salaryField, gbc); }
            else if (i == 3) { pinField = new JPasswordField(18); createAccountPanel.add(pinField, gbc); }
            else if (i == 4) { mobileField = new JTextField(18); createAccountPanel.add(mobileField, gbc); }
            else if (i == 5) {
                accTypeCombo = new JComboBox<>(new String[]{"Single", "Joint"});
                createAccountPanel.add(accTypeCombo, gbc);
            }
        }

        gbc.gridx = 0; gbc.gridy = labels.length + 1; gbc.gridwidth = 2; gbc.insets = new Insets(18, 6, 6, 6);
        createAccountButton = new JButton("Create Account");
        createAccountButton.setBackground(new Color(76, 175, 80));
        createAccountButton.setForeground(Color.WHITE);
        createAccountButton.setFocusPainted(false);
        createAccountButton.addActionListener(this);
        createAccountPanel.add(createAccountButton, gbc);
    }

    private void buildLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Login");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(new Color(0, 51, 102));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = new Insets(8, 8, 18, 8);
        loginPanel.add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);

        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        loginNameField = new JTextField(18);
        loginPanel.add(loginNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        loginPanel.add(new JLabel("PIN:"), gbc);
        gbc.gridx = 1;
        loginPinField = new JPasswordField(18);
        loginPanel.add(loginPinField, gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.insets = new Insets(16, 8, 8, 8);
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(33, 150, 243));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(this);
        loginPanel.add(loginButton, gbc);

        // Enter key triggers login
        loginPanel.registerKeyboardAction(e -> loginAction(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void buildUserPanel() {
        userPanel = new JPanel(new GridBagLayout());
        userPanel.setBackground(new Color(240, 255, 240));
        userPanel.setBorder(new EmptyBorder(18, 18, 18, 18));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Account Details");
        heading.setFont(new Font("Calibri", Font.BOLD, 20));
        heading.setForeground(new Color(0, 77, 64));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = new Insets(8, 8, 18, 8);
        userPanel.add(heading, gbc);

        nameLabel2 = new JLabel(); ageLabel2 = new JLabel(); salaryLabel2 = new JLabel();
        mobileLabel2 = new JLabel(); accTypeLabel2 = new JLabel(); balanceLabel2 = new JLabel();

        JLabel[] labels = new JLabel[]{nameLabel2, ageLabel2, salaryLabel2, mobileLabel2, accTypeLabel2, balanceLabel2};
        gbc.gridwidth = 2; gbc.insets = new Insets(6, 6, 6, 6);
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i + 1;
            userPanel.add(labels[i], gbc);
        }

        depositButton = new JButton("Deposit");
        withdrawButton = new JButton("Withdraw");
        viewTransactionsButton = new JButton("View Transaction Log");
        logoutButton = new JButton("Logout");

        depositButton.setBackground(new Color(76, 175, 80)); depositButton.setForeground(Color.WHITE);
        withdrawButton.setBackground(new Color(255, 152, 0)); withdrawButton.setForeground(Color.WHITE);
        viewTransactionsButton.setBackground(new Color(33, 150, 243)); viewTransactionsButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(244, 67, 54)); logoutButton.setForeground(Color.WHITE);

        depositButton.addActionListener(this);
        withdrawButton.addActionListener(this);
        viewTransactionsButton.addActionListener(this);
        logoutButton.addActionListener(this);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        btnPanel.setBackground(new Color(240, 255, 240));
        btnPanel.add(depositButton); btnPanel.add(withdrawButton);
        btnPanel.add(viewTransactionsButton); btnPanel.add(logoutButton);

        gbc.gridx = 0; gbc.gridy = labels.length + 1; gbc.gridwidth = 2; gbc.insets = new Insets(16, 6, 6, 6);
        userPanel.add(btnPanel, gbc);
    }

    private void showPanel(String name) {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), name);
    }

    // Action listener dispatch
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == createAccountButton) {
            createAccountAction();
        } else if (src == loginButton) {
            loginAction();
        } else if (src == depositButton) {
            depositAction();
        } else if (src == withdrawButton) {
            withdrawAction();
        } else if (src == viewTransactionsButton) {
            viewTransactionsAction();
        } else if (src == logoutButton) {
            logoutAction();
        }
    }

    private void createAccountAction() {
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String salaryText = salaryField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();
        String mobile = mobileField.getText().trim();
        String accType = (String) accTypeCombo.getSelectedItem();

        if (name.isEmpty() || ageText.isEmpty() || salaryText.isEmpty() || pin.isEmpty() || mobile.isEmpty() || accType == null) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ageText.matches("\\d+") || !salaryText.matches("\\d+") || !pin.matches("\\d{4}") || !mobile.matches("\\d{7,15}")) {
            JOptionPane.showMessageDialog(this, "Invalid input:\n- Age and Salary must be numbers\n- PIN must be exactly 4 digits\n- Mobile must be numeric (7-15 digits)",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (users.containsKey(pin)) {
            JOptionPane.showMessageDialog(this, "An account with this PIN already exists. Choose a different PIN.", "Duplicate PIN", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int age = Integer.parseInt(ageText);
        long salary = Long.parseLong(salaryText);

        UserAccount user = new UserAccount(name, age, salary, pin, mobile, accType);
        users.put(pin, user);

        // clear fields
        nameField.setText("");
        ageField.setText("");
        salaryField.setText("");
        pinField.setText("");
        mobileField.setText("");
        accTypeCombo.setSelectedIndex(0);

        JOptionPane.showMessageDialog(this, "Account created for " + name + "!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // show login panel
        showPanel("login");
    }

    private void loginAction() {
        String name = loginNameField.getText().trim();
        String pin = new String(loginPinField.getPassword()).trim();

        if (name.isEmpty() || pin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter both name and PIN.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!users.containsKey(pin) || !users.get(pin).name.equals(name)) {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentUser = users.get(pin);
        updateUserDetails();
        showPanel("user");
    }

    private void updateUserDetails() {
        if (currentUser == null) return;
        nameLabel2.setText("Name: " + currentUser.name);
        ageLabel2.setText("Age: " + currentUser.age);
        salaryLabel2.setText("Salary: ₹" + currentUser.salary);
        mobileLabel2.setText("Mobile: " + currentUser.mobile);
        accTypeLabel2.setText("Account Type: " + currentUser.accountType);
        balanceLabel2.setText("Current Balance: ₹" + currentUser.balance);
    }

    private void depositAction() {
        if (currentUser == null) return;
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to deposit:", "Deposit", JOptionPane.PLAIN_MESSAGE);
        if (amountStr == null) return; // canceled
        amountStr = amountStr.trim();
        if (!amountStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Invalid amount. Enter a positive integer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        long amount = Long.parseLong(amountStr);
        if (amount <= 0) {
            JOptionPane.showMessageDialog(this, "Amount must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        currentUser.deposit(amount);
        updateUserDetails();
        JOptionPane.showMessageDialog(this, "₹" + amount + " deposited successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void withdrawAction() {
        if (currentUser == null) return;
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw:", "Withdraw", JOptionPane.PLAIN_MESSAGE);
        if (amountStr == null) return; // canceled
        amountStr = amountStr.trim();
        if (!amountStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Invalid amount. Enter a positive integer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        long amount = Long.parseLong(amountStr);
        if (amount <= 0) {
            JOptionPane.showMessageDialog(this, "Amount must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!currentUser.withdraw(amount)) {
            JOptionPane.showMessageDialog(this, "Insufficient balance.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        updateUserDetails();
        JOptionPane.showMessageDialog(this, "₹" + amount + " withdrawn successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewTransactionsAction() {
        if (currentUser == null) return;
        JTextArea area = new JTextArea(currentUser.getTransactionLog());
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, sp, "Transaction Log for " + currentUser.name, JOptionPane.INFORMATION_MESSAGE);
    }

    private void logoutAction() {
        currentUser = null;
        loginNameField.setText("");
        loginPinField.setText("");
        showPanel("login");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankSystem frame = new BankSystem();
            frame.setVisible(true);
        });
    }
}
