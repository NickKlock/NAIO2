package core.mysql.connection;

import util.STATICS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector{
   private static String con = "jdbc:mysql://" + STATICS.MYSQL_IP + "/"+STATICS.MYSQL_DBNAME +"?useSSL=false";

    public static Connection connect() throws SQLException{
        return DriverManager.getConnection(con,STATICS.MYSQL_USER, STATICS.MYSQL_PASSWORD);
    }

}
