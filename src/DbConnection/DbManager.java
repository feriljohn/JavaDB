package DbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class DbManager {
    private static final ResourceBundle rd
            = ResourceBundle.getBundle("resourse.system",new Locale("en_US"));
            private static final String Loaddriver=rd.getString("driver");
    private static final String URL = rd.getString("url");
    private static final String USER =rd.getString( "userName");
    private static final String PASSWORD = rd.getString("password");

    static {
        try {
            Class.forName(Loaddriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error: MySQL JDBC Driver not found!");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error: Failed to close database connection!");
            }
        }
    }

    
}
