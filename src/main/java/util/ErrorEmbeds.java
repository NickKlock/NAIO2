package util;

import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;

public class ErrorEmbeds{

    public static EmbedBuilder errorEmbed(String title, String msg){
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Error - " + title)
                .setDescription(msg);
    }
}
