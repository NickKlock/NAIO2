package commands.music;

import core.audioCore.AudioInfo;
import core.audioCore.AudioPlayerSendHandler;
import core.audioCore.TrackManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import commands.Command;
import core.mysql.functions.Settings;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.CMD_REACTION;
import util.ErrorEmbeds;
import util.STATICS;

import java.awt.*;
import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.List;



public class Music implements Command {

    private static String clueURL = "https://youtu.be/IITW4P52gC4";

    private static final String NOTE = ":musical_note:  ";

    private static Guild guild;

    private static final int PLAYLIST_LIMIT = 1000;
    private static final AudioPlayerManager myManager = new DefaultAudioPlayerManager();
    private static final Map<String, Map.Entry<AudioPlayer, TrackManager>> players = new HashMap<>();

    private boolean endlessMode = false;
    private List<AudioTrack> endlessList = new ArrayList<>();
    private Member endlessAuthor;


    private boolean hasPlayer(Guild guild) {
        return players.containsKey(guild.getId());
    }

    private AudioPlayer getPlayer(Guild guild) {
        AudioPlayer p;
        if (hasPlayer(guild)) {
            p = players.get(guild.getId()).getKey();
        } else {
            p = createPlayer(guild);
        }
        return p;
    }

    private TrackManager getTrackManager(Guild guild) {
        return players.get(guild.getId()).getValue();
    }

