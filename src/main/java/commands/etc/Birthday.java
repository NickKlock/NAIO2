package commands.etc;

import commands.Command;
import core.models.Bd;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.CMD_REACTION;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static core.mysql.functions.Birthday.addBirthday;
import static core.mysql.functions.Birthday.nextBirthdays;
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
                    CMD_REACTION.negative(event);
                    return;
                }
                return;
            case "remove":
                try {
                    removeBirthday(event);
                    CMD_REACTION.positive(event);
                    return;
                } catch (SQLException e) {
                    CMD_REACTION.negative(event);
                    e.printStackTrace();
                    return;
                }

            case "next":
                try {
                    StringBuilder birthdays = new StringBuilder();
                    for (Bd b : nextBirthdays()) {
                        Period until = b.getBd().until(LocalDate.now());
                        int age = until.getYears()+1;
                        birthdays.append(event.getGuild().getMemberById(b.getId()).getAsMention()).append(" - ").append(b.getBd().getDayOfMonth()).append(".").append(b.getBd().getMonthValue()).append(" (").append(age).append(")\n");
                    }

                    EmbedBuilder eb = new EmbedBuilder().setColor(Color.LIGHT_GRAY).setTitle("NEXT 5 BIRTHDAYS").setDescription(birthdays.toString());
                    event.getTextChannel().sendMessage(eb.build()).complete();
                } catch (SQLException e) {
                    e.printStackTrace();
                    CMD_REACTION.negative(event);
                    return;
                }
                return;
            case "last":
                return;
            default:
                event.getTextChannel().sendMessage(helpEmbed(help(),description()).build()).complete();
                CMD_REACTION.negative(event);
                return;

        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event){
        System.out.println("Executed Birthday Command " + !success + " from " +event.getMember().getUser().getName()+"#"+event.getMember().getUser().getDiscriminator());

    }

    @Override
    public String help(){
        return "**.bd add** 1.1.1990\n" +
                "**.bd next**\n" +
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
