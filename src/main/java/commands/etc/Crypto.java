package commands.etc;

import commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class Crypto implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event){
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        switch (args[0]){
            case "ranking":
                String rankList = core.Crypto.getRankList();
                event.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Top - 25 Currencies").setDescription(rankList).build()).complete();

        }
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
