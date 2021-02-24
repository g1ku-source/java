package GUIPackage;

import DatabasePackage.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdministratorGui extends JFrame{

    private final JTextField movieTextField = new JTextField();
    private final JTextField snackField = new JTextField();
    private final JTextField snackPriceField = new JTextField();

    private final JButton addMovieButton = new JButton("Add movie");
    private final JButton addDateButton = new JButton("Add date");
    private final JButton removeMovieButton = new JButton("Remove movie");
    private final JButton addSnack = new JButton("Add snack");
    private final JButton removeSnack = new JButton("Remove snack");

    private final DatabaseConnection databaseConnection;
    private final MovieDate movieDate;
    private final String admin;

    private final JComboBox<String> movieCombobox = new JComboBox<String>();
    private final JComboBox<String> dateCombobox = new JComboBox<String>();
    private final JComboBox<String> snackCombobox = new JComboBox<String>();
    private final JComboBox<Integer> roomCombobox = new JComboBox<Integer>();

    public AdministratorGui(DatabaseConnection databaseConnection1, String admin1){

        admin = admin1;

        databaseConnection = databaseConnection1;
        movieDate = new MovieDate(databaseConnection);

        setTitle("Administrator Menu");

        setSize(800, 600);

        setLocation(new Point(550, 300));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        dateCombobox.addItem("-- Select a date --");
        movieCombobox.addItem("-- Select a movie --");
        snackCombobox.addItem("-- Select a snack --");

        setMovieCombobox();
        setSnackCombobox();
        setRoomCombobox();

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String dateString = formatter.format(date);

        //System.out.println(dateString);

        databaseConnection.deleteOldDate(dateString);

        initComponent();
        setVisible(true);
    }

    private void initComponent(){

        movieCombobox.addActionListener(new java.awt.event.ActionListener(){

            public void actionPerformed(java.awt.event.ActionEvent evt){

                comboMovieActionPerformed(evt, movieCombobox.getSelectedItem().toString());
            }
        });

        JLabel selectMovieLabel = new JLabel("Select a movie: ");
        selectMovieLabel.setBounds(70, 100, 200, 30);

        JLabel selectDateLabel = new JLabel("Select a date: ");
        selectDateLabel.setBounds(70, 160, 200, 30);

        JLabel writeDataLabel = new JLabel("Write a date for the movie or the movie you want to add:");
        writeDataLabel.setBounds(70, 220, 340, 30);

        JLabel roomLabel = new JLabel("Enter room");
        roomLabel.setBounds(270, 280, 200, 30);

        movieCombobox.setBounds(470, 100, 300, 30);
        dateCombobox.setBounds(470, 160, 300, 30);
        snackCombobox.setBounds(70, 340, 300, 30);
        roomCombobox.setBounds(70, 280, 200, 30);

        movieTextField.setBounds(470, 220, 300, 30);
        snackField.setBounds(70, 400, 100, 30);
        snackField.setText("Enter snack");
        snackPriceField.setText("Enter price");
        snackPriceField.setBounds(200, 400, 100, 30);

        addMovieButton.setBounds(470, 280, 300, 30);
        addDateButton.setBounds(470, 340, 300, 30);
        removeMovieButton.setBounds(470, 400, 300, 30);
        addSnack.setBounds(70, 460, 100, 30);
        removeSnack.setBounds(200, 460, 200,30);

        addMovieButton.addActionListener(this::addMovieButton);
        addDateButton.addActionListener(this::addMovieDateButton);
        removeMovieButton.addActionListener(this::removeButton);
        addSnack.addActionListener(this::addSnackButton);
        removeSnack.addActionListener(this::removeSnackButton);

        add(movieCombobox);
        add(dateCombobox);
        add(snackCombobox);
        add(roomCombobox);

        add(movieTextField);
        add(snackField);
        add(snackPriceField);

        add(addDateButton);
        add(addMovieButton);
        add(removeMovieButton);
        add(addSnack);
        add(removeSnack);

        add(selectDateLabel);
        add(selectMovieLabel);
        add(writeDataLabel);
        add(roomLabel);
    }

    private void setDateCombobox(String movieName){

        dateCombobox.removeAllItems();

        ArrayList<String> list = movieDate.getDate(movieName);
        dateCombobox.addItem("-- Select a date --");

        for (String s : list) {
            dateCombobox.addItem(s);
        }
    }

    private void setRoomCombobox(){

        roomCombobox.removeAllItems();

        ArrayList<Integer> list = databaseConnection.getRoomList();
        for(Integer i : list)
            roomCombobox.addItem(i);
    }

    private void comboMovieActionPerformed(java.awt.event.ActionEvent evt, String movieName) {

        //setMovieCombobox();
        setDateCombobox(movieName);
    }

    private void setMovieCombobox(){

        ArrayList<String> list = movieDate.getMovie();

        for(String s : list){

            movieCombobox.addItem(s);
        }
    }

    private void setSnackCombobox(){

        snackCombobox.removeAllItems();

        ArrayList<String> list = databaseConnection.getSnacks();

        for(String s : list)
            snackCombobox.addItem(s);
    }

    private void addMovieButton(ActionEvent event){

        String movieName = movieTextField.getText();

        if(movieName.equals("")){

            JOptionPane.showMessageDialog(null, "Enter a movie!");
            return;
        }

        if(databaseConnection.addMovie(movieName)){

            movieCombobox.addItem(movieName);
            databaseConnection.updateAdmin(admin);
        }
        movieTextField.setText("");
    }

    private void addMovieDateButton(ActionEvent event){

        String movie = movieCombobox.getSelectedItem().toString();
        String date = movieTextField.getText();
        int room = (int) roomCombobox.getSelectedItem();

        if(databaseConnection.addMovieDate(movie, date, room)){

            databaseConnection.updateAdmin(admin);
            movieTextField.setText("");
        }
        else
            JOptionPane.showMessageDialog(null, "Try again");
    }

    private void removeButton(ActionEvent event) {

        String name = movieCombobox.getSelectedItem().toString();
        String date = dateCombobox.getSelectedItem().toString();

        if(date.equals("-- Select a date --")){

            if(name.equals("-- Select a movie --")){

                JOptionPane.showMessageDialog(null, "Please choose a movie!");
                return ;
            }

            if(databaseConnection.deleteMovie(name)){

                JOptionPane.showMessageDialog(null, "Success");

                databaseConnection.updateAdmin(admin);
                setDateCombobox(movieCombobox.getSelectedItem().toString());
            }
            else
                JOptionPane.showMessageDialog(null, "Try again");
            return;
        }

        if(databaseConnection.deleteDate(name, date)){

            JOptionPane.showMessageDialog(null, "Success");

            databaseConnection.updateAdmin(admin);
            setDateCombobox(movieCombobox.getSelectedItem().toString());
        }
        else
            JOptionPane.showMessageDialog(null, "Try again");
    }

    private void addSnackButton(ActionEvent event){

        String name = snackField.getText();
        int price;

        try{

            price = Integer.parseInt(snackPriceField.getText());

            databaseConnection.addSnack(name, price);

            snackCombobox.removeAllItems();
            setSnackCombobox();

            databaseConnection.updateAdmin(admin);
        }
        catch (NumberFormatException exception){

            JOptionPane.showMessageDialog(null, "Enter a number!");
        }
    }

    private void removeSnackButton(ActionEvent event){

        String name = snackCombobox.getSelectedItem().toString();

        if(name.equals("-- Select a snack --")){

            JOptionPane.showMessageDialog(null, "Please select a snack");
            return ;
        }

        if(databaseConnection.removeSnack(name)){

            databaseConnection.updateAdmin(admin);
            setSnackCombobox();

            return ;
        }

        JOptionPane.showMessageDialog(null, "Something went wrong");
    }
}
