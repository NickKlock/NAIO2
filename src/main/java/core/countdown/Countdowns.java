package core.countdown;

import core.mysql.functions.Settings;
import net.dv8tion.jda.core.JDA;
import util.STATICS;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TimerTask;

public class Countdowns extends TimerTask{

    private static void initializeElectus(){
        if (!STATICS.isElectusCd()) return;
        LocalDateTime startTime = LocalDateTime.parse("2019-04-26T19:00:00.000");
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime tempDateTime = LocalDateTime.from( now );

        long years = tempDateTime.until( startTime, ChronoUnit.YEARS);
        tempDateTime = tempDateTime.plusYears( years );

        long months = tempDateTime.until( startTime, ChronoUnit.MONTHS);
        tempDateTime = tempDateTime.plusMonths( months );

        long days = tempDateTime.until( startTime, ChronoUnit.DAYS);
        tempDateTime = tempDateTime.plusDays( days );


        long hours = tempDateTime.until( startTime, ChronoUnit.HOURS);
        tempDateTime = tempDateTime.plusHours( hours );

        long minutes = tempDateTime.until( startTime, ChronoUnit.MINUTES);
        tempDateTime = tempDateTime.plusMinutes( minutes );

        long seconds = tempDateTime.until( startTime, ChronoUnit.SECONDS);

        if (days > 0){
            STATICS.getJDA().getGuilds().get(0).getCategoryById(STATICS.ELECTUS_CD_CAT_ID).getManager().setName("ELECTUS: "+String.format("%02d", days)+" days " + String.format("%02d", hours)+":"+String.format("%02d", minutes)+":"+String.format("%02d", seconds)).complete();

        }else{
            STATICS.getJDA().getGuilds().get(0).getCategoryById(STATICS.ELECTUS_CD_CAT_ID).getManager().setName("ELECTUS: "+ String.format("%02d", hours)+":"+String.format("%02d", minutes)+":"+String.format("%02d", seconds)).complete();
        }

        if (years == 0 && days == 0 && hours == 0 && minutes == -5 && seconds == 0){
            try {
                Settings.setElectusCd(false);
                STATICS.getJDA().getGuilds().get(0).getCategoryById(STATICS.ELECTUS_CD_CAT_ID).delete().complete();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void run(){
        initializeElectus();
    }
}
