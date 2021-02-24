package ProjectMainPackage;

import DatabasePackage.ConnectionStrings;
import DatabasePackage.DatabaseConnection;
import GUIPackage.*;

import javax.swing.*;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args)  {

        try{

            ConnectionStrings connectionStrings = new ConnectionStrings();

            DatabaseConnection databaseConnection = new DatabaseConnection(connectionStrings.getHost(), connectionStrings.getUser(), connectionStrings.getPassword());

            LoginGUI gui = new LoginGUI(databaseConnection);
            gui.setVisible(true);
        }
        catch (SQLException exception){

            JOptionPane.showMessageDialog(null, "Connection error");
        }
    }
}