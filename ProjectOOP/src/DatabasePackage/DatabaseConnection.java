package DatabasePackage;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection{

    private final Connection connection;
    private String query;
    private PreparedStatement preparedStatement;

    public DatabaseConnection(String host, String user, String password) throws SQLException {

        connection = DriverManager.getConnection(host, user, password);
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean insert(String user, String password) {

        try {

            try {

                query = "INSERT INTO demo.login(username, userPassword, administrator) values(?, ?, ?)";
                preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1, user);
                preparedStatement.setString(2, password);
                preparedStatement.setInt(3, 0);

                preparedStatement.execute();

                return true;
            } catch (SQLIntegrityConstraintViolationException e) {

                return false;
            }
        } catch (SQLException exception) {

            return false;
        }
    }

    public boolean checkUser(String user, String password) {

        try {

            query = "SELECT ID from demo.login where username = ? and userPassword = ?";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, user);
            preparedStatement.setString(2, password);

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();

            return resultSet.next();
        } catch (SQLException exception) {

            return false;
        }
    }

    public boolean checkAdministrator(String user, String password) {

        try {

            query = "SELECT administrator from demo.login where username = ? and userPassword = ?";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, user);
            preparedStatement.setString(2, password);

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {

                int administrator = resultSet.getInt("administrator");

                return administrator == 1;
            }

        } catch (SQLException exception) {

            return false;
        }

        return false;
    }

    public boolean addMovie(String name){

        try {

            query = "INSERT INTO demo.movie (movieName) values (?)";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);

            preparedStatement.execute();

            JOptionPane.showMessageDialog(null, "Movie created!");

            return true;

        } catch (SQLException exception) {

            JOptionPane.showMessageDialog(null, "Movie already exists!");

            return false;
        }
    }

    public boolean addMovieDate(String name, String date, int room){

        try{

            query = "insert into moviedatetable(roomPlace, movieid, roomnumber, moviedate)\n" +
                    "select room.roomPlaces, movie.movieId, ?, ? from room, movie where (roomId = ?) and (movieName = ?) and not exists\n" +
                    "(select * from moviedatetable, movie where moviedatetable.movieId = movie.movieId and movie.movieName = ? and roomnumber = ? and moviedate = ?)";

            preparedStatement = connection.prepareStatement(query);


            preparedStatement.setInt(1, room);
            preparedStatement.setString(2, date);
            preparedStatement.setInt(3, room);
            preparedStatement.setString(4, name);
            preparedStatement.setString(5, name);
            preparedStatement.setInt(6, room);
            preparedStatement.setString(7, date);

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            return true;
        }
        catch(SQLException exception){

            JOptionPane.showMessageDialog(null, exception);

            return false;
        }
    }

    public boolean deleteMovie(String movieName){

        try{

            query = "delete from moviedatetable where movieId = (Select movieId from movie where movieName = ?)";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, movieName);

            preparedStatement.execute();

            return true;
        }
        catch(SQLException exception){

            JOptionPane.showMessageDialog(null, exception);

            return false;
        }
    }

    public boolean deleteDate(String movieName, String date){

        try{

            query = "delete from moviedatetable where movieId = (Select movieId from movie where movieName = ?) and movieDate = ?";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, movieName);
            preparedStatement.setString(2, date);

            preparedStatement.execute();

            return true;
        }
        catch(SQLException exception){

            JOptionPane.showMessageDialog(null, exception);

            return false;
        }
    }

    public boolean updateAdmin(String admin){

        try{

            query = "update demo.administrator set numberMovies = numberMovies + 1 where id = (select demo.login.ID from demo.login where username = ?)";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, admin);

            preparedStatement.execute();

            return true;
        }
        catch (SQLException exception){

            JOptionPane.showMessageDialog(null, exception);
            return false;
        }
    }

    public boolean addSnack(String name, int price){

        try{

            query = "insert into demo.snacks (snackName, snackPrice) VALUES (?, ?)";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, price);

            preparedStatement.execute();

            return true;
        }
        catch(SQLException exception){

            JOptionPane.showMessageDialog(null, exception);
            return false;
        }
    }

    public ArrayList<String> getSnacks(){

        ArrayList<String> list = new ArrayList<>();

        try{

            query = "select snackName from demo.snacks";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();

            while(resultSet.next()){

                list.add(resultSet.getString("snackName"));
            }
        }
        catch (SQLException exception){

            JOptionPane.showMessageDialog(null, exception);
        }

        return list;
    }

    public boolean removeSnack(String name){

        try{

            query = "delete from snacks where snackName = ?";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);

            preparedStatement.execute();

            return true;
        }
        catch(SQLException exception){

            JOptionPane.showMessageDialog(null, exception);
            return false;
        }
    }

    public ArrayList<Integer> getRoomList(){

        ArrayList<Integer> list = new ArrayList<>();

        try{

            query = "select roomId from demo.room";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();

            while(resultSet.next()){

                list.add(resultSet.getInt("roomId"));
            }
        }
        catch (SQLException exception){

            JOptionPane.showMessageDialog(null, exception);
        }

        return list;
    }

    public void deleteOldDate(String date){

        try{

            query = "delete from moviedatetable where movieDate < (?)";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, date);

            preparedStatement.execute();
        }
        catch(SQLException exception){

            JOptionPane.showMessageDialog(null, exception);
        }
    }

    public int getAvailablePlaces(String movieName, String date){

        try{

            query = "select roomPlace from moviedatetable where movieid = (select movieId from movie where movieName = ?) and moviedate = ?";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, movieName);
            preparedStatement.setString(2, date);

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();

            if(resultSet.next())
                return resultSet.getInt("roomPlace");
        }
        catch(SQLException exception){

            JOptionPane.showMessageDialog(null, exception);
        }

        return -1;
    }

    public boolean reservation(String movieName, String movieDate, String clientName, int roomPlaces, String snackName){

        try{

            query =  "insert into reservation(moviedateid, clientid, roomplaces, snackId) VALUES " +
                    "((select id from moviedatetable where movieid = (select movieId from movie where moviename = ?) and moviedate = ?), (select ID from login where username = ?), ?, (select snackid from snacks where snackName = ?))";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, movieName);
            preparedStatement.setString(2, movieDate);
            preparedStatement.setString(3, clientName);
            preparedStatement.setInt(4, roomPlaces);
            preparedStatement.setString(5, snackName);

            preparedStatement.execute();

            return true;
        }
        catch(SQLException exception){

            JOptionPane.showMessageDialog(null, exception);
        }

        return false;
    }

    public boolean updateMovieDate(String movieName, String date, int numberPlaces){

        try{

            query = "update moviedatetable set roomPlace = roomPlace - ? where movieid = (select movieId from movie where movieName = ?) and moviedate = ?";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, numberPlaces);
            preparedStatement.setString(2, movieName);
            preparedStatement.setString(3, date);

            preparedStatement.execute();
            return true;
        }
        catch (SQLException exception){

            JOptionPane.showMessageDialog(null, exception);
        }

        return false;
    }

    public boolean getProfit(String date, int profit) {

        try {
            query = "INSERT INTO profit (day, profitday) VALUES (?, ?) ON DUPLICATE KEY UPDATE profitday = profitday + ?";

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(2, profit);
            preparedStatement.setString(1, date);
            preparedStatement.setInt(3, profit);

            preparedStatement.execute();

            return true;
        }
        catch (SQLException exception){

            return false;
        }
    }
}