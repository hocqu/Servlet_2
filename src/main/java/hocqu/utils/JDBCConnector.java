package hocqu.utils;


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnector {

    private static JDBCConnector instance = null;

    public static JDBCConnector getInstance() throws SQLException {
        if (instance == null) {
            instance = new JDBCConnector();
        }
        return instance;
    }

    private Connection connection;

    private JDBCConnector() throws SQLException {
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream("src/main/resources/db_config.properties");
            property.load(fis);
            this.connection = DriverManager.getConnection(property.getProperty("URL"),
                    property.getProperty("USERNAME"),
                    property.getProperty("PASSWORD"));

        } catch (IOException e) {
            System.err.println("Error: There is no such file");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void connectionClose() throws SQLException {
        connection.close();
    }
}
