package DBlogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static String user;
    private static String password;
    private static String url;

    static  {
        loadfromENV();
    }
    private static void loadfromENV() {
        user = System.getenv("DB_USER");
        password = System.getenv("DB_PASSWORD");
        url = System.getenv("DB_URL");
    }
    public static Connection connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(url, user, password);
    }
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        return connect();
    }

}
