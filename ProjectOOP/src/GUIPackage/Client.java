package GUIPackage;


import DatabasePackage.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Client extends JFrame{

    private final String user;
    private static int ticket = 5;
    private final JButton reservationButton = new JButton("Reserve");

    private final JComboBox<Integer> placesAvailable = new JComboBox<Integer>();
    private final JComboBox<String> movieCombobox = new JComboBox<String>();
    private final JComboBox<String> dateCombobox = new JComboBox<String>();
    private final JComboBox<String> snackCombobox = new JComboBox<String>();

    private final DatabaseConnection databaseConnection;
    private final MovieDate movieDate;

    private final JButton reserve = new JButton("RESERVE");

    public Client(DatabaseConnection databaseConnection1, String user){

        this.user = user;
        this.databaseConnection = databaseConnection1;
        this.movieDate = new MovieDate(databaseConnection);

        setTitle("Client Menu");

        setSize(800, 600);

        setLocation(new Point(550, 300));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        setMovieCombobox();
        setSnackCombobox();

        initComponent();
        setVisible(true);
    }

    private void initComponent(){

        movieCombobox.addActionListener(new java.awt.event.ActionListener(){

            public void actionPerformed(java.awt.event.ActionEvent evt){

                comboMovieActionPerformed(evt, movieCombobox.getSelectedItem().toString());
            }
        });

        dateCombobox.addActionListener(new java.awt.event.ActionListener(){

            public void actionPerformed(java.awt.event.ActionEvent evt){

                if(dateCombobox.getSelectedItem() != null)
                    comboDateActionPerformed(evt, dateCombobox.getSelectedItem().toString());
            }
        });

        reservationButton.addActionListener(this::reserveButton);

        movieCombobox.setBounds(470, 100, 300, 30);
        dateCombobox.setBounds(470, 160, 300, 30);
        snackCombobox.setBounds(470, 220, 300, 30);
        placesAvailable.setBounds(470, 280, 300, 30);

        reservationButton.setBounds(470, 340, 300, 30);

        add(reservationButton);
        add(movieCombobox);
        add(dateCombobox);
        add(snackCombobox);
        add(placesAvailable);
    }

    private void reserveButton(ActionEvent actionEvent){

        String movie = movieCombobox.getSelectedItem().toString();
        String date = dateCombobox.getSelectedItem().toString();
        int places;

        String placeString = placesAvailable.getSelectedItem().toString();

        places = Integer.parseInt(placeString);

        if(databaseConnection.reservation(movie, date, user, places, snackCombobox.getSelectedItem().toString())){

            databaseConnection.updateMovieDate(movie, date, places);
            databaseConnection.getProfit(date, places * ticket);
            JOptionPane.showMessageDialog(null, "Success");
        }
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

    private void setDateCombobox(String movieName){

        dateCombobox.removeAllItems();

        ArrayList<String> list = movieDate.getDate(movieName);

        for (String s : list) {
            dateCombobox.addItem(s);
        }
    }
    private void comboMovieActionPerformed(java.awt.event.ActionEvent evt, String movieName) {

        setDateCombobox(movieName);
    }

    private void comboDateActionPerformed(java.awt.event.ActionEvent evt, String date) {

        int roomPlaces = databaseConnection.getAvailablePlaces(movieCombobox.getSelectedItem().toString(), date);
        placesAvailable.removeAllItems();

        for(int i = 1; i <= roomPlaces; i ++)
            placesAvailable.addItem(i);
    }
}

