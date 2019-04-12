package commands.chat;

import commands.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.CMDHelpEmbed;
import util.CMD_REACTION;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class Clear implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event){
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException{
        if (args.length < 1){
            CMD_REACTION.negative(event);
            event.getTextChannel().sendMessage(CMDHelpEmbed.helpEmbed(help(),description()).build()).complete();

            return;
        }

        if (Integer.valueOf(args[0]) <= 100){
            CMD_REACTION.positive(event);
            List<Message> msgs = event.getTextChannel().getHistory().retrievePast(Integer.valueOf(args[0])).complete();
            event.getTextChannel().deleteMessages(msgs).complete();
        }else{
            event.getTextChannel().sendMessage(CMDHelpEmbed.helpEmbed(help(),description()).build()).complete();
            CMD_REACTION.negative(event);
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event){
        System.out.println("Executed Clear Command " + !success + " from " +event.getMember().getUser().getName()+"#"+event.getMember().getUser().getDiscriminator());

    }

    @Override
    public String help(){
        return "**.clear** *Amount Msgs <= 100*";
    }

    @Override
    public String description(){
        return "Deletes Messages from the text-channel";
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
