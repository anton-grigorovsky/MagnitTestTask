package magnit.test.task;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
;

/**
 * Created by Антон on 21.09.2017.
 */


// --------- Утильный класс, создает соединение с БД --------
public class DBConnectivity {

    private String URL;
    private String userName;
    private String password;

    public DBConnectivity(String URL, String userName, String password) {
        this.URL = URL;
        this.userName = userName;
        this.password = password;
    }

    public Connection getConnection() throws SQLException{

        Connection connection = null;
        try {
            Driver driver = (Driver)Class.forName("org.postgresql.Driver").newInstance();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(URL, userName, password);
        }
        catch (Exception e ) {}

    return connection;

}


}
