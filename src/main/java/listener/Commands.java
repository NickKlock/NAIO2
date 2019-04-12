package listener;

import core.mysql.functions.CustomCommands;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.STATICS;
import core.Naio2;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class Commands extends ListenerAdapter{

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if(e.getChannelType() == ChannelType.PRIVATE) return;
        String response = null;
        try {
            response = CustomCommands.getResponse(e.getMessage().getContentRaw());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        if (response != null){
            e.getTextChannel().sendTyping().complete();
            e.getTextChannel().sendMessage(response).complete();
            return;
        }
        if (e.getMessage().getContentRaw().startsWith(STATICS.PREFIX) && e.getMessage().getAuthor().getId() != e.getJDA().getSelfUser().getId()) {
            try {
                Naio2.handleCommand(Naio2.parser.parse(e.getMessage().getContentRaw(),e));
            } catch (ParseException | IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
