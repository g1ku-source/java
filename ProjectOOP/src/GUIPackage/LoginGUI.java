package GUIPackage;

import DatabasePackage.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame {

    private final JButton registerButton  = new JButton("Register");
    private final JButton loginButton = new JButton("Login");

    private JTextField userText = new JTextField();
    private JPasswordField passwordText = new JPasswordField();

    private final JLabel userLabel = new JLabel("Username: ");
    private final JLabel passwordLabel = new JLabel("Password: ");

    private final DatabaseConnection databaseConnection;

    private String username = null;
    private String password = null;

    public LoginGUI(DatabaseConnection databaseConnection){

        this.databaseConnection = databaseConnection;

        setTitle("Login window");

        setSize(400,200);

        setLocation(new Point(500,500));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        initComponent();
        initEvent();
    }

    private void initComponent(){

        registerButton.setBounds(275,130, 100,25);
        loginButton.setBounds(275,100, 100,25);

        userText.setBounds(100,10,100,20);
        passwordText.setBounds(100,35,100,20);

        userLabel.setBounds(20,10,100,20);
        passwordLabel.setBounds(20,35,100,20);

        add(registerButton);
        add(loginButton);

        add(userLabel);
        add(passwordLabel);

        add(userText);
        add(passwordText);
    }

    private void initEvent(){

        registerButton.addActionListener(this::registerButtonPressed);

        loginButton.addActionListener(this::loginButtonPressed);
    }

    private boolean checkPassword(){

        return password != null;
    }

    private boolean checkUsername(){

        return username != null;
    }

    private boolean checkCharacters(){

        boolean capitalLetter = false;
        boolean symbol = false;
        boolean number = false;
        boolean letter = false;

        for(int i = 0; i < password.length(); i ++){

            if(password.charAt(i) >= 33 && password.charAt(i) <= 46)
                symbol = true;

            else {
                if(password.charAt(i) >= 48 && password.charAt(i) <= 57)
                    number = true;

                else {
                    if(password.charAt(i) >= 65 && password.charAt(i) <= 90)
                        capitalLetter = true;

                    else{

                        if(password.charAt(i) >= 97 && password.charAt(i) <= 122)
                            letter = true;
                    }
                }
            }
        }

        return letter && capitalLetter && number && symbol;
    }

    private void registerButtonPressed(ActionEvent event){

        try{

            username = userText.getText();

            password = passwordText.getText();

            if(!checkCharacters()) {

                JOptionPane.showMessageDialog(null, "Invalid password! Must contain 1 uppercase letter, 1 symbol and 1 number");
                return;
            }

            if(databaseConnection.insert(username, password)){

                JOptionPane.showMessageDialog(null, "Account created!\nPress ok to continue");
            }

            else{

                if(checkUsername() && checkPassword())
                    JOptionPane.showMessageDialog(null, "Already taken");

                else
                    JOptionPane.showMessageDialog(null, "Please enter the password or the username!");
            }

            userText.setText("");
            passwordText.setText("");
        }
        catch(Exception exception){

            JOptionPane.showMessageDialog(null, "An error has occurred \n" + exception.toString());
        }
    }

    private void admin(){

        AdministratorGui administratorGui = new AdministratorGui(databaseConnection, username);
    }

    private void client(){

        Client client1 = new Client(databaseConnection, username);
    }

    private void loginButtonPressed(ActionEvent event){

        try{

            username = userText.getText();
            password = passwordText.getText();

            if(databaseConnection.checkUser(username, password)){

                databaseConnection.checkAdministrator(username, password);

                if(databaseConnection.checkAdministrator(username, password))
                    admin();
                else
                    client();

                dispose();
            }
            else{

                if(checkPassword() && checkUsername())
                    JOptionPane.showMessageDialog(null, "Wrong credentials");

                else
                    JOptionPane.showMessageDialog(null, "Please enter the password or the username!");
            }
        }
        catch(Exception exception){

            JOptionPane.showMessageDialog(null, "An error has occurred \n" + exception.toString());
        }
    }
}