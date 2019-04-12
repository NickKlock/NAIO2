package commands.etc;

import commands.Command;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.CMDHelpEmbed;
import util.CMD_REACTION;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static core.mysql.functions.Birthday.addBirthday;
import static core.mysql.functions.Birthday.removeBirthday;
import static util.CMDHelpEmbed.*;

public class Birthday implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event){
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException{
        event.getTextChannel().sendTyping().complete();
        if (args.length < 1){
            event.getTextChannel().sendMessage(helpEmbed(help(),description()).build()).complete();
            CMD_REACTION.negative(event);
            return;
        }
        String identifier = args[0];

        switch (identifier){
            case "add":
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    formatter = formatter.withLocale( Locale.GERMAN );
                    LocalDate birthday = LocalDate.parse(args[1], formatter);
                    addBirthday(event,birthday);
                    CMD_REACTION.positive(event);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return;
            case "remove":
                try {
                    removeBirthday(event);
                    CMD_REACTION.positive(event);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event){
        System.out.println("Executed Birthday Command " + !success + " from " +event.getMember().getUser().getName()+"#"+event.getMember().getUser().getDiscriminator());

    }

    @Override
    public String help(){
        return "**.bd add** 1.1.1990\n" +
                "**.bd remove**";
    }

    @Override
    public String description(){
        return "Announces Birthdays. You can add and remove your Birthday";
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
