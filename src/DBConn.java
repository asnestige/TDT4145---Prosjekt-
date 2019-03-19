import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConn {


    // Statisk funksjon som kan brukes for aa koble seg opp mot databasen.
    // Passord og brukernavn ligger i koden, mulig det skal gaa inn som startargs eller env. variabler
    public static Connection getDBConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://mysql.stud.ntnu.no/heddasu_dbkissa?autoReconnect=true&useSSL=false";
        String user = "heddasu_db";
        String pass = "yulve123";

        Properties p = new Properties();
        p.put("user", user);
        p.put("password", pass);

        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        Connection conn = DriverManager.getConnection (url, p);

        return conn;
    }

}