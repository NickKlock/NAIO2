package core.mysql.functions;

import core.mysql.connection.Connector;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomCommands{

    public static String getResponse(String lookfor) throws SQLException{

        Connection connection = Connector.connect();
        PreparedStatement ps = connection.prepareStatement("SELECT Link from CustomCommands where Responder = ?");
        ps.setString(1,lookfor);
        ResultSet rs = ps.executeQuery();
        String resp = null;
        while (rs.next()){
            resp = rs.getString(1);
        }
        rs.close();
        ps.close();
        connection.close();
        return resp;

    }

    public static void addTrigger(String link, String response) throws SQLException{
        Connection connection = Connector.connect();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO CustomCommands (Link, Responder) VALUES (?,?)");
        ps.setString(1,link);
        ps.setString(2,response);
        ps.executeUpdate();
        ps.close();
        connection.close();
    }

    public static ArrayList<String> getAll() throws SQLException{
        Connection connection = Connector.connect();
        PreparedStatement ps = connection.prepareStatement("SELECT Responder from CustomCommands");
        ResultSet resultSet = ps.executeQuery();
        ArrayList<String> results = new ArrayList<>();
        while (resultSet.next()){
            results.add(resultSet.getString(1));
        }
        resultSet.close();
        ps.close();
        connection.close();
        return results;
    }

    public static void deleteTrigger(String trigger) throws SQLException{
        Connection connection = Connector.connect();
        PreparedStatement ps = connection.prepareStatement("DELETE from CustomCommands where Responder = ?");
        ps.setString(1,trigger);
        ps.executeUpdate();
        ps.close();
        connection.close();
    }
}
