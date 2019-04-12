package listener;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.STATICS;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Voice extends ListenerAdapter{
    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event){
        String joinedName = event.getChannelJoined().getName();
        String member = event.getMember().getAsMention();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.LLLL.yyyy HH:mm");
        String formattedString = now.format(formatter);

        TextChannel channel = event.getGuild().getTextChannelsByName(STATICS.getVoicelogChannel(), false).get(0);
        EmbedBuilder eb = new EmbedBuilder()
                .setColor(Color.GRAY)
                .setFooter(formattedString,null)
                .setDescription(member+" joined "+joinedName)
                ;
        channel.sendMessage(eb.build()).complete();
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event){
        String leftName = event.getChannelLeft().getName();
        String member = event.getMember().getAsMention();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.LLLL.yyyy HH:mm");
        String formattedString = now.format(formatter);

        TextChannel channel = event.getGuild().getTextChannelsByName(STATICS.getVoicelogChannel(), false).get(0);
        EmbedBuilder eb = new EmbedBuilder()
                .setColor(Color.GRAY)
                .setFooter(formattedString,null)
                .setDescription(member+" left "+leftName)
                ;
        channel.sendMessage(eb.build()).complete();
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event){
        String leftName = event.getChannelLeft().getName();
        String joinedName = event.getChannelJoined().getName();

        String member = event.getMember().getAsMention();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.LLLL.yyyy HH:mm");
        String formattedString = now.format(formatter);

        TextChannel channel = event.getGuild().getTextChannelsByName(STATICS.getVoicelogChannel(), false).get(0);
        EmbedBuilder eb = new EmbedBuilder()
                .setColor(Color.GRAY)
                .setFooter(formattedString,null)
                .setDescription(member+" moved from "+leftName+" to "+joinedName)
                ;
        channel.sendMessage(eb.build()).complete();
    }
}
