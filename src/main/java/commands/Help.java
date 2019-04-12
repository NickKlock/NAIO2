package commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.CMD_REACTION;
import util.STATICS;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;

public class Help implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event){
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException{
        CMD_REACTION.positive(event);
        EmbedBuilder help = new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle("Help 4 Dummies")
                .setDescription("EIGHTs very own bot")
                .addField(STATICS.getPREFIX()+"raffle","Creates a list from Names and raffles a winner",false)
                .addField(STATICS.getPREFIX()+"m","Classic Music bot works only in #"+STATICS.getMusicChannel(),false)
                .addField(STATICS.getPREFIX()+"triggers","Add/remove and list all custom triggers",false)
                .addField(STATICS.getPREFIX()+"bd","Add/remove you birthday from the reminder System",false)
                .addField(STATICS.getPREFIX()+"s","Better don't touch if you got no idea.",false)
                .addField(STATICS.getPREFIX()+"clear","Clears x messages from a text-channel.",false)

                ;
        event.getTextChannel().sendMessage(help.build()).complete();

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event){

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
