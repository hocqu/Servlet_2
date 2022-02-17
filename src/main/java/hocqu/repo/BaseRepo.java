package hocqu.repo;


import hocqu.utils.JDBCConnector;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseRepo {
    private final JDBCConnector jdbcConnector = JDBCConnector.getInstance();
    protected Connection connection = jdbcConnector.getConnection();

    protected BaseRepo() throws SQLException {
    }
}