    private AudioPlayer createPlayer(Guild guild) {
        AudioPlayer nPlayer = myManager.createPlayer();
        TrackManager manager = new TrackManager(nPlayer);
        nPlayer.addListener(manager);
        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(nPlayer));
        players.put(guild.getId(), new AbstractMap.SimpleEntry<>(nPlayer, manager));
        return nPlayer;
    }

    private void reset(Guild guild) {
        players.remove(guild.getId());
        getPlayer(guild).destroy();
        getTrackManager(guild).purgeQueue();
        guild.getAudioManager().closeAudioConnection();
    }

    private void loadTrackNext(String identifier, Member author, Message msg) {


        Guild guild = author.getGuild();
        getPlayer(guild);

        msg.getTextChannel().sendTyping().queue();
        myManager.setFrameBufferDuration(100);
        myManager.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {

                AudioInfo currentTrack = getTrackManager(guild).getQueuedTracks().iterator().next();
                Set<AudioInfo> queuedTracks = getTrackManager(guild).getQueuedTracks();
                queuedTracks.remove(currentTrack);
                getTrackManager(guild).purgeQueue();
                getTrackManager(guild).queue(currentTrack.getTrack(), author);
                getTrackManager(guild).queue(track, author);
                queuedTracks.forEach(audioInfo -> getTrackManager(guild).queue(audioInfo.getTrack(), author));
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getSelectedTrack() != null) {
                    trackLoaded(playlist.getSelectedTrack());
                } else if (playlist.isSearchResult()) {
                    trackLoaded(playlist.getTracks().get(0));
                } else {
                    AudioInfo currentTrack = getTrackManager(guild).getQueuedTracks().iterator().next();
                    Set<AudioInfo> queuedTracks = getTrackManager(guild).getQueuedTracks();
                    queuedTracks.remove(currentTrack);
                    getTrackManager(guild).purgeQueue();
                    getTrackManager(guild).queue(currentTrack.getTrack(), author);
                    for (int i = 0; i < Math.min(playlist.getTracks().size(), PLAYLIST_LIMIT); i++) {
                        getTrackManager(guild).queue(playlist.getTracks().get(i), author);
                    }
                    queuedTracks.forEach(audioInfo -> getTrackManager(guild).queue(audioInfo.getTrack(), author));
                }
            }

            @Override
            public void noMatches() {
            }

            @Override
            public void loadFailed(FriendlyException exception) {
            }
        });
    }

    private void loadTrack(String identifier, Member author, Message msg) {


        Guild guild = author.getGuild();
        getPlayer(guild);

        msg.getTextChannel().sendTyping().queue();
        myManager.setFrameBufferDuration(5000);
        myManager.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {

                getTrackManager(guild).queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getSelectedTrack() != null) {
                    trackLoaded(playlist.getSelectedTrack());
                } else if (playlist.isSearchResult()) {
                    trackLoaded(playlist.getTracks().get(0));
                } else {

                    for (int i = 0; i < Math.min(playlist.getTracks().size(), PLAYLIST_LIMIT); i++) {
                        getTrackManager(guild).queue(playlist.getTracks().get(i), author);
                    }
                }
            }

            @Override
            public void noMatches() {
            }

            @Override
            public void loadFailed(FriendlyException exception) {
            }
        });
    }


    private boolean isIdle(Guild guild, MessageReceivedEvent event) {
        if (!hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null) {
            event.getTextChannel().sendMessage("No music is being played at the moment!").queue();
            return true;
        }
        return false;
    }

    private void forceSkipTrack(Guild guild) {
        getPlayer(guild).stopTrack();
    }

    private void sendHelpMessage(MessageReceivedEvent event) {

        String pre = STATICS.PREFIX;

        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(new Color(22, 138, 233))
                        .setDescription(NOTE + "  __**MUSIC PLAYER GUIDE**__\n\n")

                        .addField(pre + "m play <INPUT>",
                                "`SHORT:`  **" + pre + "m p <INPUT>**\n" +
                                        "`INPUT:`  YouTube/SoundCloud/Twitch/BandCamp - URL of track or playlist\n\n" +
                                        "*If queue is empty:*  Starts the player with the entered track / playlist.\n" +
                                        "*If queue is playing:*  Attaches the entered track or playlist at the end of the queue.\n" +
                                        ":heavy_minus_sign: :heavy_minus_sign: :heavy_minus_sign: ",  false)

                        .addField(pre + "m ytplay <INPUT>",
                                "`SHORT:`  **" + pre + "m yp <INPUT>**\n" +
                                        "`INPUT:`  Search query string for YouTube\n\n" +
                                        "*If queue is empty:*  Starts the player with the first result of the search.\n" +
                                        "*If queue is playing:*  Attaches the first result of the search at the end of the queue.\n" +
                                        ":heavy_minus_sign: :heavy_minus_sign: :heavy_minus_sign: ",  false)

                        .addField(pre + "m playshuffle <INPUT>",
                                "`SHORT:`  **" + pre + "m ps <INPUT>**\n" +
                                        "`INPUT:`  YouTube/SoundCloud/Twitch/BandCamp - URL of track or playlist\n\n" +
                                        "Same behaviour like play, but shuffleing the whole queue after attaching.\n" +
                                        ":heavy_minus_sign: :heavy_minus_sign: :heavy_minus_sign: ",  false)

                        .addField(pre + "m playnext <INPUT>",
                                "`SHORT:`  **" + pre + "m pn <INPUT>**\n" +
                                        "`INPUT:`  YouTube/SoundCloud/Twitch/BandCamp - URL of track or playlist\n\n" +
                                        "Enqueues the entered track/playlist after the now playing track without reset of the default queue.\n" +
                                        ":heavy_minus_sign: :heavy_minus_sign: :heavy_minus_sign: ",  false)

                        .addField(pre + "m skip <OPTIONAL INPUT>",
                                "`SHORT:`  **" + pre + "m s <OPTIONAL INPUT>**\n" +
                                        "`OPTIONAL INPUT:`  Count to skip. *Without:* Skips current playing track.\n\n" +
                                        "Skip current or multiple tracks in queue.\n" +
                                        ":heavy_minus_sign: :heavy_minus_sign: :heavy_minus_sign: ",  false)

                        .addField(pre + "m now",
                                "`SHORT:`  **" + pre + "m n**\n\n" +
                                        "Shows information about the current playing track and shows the next track in queue.\n" +
                                        ":heavy_minus_sign: :heavy_minus_sign: :heavy_minus_sign: ",  false)

                        .addField(pre + "m stop",
                                "Stops the current playing. The current queue will be reset after this!\n" +
                                        ":heavy_minus_sign: :heavy_minus_sign: :heavy_minus_sign: ",  false)

                        .addField(pre + "m queue <OPTIONAL INPUT>",
                                "`OPTIONAL INPUT:`  Side of list.\n\n" +
                                        "\nShows the current queue. Displays only 20 tracks on one side. Use Optional input to switch between sides.\n" +
                                        ":heavy_minus_sign: :heavy_minus_sign: :heavy_minus_sign: ",  false)

                        .addBlankField(false)
                        .build()
        ).queue();
    }

    private String buildQueueMessage(AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }

    private String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    private AudioEventListener audioEventListener = new AudioEventAdapter() {
        @Override
        public void onTrackStart(AudioPlayer player, AudioTrack track) {
            if (guild.getTextChannelsByName(STATICS.getMusicChannel(), true).size() > 0) {
                guild.getTextChannelsByName(STATICS.getMusicChannel(), true).get(0).getManager().setTopic(
                        track.getInfo().title
                ).queue();

                Set<AudioInfo> queue = getTrackManager(guild).getQueuedTracks();
                ArrayList<AudioInfo> tracks = new ArrayList<>();
                queue.forEach(tracks::add);

                try {
                    EmbedBuilder eb = new EmbedBuilder()
                            .setColor(Color.CYAN)
                            .setDescription(NOTE + "   **Now Playing**   ")
                            .addField("Current Track", "`(" + getTimestamp(track.getDuration()) + ")`  " + track.getInfo().title, false)
                            .addField("Next Track", "`(" + getTimestamp(tracks.get(1).getTrack().getDuration()) + ")`  " + tracks.get(1).getTrack().getInfo().title, false);
                    guild.getTextChannelsByName(STATICS.getMusicChannel(), true).get(0).sendMessage(
                            eb.build()
                    ).queue();
                } catch (Exception e) {}
            }
        }

        @Override
        public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
            EmbedBuilder eb = new EmbedBuilder().setColor(Color.GREEN);
            if (getTrackManager(guild).getQueuedTracks().size() < 2 && endlessMode) {
                endlessList.forEach(t -> getTrackManager(guild).queue(t, endlessAuthor));
                if (guild.getTextChannelsByName(STATICS.getMusicChannel(), true).size() > 0)
                    guild.getTextChannelsByName(STATICS.getMusicChannel(), true).get(0).sendMessage(eb.setDescription("Repeated queue. *(endless mode)*").build()).queue();
            }

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (player.getPlayingTrack() == null) {
                        if (guild.getTextChannelsByName(STATICS.getMusicChannel(), true).size() > 0) {
                            guild.getTextChannelsByName(STATICS.getMusicChannel(), true).get(0).getManager().setTopic(
                                    "-music help"
                            ).queue();
                        }
                    }
                }
            }, 500);

        }
    };

    public Music() {
        AudioSourceManagers.registerRemoteSources(myManager);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) throws ParseException, IOException {

        guild = event.getGuild();

        if (!event.getTextChannel().getName().equals(STATICS.getMusicChannel())) {
            Message msg = event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(":warning:  " + event.getAuthor().getAsMention() + ", please only send music commands in the #music channel!").build()).complete();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    msg.delete().queue();
                    event.getMessage().delete().queue();
                }
            }, 3000);
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();

        getPlayer(guild).removeListener(audioEventListener);
        getPlayer(guild).addListener(audioEventListener);


        switch (args.length) {
            case 0:
                sendHelpMessage(event);
                break;

            case 1:
            case 2:
                switch (args[0].toLowerCase()) {

                    case "help":
                        sendHelpMessage(event);
                        CMD_REACTION.negative(event);
                        break;

                    case "channel":
                        if (args.length < 2) {
                            event.getTextChannel().sendMessage(ErrorEmbeds.errorEmbed(null,null).setDescription(help()).build()).queue();
                            CMD_REACTION.negative(event);

                            return;
                        }
                        try {
                            Settings.setMusicChannel(args[1].toLowerCase());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        CMD_REACTION.positive(event);

                        break;

                    case "n":
                    case "now":
                    case "current":
                    case "nowplaying":
                    case "info":
                        if (!hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null) {
                            event.getTextChannel().sendMessage(NOTE + "No music currently playing!").queue();
                            CMD_REACTION.negative(event);

                        } else {
                            AudioTrack track = getPlayer(guild).getPlayingTrack();
                            AudioTrackInfo info = track.getInfo();
                            eb
                                    .setColor(Color.orange)
                                    .setDescription(":musical_note:   **Current Track Info**")
                                    .addField(":cd:  Title", info.title, false)
                                    .addField(":stopwatch:  Duration", "`[ " + getTimestamp(track.getPosition()) + " / " + getTimestamp(track.getInfo().length) + " ]`", false)
                                    .addField(":microphone:  Channel / Author", info.author, false);
                            event.getTextChannel().sendMessage(
                                    eb.build()
                            ).queue();
                            CMD_REACTION.positive(event);

                        }
                        break;

                    case "queue":
                        if (!hasPlayer(guild) || getTrackManager(guild).getQueuedTracks().isEmpty()) {
                            event.getTextChannel().sendMessage(NOTE + "The queue ist currently empty!").queue();
                            CMD_REACTION.negative(event);

                        } else {
                            CMD_REACTION.positive(event);

                            int SideNumbInput = 1;
                            if (args.length > 1)
                                SideNumbInput = Integer.parseInt(args[1]);

                            StringBuilder sb = new StringBuilder();
                            Set<AudioInfo> queue = getTrackManager(guild).getQueuedTracks();
                            ArrayList<String> tracks = new ArrayList<>();
                            List<String> tracksSublist;
                            queue.forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

                            if (tracks.size() > 20)
                                tracksSublist = tracks.subList((SideNumbInput-1)*20, (SideNumbInput-1)*20+20);
                            else
                                tracksSublist = tracks;

                            tracksSublist.forEach(s -> sb.append(s));
                            int sideNumbAll = tracks.size() >= 20 ? tracks.size() / 20 : 1;
                            int sideNumb = SideNumbInput;

                            eb.setColor(Color.GREEN).setDescription(
                                    NOTE + "**QUEUE**\n\n" +
                                            "*[" + queue.size() + " Tracks | Side " + sideNumb + "/" + sideNumbAll + "]*\n\n" +
                                            sb
                            );

                            event.getTextChannel().sendMessage(
                                    eb.build()
                            ).queue();

                        }
                        break;

                    case "s":
                    case "skip":
                        CMD_REACTION.positive(event);

                        for (int skip = (args.length > 1 ? Integer.parseInt(args[1]) : 1); skip > 0; skip--) {
                                if (isIdle(guild, event)) return;
                                forceSkipTrack(guild);
                            }
                        
                        break;

                    case "stop":
                        CMD_REACTION.positive(event);

                        getTrackManager(guild).purgeQueue();
                        forceSkipTrack(guild);
                        guild.getAudioManager().closeAudioConnection();

                        endlessMode = false;
                        endlessList.clear();

                        break;


                    case "endless":
                        CMD_REACTION.positive(event);

                        getTrackManager(guild).getQueuedTracks().stream().skip(1).forEach(t -> endlessList.add(t.getTrack()));
                        endlessMode = true;
                        endlessAuthor = event.getMember();

                        //event.getTextChannel().sendMessage(MSGS.success().setDescription(":repeat:  Endless mode activated.").build()).queue();

                        break;


                    case "reset":
                        CMD_REACTION.positive(event);

                        reset(guild);
                        event.getTextChannel().sendMessage("\uD83D\uDD04 Resetting the music player..");
                        break;

                    case "shuffle":
                        if (isIdle(guild, event)) return;
                        CMD_REACTION.positive(event);
                        getTrackManager(guild).shuffleQueue();
                        event.getTextChannel().sendMessage(NOTE + "Shuffled queue.  :twisted_rightwards_arrows: ").queue();
                        
                        break;

                    case "pause":
                    case "resume":
                        CMD_REACTION.positive(event);
                        if (getPlayer(guild).isPaused()) {
                            getPlayer(guild).setPaused(false);
                            event.getTextChannel().sendMessage(
                                    NOTE + "Player resumed."
                            ).queue();
                        } else {
                            getPlayer(guild).setPaused(true);
                            event.getTextChannel().sendMessage(
                                    NOTE + "Player paused."
                            ).queue();
                        }
                        break;

                    case "clue":
                        loadTrack(clueURL, event.getMember(), event.getMessage());
                        if (getPlayer(guild).isPaused())
                            getPlayer(guild).setPaused(false);
                        break;
                }

            default:

                String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                if (input != null && input.startsWith("http"))
                    STATICS.input = input;
                else
                    input = "ytsearch: " + input;

                switch (args[0].toLowerCase()) {

                    case "p":
                    case "play":
                        if (args.length <= 1) {
                            CMD_REACTION.negative(event);
                            event.getTextChannel().sendMessage(":warning:  Please include a valid source.").queue();
                        } else {
                            loadTrack(input, event.getMember(), event.getMessage());
                            CMD_REACTION.positive(event);
                            if (getPlayer(guild).isPaused())
                                getPlayer(guild).setPaused(false);

                            new Timer().schedule(
                                    new java.util.TimerTask() {
                                        @Override
                                        public void run() {
                                            int tracks = getTrackManager(guild).getQueuedTracks().size();
                                            event.getTextChannel().sendMessage(
                                                    new EmbedBuilder().setDescription(NOTE + "Queued `" + tracks + "` Tracks.").setColor(new Color(0, 255, 151)).build()
                                            ).queue();
                                        }
                                    },
                                    5000
                            );
                        }
                        break;

                    case "ps":
                    case "playshuffle":
                        if (args.length <= 1) {
                            CMD_REACTION.negative(event);
                            event.getTextChannel().sendMessage(ErrorEmbeds.errorEmbed(null,null).setDescription(":warning:  Please include a valid source.").build()).queue();
                        } else {
                            CMD_REACTION.positive(event);
                            loadTrack(input, event.getMember(), event.getMessage());

                            getTrackManager(guild).shuffleQueue();

                            if (getPlayer(guild).isPaused())
                                getPlayer(guild).setPaused(false);

                            new Timer().schedule(
                                    new java.util.TimerTask() {
                                        @Override
                                        public void run() {
                                            int tracks = getTrackManager(guild).getQueuedTracks().size();
                                            event.getTextChannel().sendMessage(
                                                    NOTE + "Queued `" + tracks + "` Tracks."
                                            ).queue();
                                        }
                                    },
                                    5000
                            );
                        }
                        break;

                    case "pn":
                    case "playnext":
                        if (args.length <= 1) {
                            CMD_REACTION.negative(event);
                            event.getTextChannel().sendMessage(ErrorEmbeds.errorEmbed(null,null).setDescription(":warning:  Please include a valid source.").build()).queue();
                        } else {
                            CMD_REACTION.positive(event);
                            loadTrackNext(input, event.getMember(), event.getMessage());

                            new Timer().schedule(
                                    new java.util.TimerTask() {
                                        @Override
                                        public void run() {
                                            int tracks = getTrackManager(guild).getQueuedTracks().size();
                                            event.getTextChannel().sendMessage(
                                                    NOTE + "Queued `" + tracks + "` Tracks."
                                            ).queue();
                                        }
                                    },
                                    5000
                            );
                        }
                        break;

                }
                break;
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return
                ":musical_note:  **MUSIC PLAYER**  :musical_note: \n\n" +
                        "` -music play <yt/soundcloud - URL> `  -  Start playing a track / Add a track to queue / Add a playlist to queue\n" +
                        "` -music playnext <yt/soundcloud - URL>  -  Add track or playlist direct after the current song in queue`\n" +
                        "` -music ytplay <Search string for yt> `  -  Same like *play*, just let youtube search for a track you enter\n" +
                        "` -music queue <Side>`  -  Show the current music queue\n" +
                        "` -music skip `  -  Skip the current track in queue\n" +
                        "` -music now `  -  Show info about the now playing track\n" +
                        "` -music stop `  -  Stop the music player\n" +
                        "` -music channel <text channel> `  -  Set the channel where now playing will shown (use a channel that does not exist to disable messages)\n"
                ;
    }

    @Override
    public String description() {
        return "Play music";
    }

    @Override
    public String commandType() {
        return null;
    }

    @Override
    public int permission() {
        return 0;
    }
}