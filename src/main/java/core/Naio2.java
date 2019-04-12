package core;

import commands.Command;
import commands.Help;
import commands.chat.Clear;
import commands.etc.Birthday;
import commands.chat.Triggers;
import commands.music.Music;
import commands.sro.Raffle;
import core.mysql.functions.Settings;
import listener.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import util.STATICS;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

public class Naio2{
    private static JDA jda;
    private static JDABuilder jdaBuilder;
    public static final CommandParser parser = new CommandParser();
    public static HashMap<String, Command> commands = new HashMap<>();

    public static void main(String[] args) {
        try {
            System.out.println("loading settings...");
            Settings.getSettings();
            System.out.println("loading settings done");

            System.out.println("initializing bot...");
            setupBot();
            System.out.println("initializing bot done");

        } catch (LoginException  | SQLException e) {
            e.printStackTrace();
        }
    }
    private static void setupBot() throws LoginException{
        jdaBuilder = new JDABuilder(AccountType.BOT)
                .setToken(STATICS.getBotToken())
                .setAudioEnabled(true);

        initializeListeners();
        initializeCommands();

        System.out.println("initializing jda...");
        jda = jdaBuilder.build();
        System.out.println("initializing jda done");

    }
    private static void initializeListeners() {
        System.out.println("initializing listeners...");
        jdaBuilder.addEventListener(new Ready())
                .addEventListener(new Leave())
                .addEventListener(new Commands())
                .addEventListener(new Stream())
                .addEventListener(new Voice())

        ;
        System.out.println("initializing listeners done");

    }
    private static void initializeCommands(){
        System.out.println("initializing commands...");

        commands.put("raffle",new Raffle());
        commands.put("r",new Raffle());
        commands.put("triggers",new Triggers());
        commands.put("t",new Triggers());
        commands.put("s",new commands.admin.Settings());
        commands.put("bd",new Birthday());
        commands.put("m", new Music());
        commands.put("help", new Help());
        commands.put("h", new Help());
        commands.put("clear", new Clear());
        commands.put("c", new Clear());
        System.out.println("initializing commands done.");

    }
    public static void handleCommand(CommandParser.CommandContainer cmd) throws ParseException, IOException{

        if (commands.containsKey(cmd.invoke.toLowerCase())) {

            boolean safe = commands.get(cmd.invoke.toLowerCase()).called(cmd.args, cmd.event);

            if (!safe) {
                commands.get(cmd.invoke.toLowerCase()).action(cmd.args, cmd.event);
                commands.get(cmd.invoke.toLowerCase()).executed(safe, cmd.event);
            } else {
                commands.get(cmd.invoke.toLowerCase()).executed(safe, cmd.event);
            }

        }
    }

}
