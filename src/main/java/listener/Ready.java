package listener;

import core.Birthday;
import core.countdown.Countdowns;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.STATICS;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Ready extends ListenerAdapter{
    //public  static Timer timer;

    @Override
    public void onReady(ReadyEvent event){
        STATICS.setJDA(event.getJDA());
        if (STATICS.isElectusCd()) {
            System.out.println("setting up countdown...");
           // timer = new Timer();
            //timer.schedule(new Countdowns(), 0, 1000);
            System.out.println("setting up countdown done");

        }

        initializeBirthdayTask();
        System.out.println("NAIO READY");

    }

    private static void initializeBirthdayTask(){
        System.out.println("initializing birthday reminder...");
<<<<<<< Updated upstream
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 1);
        today.set(Calendar.SECOND, 0);
        Timer timer2 = new Timer();
        timer2.schedule(new Birthday(), today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
=======
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        //LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.MINUTES
        Long midnight=LocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.MINUTES);
        scheduler.scheduleAtFixedRate(new Birthday(), midnight, TimeUnit.DAYS.toMinutes(1), TimeUnit.MINUTES);
>>>>>>> Stashed changes
        System.out.println("initializing birthday reminder done");



    }
}
