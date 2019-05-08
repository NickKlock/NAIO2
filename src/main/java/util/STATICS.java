package util;

import net.dv8tion.jda.core.JDA;

public class STATICS{
    public static String BOT_TOKEN;
    public static String PREFIX ;
    public static String MYSQL_IP = "81.169.241.135:3306";
    public static String MYSQL_PASSWORD = "evo2ve16";
    public static String MYSQL_DBNAME = "NAIO";
    public static String MYSQL_USER = "naio";
    public static String ELECTUS_CD_CAT_ID ;
    public static boolean ELECTUS_CD ;
    public static JDA JDA;
    public static String input;
    public static String MUSIC_CHANNEL;
    public static String VOICELOG_CHANNEL;
    public static String CRYPTO_PREFIX = "!";

    public static String getCryptoPrefix() {
        return CRYPTO_PREFIX;
    }

    public static void setCryptoPrefix(String cryptoPrefix) {
        CRYPTO_PREFIX = cryptoPrefix;
    }


    public static String getVoicelogChannel(){
        return VOICELOG_CHANNEL;
    }

    public static void setVoicelogChannel(String voicelogChannel){
        VOICELOG_CHANNEL = voicelogChannel;
    }

    public static String getMusicChannel(){
        return MUSIC_CHANNEL;
    }

    public static void setMusicChannel(String musicChannel){
        MUSIC_CHANNEL = musicChannel;
    }

    public static net.dv8tion.jda.core.JDA getJDA(){
        return JDA;
    }

    public static void setJDA(net.dv8tion.jda.core.JDA JDA){
        STATICS.JDA = JDA;
    }

    public static String RAFFLE_CHANNEL ;


    public static String getPREFIX(){
        return PREFIX;
    }

    public static void setPREFIX(String PREFIX){
        STATICS.PREFIX = PREFIX;
    }

    public static String getElectusCdCatId(){
        return ELECTUS_CD_CAT_ID;
    }

    public static void setElectusCdCatId(String electusCdCatId){
        ELECTUS_CD_CAT_ID = electusCdCatId;
    }

    public static String getRaffleChannel(){
        return RAFFLE_CHANNEL;
    }

    public static void setRaffleChannel(String raffleChannel){
        RAFFLE_CHANNEL = raffleChannel;
    }

    public static boolean isElectusCd(){
        return ELECTUS_CD;
    }

    public static void setElectusCd(boolean electusCd){
        ELECTUS_CD = electusCd;
    }

    public static String getBotToken(){
        return BOT_TOKEN;
    }
    public static void setBotToken(String botToken){
        STATICS.BOT_TOKEN = botToken;
    }
}
