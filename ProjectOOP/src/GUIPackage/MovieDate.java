package GUIPackage;

import DatabasePackage.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MovieDate{

    private String query;
    private final DatabaseConnection databaseConnection;

    public MovieDate(DatabaseConnection db){

        databaseConnection = db;
    }

    public ArrayList<String> getMovie(){

        ArrayList<String> list = new ArrayList<String>();

        try {

            query = "SELECT movieName from demo.movie ";

            PreparedStatement preparedStatement1 = databaseConnection.getConnection().prepareStatement(query);

            preparedStatement1.execute();

            ResultSet resultSet = preparedStatement1.getResultSet();

            while (resultSet.next()) {

                list.add(resultSet.getString("movieName"));
            }

        } catch (SQLException exception) {

            System.out.println(exception);
        }

        return list;
    }

    private int getMovieId(String MovieName){

        int movieId;

        try {

            query = "SELECT movieId from demo.movie where movieName = ?";

            PreparedStatement preparedStatement1 = databaseConnection.getConnection().prepareStatement(query);

            preparedStatement1.setString(1, MovieName);

            preparedStatement1.execute();

            ResultSet resultSet = preparedStatement1.getResultSet();

            if (resultSet.next()) {

                movieId = resultSet.getInt("movieId");

                return movieId;
            }

            return 0;

        } catch (SQLException exception) {

            return 0;
        }
    }

    public ArrayList<String> getDate(String movieName){

        ArrayList<String> list = new ArrayList<String>();

        try {

            query = "SELECT movieDate from demo.moviedatetable where movieId = ? order by movieDate";

            PreparedStatement preparedStatement1 = databaseConnection.getConnection().prepareStatement(query);

            preparedStatement1.setInt(1, getMovieId(movieName));

            preparedStatement1.execute();

            ResultSet resultSet = preparedStatement1.getResultSet();

            while (resultSet.next()) {

                list.add(resultSet.getString("movieDate"));
            }

        } catch (SQLException exception) {

            System.out.println(exception);
        }

        return list;
    }
}
