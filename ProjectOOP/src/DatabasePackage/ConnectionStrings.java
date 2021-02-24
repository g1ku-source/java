package DatabasePackage;

public class ConnectionStrings {

    private String schemaName = "demo";
    private String password = "P!tur1na";
    private String user = "root";

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
