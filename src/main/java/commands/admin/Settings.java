package commands.admin;

import commands.Command;
import core.countdown.Countdowns;
import listener.Ready;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.CMD_REACTION;
import util.STATICS;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import static core.mysql.functions.Settings.*;

public class Settings implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event){
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException{
        switch (args[0]){
            case "cd":
                switch (args[1]){
                    case "on":
                        try {
                            Ready.timer.schedule(new Countdowns(),0,1000);
                            setElectusCd(true);
                            CMD_REACTION.positive(event);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "off":
                        try {
                            setElectusCd(false);
                            Ready.timer.purge();
                            CMD_REACTION.positive(event);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "id":
                        try {
                            setCdCategoryID(args[2]);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            case "music":
                switch (args[1]){
                    case "channel":
                        try {
                            setMusicChannel(args[2]);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                }
            case "vc_log":
                try {
                    setVcLogChannel(args[2]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "destroyNaio":
                Member memberById = event.getGuild().getMemberById("383355903920832536");
                event.getGuild().getController().kick(memberById).complete();

        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event){
        System.out.println("Executed Settings Command " + !success + " from " +event.getMember().getUser().getName()+"#"+event.getMember().getUser().getDiscriminator());

    }

    @Override
    public String help(){
        return null;
    }

    @Override
    public String description(){
        return null;
    }

    @Override
    public String commandType(){
        return null;
    }

    @Override
    public int permission(){
        return 0;
    }
}
