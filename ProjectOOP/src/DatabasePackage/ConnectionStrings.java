package DatabasePackage;

public class ConnectionStrings {
    
    //insert your own credentials from the database
    private String schemaName = "";
    private String password = "";
    private String user = "";

    public String getHost() {

        String host = "jdbc:mysql://localhost:3306/";
        return host + schemaName;
    }

    public String getUser() {

        return user;
    }

    public String getPassword() {

        return password;
    }
}
