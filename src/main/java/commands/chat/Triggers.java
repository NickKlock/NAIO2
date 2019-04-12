package commands.chat;

import commands.Command;
import core.mysql.functions.CustomCommands;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.CMDHelpEmbed;
import util.CMD_REACTION;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Objects;

public class Triggers implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event){
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException{
        event.getTextChannel().sendTyping().complete();
        if (args.length < 1){
            event.getTextChannel().sendMessage(CMDHelpEmbed.helpEmbed(help(),description()).build()).complete();
            CMD_REACTION.negative(event);
            return;
        }

        String identifier = args[0] ;
        StringBuilder allArgs = new StringBuilder();
        for(int i = 1 ; i < args.length ; i++){
            allArgs.append(args[i]).append(" ");
        }

        switch (identifier){
            case "add":
                String[] split = allArgs.toString().split("\\|");
                String trigger = split[0].trim();
                String response = split[1].trim();
                try {
                    CustomCommands.addTrigger(response,trigger);
                    CMD_REACTION.positive(event);

                } catch (SQLException e) {
                    e.printStackTrace();
                    CMD_REACTION.negative(event);

                }
                return;
            case "all":
                PrivateChannel privateChannel = event.getMember().getUser().openPrivateChannel().complete();
                StringBuilder msg = new StringBuilder();
                try {
                    for (String res : Objects.requireNonNull(CustomCommands.getAll())) {
                        msg.append(res).append("\n");
                    }
                    EmbedBuilder eb = new EmbedBuilder()
                        .setColor(Color.CYAN)
                        .setDescription(msg.toString())
                        .setTitle("All triggers");
                    privateChannel.sendMessage(eb.build()).complete();
                    privateChannel.close().complete();
                    CMD_REACTION.positive(event);
                } catch (SQLException e) {
                    e.printStackTrace();
                    CMD_REACTION.negative(event);

                }
                return;
            case "remove":
                try {
                    CustomCommands.deleteTrigger(allArgs.toString());
                    CMD_REACTION.positive(event);

                } catch (SQLException e) {
                    e.printStackTrace();
                    CMD_REACTION.negative(event);

                }
                return;
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event){
        System.out.println("Executed Triggers Command " + !success + " from " +event.getMember().getUser().getName()+"#"+event.getMember().getUser().getDiscriminator());

    }

    @Override
    public String help(){
        return  "**.t / .triggers add** *some trigger* | *some response*\n" +
                "**.t / .triggers remove** *some trigger*\n"+
                "**.t/ .triggers all**";
    }

    @Override
    public String description(){
        return "Manages the custom responses. You can add, remove and get a list of all triggers.";
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
