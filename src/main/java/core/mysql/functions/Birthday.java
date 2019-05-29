package core.mysql.functions;


import core.models.Bd;
import core.mysql.connection.Connector;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Birthday  {

    public static HashMap<LocalDate,String> getBirthdays() throws SQLException{
        Connection conn;
        ResultSet rs ;
        String sql = "select userID, birthday from Birthday";
        conn = Connector.connect();
        Statement statement = conn.createStatement();
        rs = statement.executeQuery(sql);
        HashMap<LocalDate,String> birthDays = new HashMap<>();
        while (rs.next()){
            String dateString = rs.getString(2);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            formatter = formatter.withLocale( Locale.GERMAN );  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
            LocalDate birthday = LocalDate.parse(dateString, formatter);
            birthDays.put(birthday,rs.getString(1));
        }
        statement.close();
        rs.close();
        conn.close();
        return birthDays;
    }

    public static void addBirthday(MessageReceivedEvent event, LocalDate date) throws SQLException{
        Connection connection = Connector.connect();
        //System.out.println(Date.valueOf(date).toString());
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Birthday(userID, userName, birthday) VALUES (?,?,?)");
        preparedStatement.setString(1,event.getMember().getUser().getId());
        preparedStatement.setString(2,event.getMember().getUser().getName());
        preparedStatement.setString(3,Date.valueOf(date).toString());
        preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
    }
    public static void removeBirthday(MessageReceivedEvent event) throws SQLException{
        Connection connection = Connector.connect();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE from Birthday where userID = ?");
        preparedStatement.setString(1,event.getMember().getUser().getId());
        preparedStatement.executeUpdate();
        preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
    }

    public static ArrayList<Bd> nextBirthdays() throws SQLException{
        LocalDate now = LocalDate.now();
        Connection connection = Connector.connect();
        PreparedStatement preparedStatement = connection.prepareStatement("select userID,birthday from Birthday where date_format(birthday,'%m-%d') > date_format(?, '%m-%d') order by extract(month from birthday), extract(day from birthday) LIMIT 5");
        preparedStatement.setString(1,now.toString());
        ResultSet rs =preparedStatement.executeQuery();
        ArrayList<Bd> bds = new ArrayList<>();
        while (rs.next()){
            Bd bd = new Bd();
            bd.setId(rs.getString(1));
            bd.setBd(rs.getDate(2).toLocalDate());

            bds.add(bd);
        }
        rs.close();
        preparedStatement.close();
        connection.close();
        return bds;
    }


}
