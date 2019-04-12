package util;

import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;

public class CMDHelpEmbed{

    public static EmbedBuilder helpEmbed(String help,String desc){

        return new EmbedBuilder().setColor(Color.CYAN)
                .setTitle("Help")
                .setDescription(desc)
                .addField("Instructions",help,false);
    }
}
