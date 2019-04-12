package util;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class CMD_REACTION{
    public static void positive(MessageReceivedEvent event){
        //415684635079606286L
        event.getMessage().addReaction("✅").complete();
    }
    public static void negative(MessageReceivedEvent event){
        event.getMessage().addReaction("❌").complete();
    }

}
