package listener;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class Leave extends ListenerAdapter{

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event){
        TextChannel defaultChannel = event.getGuild().getDefaultChannel();
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription(event.getUser().getAsTag()+" just left the server.");

        assert defaultChannel != null;

        defaultChannel.sendMessage(builder.build()).queue();
    }

}
