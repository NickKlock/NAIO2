package core.mysql.functions;

import core.mysql.connection.Connector;
import util.STATICS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Settings{

    public static void getSettings() throws SQLException{
        Connection connection = Connector.connect();
        PreparedStatement ps = connection.prepareStatement("SELECT * from Settings ");
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            STATICS.setBotToken(rs.getString("BotToken"));
            STATICS.setPREFIX(rs.getString("Prefix"));
            STATICS.setElectusCdCatId(rs.getString("ElectusCdCatID"));
            STATICS.setRaffleChannel(rs.getString("RaffleChannel"));
            STATICS.setElectusCd(rs.getBoolean("CdEnable"));
            STATICS.setMusicChannel(rs.getString("MusicChannel"));
            STATICS.setVoicelogChannel(rs.getString("VoiceLogChannel"));
        }

        rs.close();
        ps.close();
        connection.close();
    }

    public static void setElectusCd(boolean cd) throws SQLException{
        Connection connection = Connector.connect();
        PreparedStatement ps = connection.prepareStatement("UPDATE Settings SET CdEnable = ? WHERE CdEnable = ?");

        ps.setBoolean(1,cd);
        ps.setBoolean(2,STATICS.ELECTUS_CD);

        STATICS.setElectusCd(cd);

        ps.close();
        connection.close();

    }

    public static void setCdCategoryID(String id) throws SQLException{
        Connection connection = Connector.connect();
        PreparedStatement ps = connection.prepareStatement("UPDATE Settings SET ElectusCdCatID = ? WHERE ElectusCdCatID = ?");

        ps.setString(1,id);
        ps.setString(2,STATICS.ELECTUS_CD_CAT_ID);

        STATICS.setElectusCdCatId(id);

        ps.close();
        connection.close();
    }

    public static void setMusicChannel(String name) throws SQLException{
        Connection connection = Connector.connect();
        PreparedStatement ps = connection.prepareStatement("UPDATE Settings SET MusicChannel = ? WHERE MusicChannel = ?");

        ps.setString(1,name);
        ps.setString(2,STATICS.MUSIC_CHANNEL);

        STATICS.setMusicChannel(name);

        ps.close();
        connection.close();
    }

    public static void setVcLogChannel(String name) throws SQLException{
        Connection connection = Connector.connect();
        PreparedStatement ps = connection.prepareStatement("UPDATE Settings SET VoiceLogChannel = ? WHERE VoiceLogChannel = ?");

        ps.setString(1,name);
        ps.setString(2,STATICS.VOICELOG_CHANNEL);

        STATICS.setVoicelogChannel(name);

        ps.close();
        connection.close();
    }
}

