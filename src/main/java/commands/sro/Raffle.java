package commands.sro;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.CMDHelpEmbed;
import util.CMD_REACTION;
import util.ErrorEmbeds;
import util.STATICS;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Raffle implements Command{
    private static List<String> participants;
    private static boolean multi;

    @Override
    public boolean called(String[] args, MessageReceivedEvent event){
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException{
        event.getTextChannel().sendTyping().complete();

        if (!event.getTextChannel().getName().equals(STATICS.RAFFLE_CHANNEL)){
            String asMention = event.getGuild().getTextChannelsByName("raffle", false).get(0).getAsMention();
            event.getTextChannel().sendMessage(ErrorEmbeds.errorEmbed("Wrong text-channel"," Only usable in "+asMention+" .").build()).complete();
            CMD_REACTION.negative(event);
            return;
        }
        if (args.length < 1){
            event.getTextChannel().sendMessage(CMDHelpEmbed.helpEmbed(help(),description()).build()).complete();
            CMD_REACTION.negative(event);
            return;
        }

        participants = new LinkedList<>(Arrays.asList(args));
        CMD_REACTION.positive(event);

        if (isInt(args[0])){
            int howMany = Integer.valueOf(args[0]);
            participants.remove(args[0]);
            multi = true;
            for (int i = 1; i <= howMany; i++){
                event.getTextChannel().sendMessage(createMessage().build()).complete();
            }
            multi = false;
            return;
        }
        event.getTextChannel().sendMessage(createMessage().build()).complete();

    }

    private static EmbedBuilder createMessage(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.LLLL.yyyy HH:mm");
        String formattedString = localDateTime.format(formatter);

        EmbedBuilder msg = new EmbedBuilder()
                .setColor(Color.YELLOW)
                .setTitle("Participants")
                .setFooter("Raffle date: "+formattedString,null);

        int counter = 1;
        for (String participant :
                participants) {
            msg.addField("#"+String.valueOf(counter), participant, false);
            counter++;
        }
        msg.addBlankField(false);
        int randomNum = ThreadLocalRandom.current().nextInt(0, participants.size() );
        msg.addField("WINNER","Number: "+(randomNum+1) + " | Name: "+participants.get(randomNum),false);

        if (multi) {
            participants.remove(randomNum);
        }


        return msg;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event){
        System.out.println("Executed Raffle Command " + !success + " from " +event.getMember().getUser().getName()+"#"+event.getMember().getUser().getDiscriminator());
    }

    @Override
    public String help(){
        return "**.r/.raffle** name1 name2 name3 ...\n" +
                "**.r/raffle** [Amount of raffles] name1 name2 name3 ...";
    }

    @Override
    public String description(){
        return "Generates a list from the input and generates a random number. And announces the winner. Can only be used in #raffle";
    }

    @Override
    public String commandType(){
        return null;
    }

    @Override
    public int permission(){
        return 0;
    }

    private static boolean isInt(String a){
        try{
            int b = Integer.valueOf(a);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
}
