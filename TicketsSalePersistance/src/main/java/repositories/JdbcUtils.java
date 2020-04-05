package repositories;

import util.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private Properties props;
    public JdbcUtils(Properties props){
        this.props=props;
    }
    private static Connection instance=null;
    private Connection getNewConnection(){
        String driver=props.getProperty("jdbc.driver");
        String url=props.getProperty("jdbc.url");
        //String user=props.getProperty("chat.jdbc.user");
        //String pass=props.getProperty("chat.jdbc.pass");
        Connection con=null;
        try {
            Class.forName(driver);
            con= DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            Utils.logger.trace(e.getMessage());
        } catch (SQLException e) {
            Utils.logger.trace(e.getMessage());
        }
        return con;
    }

    public Connection getConnection(){
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
            Utils.logger.trace(e.getMessage());
        }
        return instance;
    }

}

