package listener;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class Stream extends ListenerAdapter{
    /**
     * getName = TITLE
     * getDetails = GAME
     * getLargeImage = THUMBNAIL
     * getUrl = twitch url
     **/
    @Override
    public void onUserUpdateGame(UserUpdateGameEvent event){
        if (event.getNewGame() != null && event.getNewGame().getType() == Game.GameType.STREAMING){
            String title = event.getNewGame().getName();
            String game = event.getNewGame().asRichPresence().getDetails();
            String thumb = event.getNewGame().asRichPresence().getLargeImage().getUrl();
            String url = event.getNewGame().getUrl();
            String twitchName = url.split("\\/")[3].trim();
            TextChannel defaultChannel = event.getGuild().getTextChannelsByName("general",false).get(0);
            EmbedBuilder streaming_emb = new EmbedBuilder()
                    .setColor(Color.decode("#6441a5"))
                    .setThumbnail(thumb)
                    .setTitle(twitchName,url)
                    .setDescription(title)
                    .addField("Playing now:",game,true)
                    .addField("Link:",url,true)
                    ;
            defaultChannel.sendMessage(streaming_emb.build()).complete();
        }




    }


}
