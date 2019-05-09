package core;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.text.DecimalFormat;

public class Crypto {

    public static void getCurrency(String currency, MessageReceivedEvent e){
        try {
            sendCurrencyMessage(getJsonCurrency(currency),e);
        }catch (IndexOutOfBoundsException ne){
            EmbedBuilder err = new EmbedBuilder().setColor(Color.RED).setDescription("Currency not found are you sure u typed it correct?");
            e.getTextChannel().sendMessage(err.build()).complete();
            util.CMD_REACTION.negative(e);
        }
    }

    public static String getRankList(){
        JSONArray jsonList = getJsonList();
        StringBuilder list = new StringBuilder();
        int counter = 1;
        for (Object o :jsonList) {
            JSONObject jo = (JSONObject) o;
            String name = jo.get("name").toString();
            list.append("#").append(counter).append(" - ").append(name).append("\n");
            counter++;
        }
        return list.toString();
    }
    private static JSONObject getJsonCurrency(String currency){
        try {
            JSONParser parser = new JSONParser();

            URL url = new URL("https://api.coincap.io/v2/assets/?search="+currency); // URL to Parse
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent","Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                JSONObject jo = (JSONObject) parser.parse(inputLine);
                JSONArray data = (JSONArray) jo.get("data");

                return (JSONObject) data.get(0);
            }
            in.close();
        } catch (IOException | ParseException error) {
            error.printStackTrace();
            return null;
        }
        return null;
    }

    private static JSONArray getJsonList(){
        try {
            JSONParser parser = new JSONParser();

            URL url = new URL("https://api.coincap.io/v2/assets/?limit=25"); // URL to Parse
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent","Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                JSONObject jo = (JSONObject) parser.parse(inputLine);
                JSONArray data = (JSONArray) jo.get("data");

                return data;
            }
            in.close();
        } catch (IOException | ParseException error) {
            error.printStackTrace();
            return null;
        }
        return null;
    }

    private static void sendCurrencyMessage(JSONObject jo, MessageReceivedEvent event){
        util.CMD_REACTION.positive(event);
        String maxSupplyStr;
        String availSupplyStr;
        String marketCapStr ;
        String volumeUsd24HhrStr;

        double price;
        try {
            price = Double.valueOf(jo.get("priceUsd").toString());
        } catch (NullPointerException e) {
            price = 0;
        }

        double maxSupply;
        try {
            maxSupply = Double.valueOf(jo.get("maxSupply").toString());
            if (maxSupply >= 1000000){
                maxSupplyStr = millionConvert(maxSupply);
            }else{
                maxSupplyStr = doubleDigit(maxSupply);
            }
        } catch (NullPointerException e) {
            maxSupply = 0;
            maxSupplyStr = "0";
        }

        double availSupply;
        try {
            availSupply = Double.valueOf(jo.get("supply").toString());
            if (availSupply >= 1000000){
                availSupplyStr = millionConvert(availSupply);
            }else{
                availSupplyStr = doubleDigit(availSupply);
            }

        } catch (NullPointerException e) {
            availSupply = 0;
            availSupplyStr = "0";
        }

        double marketCap;
        try {
            marketCap = Double.valueOf(jo.get("marketCapUsd").toString());
            if (marketCap >= 1000000){
                marketCapStr = millionConvert(marketCap);
            }else{
                marketCapStr = doubleDigit(marketCap);
            }
        } catch (NullPointerException e) {
            marketCap = 0;
            marketCapStr = "0";
        }

        double volumeUsd24Hhr;
        try {
            volumeUsd24Hhr = Double.valueOf(jo.get("volumeUsd24Hr").toString());
            if (volumeUsd24Hhr >= 1000000){
                volumeUsd24HhrStr = millionConvert(volumeUsd24Hhr);
            }else{
                volumeUsd24HhrStr = doubleDigit(marketCap);
            }

        } catch (NullPointerException e) {
            volumeUsd24Hhr = 0;
            volumeUsd24HhrStr = "0";

        }

        double change24;
        try {
            change24 = Double.valueOf(jo.get("changePercent24Hr").toString());

        } catch (NullPointerException e) {
            change24 = 0;
        }

        double vwap24Hr;
        try {
            vwap24Hr = Double.valueOf(jo.get("vwap24Hr").toString());

        } catch (NullPointerException e) {
            vwap24Hr = 0;
        }

        EmbedBuilder message = new EmbedBuilder().setColor(ColorUIResource.CYAN)
                .setTitle(jo.get("name").toString())
                .addField(":atom: Symbol", jo.get("symbol").toString(), true)
                .addField(":id: Unique ID", jo.get("id").toString(), true)
                .addField(":moneybag: Price", doubleDigit(price) + " USD", true)
                .addField(":gem: Total supply", maxSupplyStr + " " + jo.get("symbol").toString(), true)
                .addField(":o: Available supply ", availSupplyStr + " " + jo.get("symbol").toString(), true)
                .addField(":money_with_wings: Market cap", marketCapStr + " USD", true)
                .addField(":dollar: Last 24h traded", volumeUsd24HhrStr + " USD", true)
                .addField(":calendar: Change last 24h", doubleDigit(change24) + " %", true)
                .addField(":credit_card: Average Price 24h", doubleDigit(vwap24Hr) + " USD", true);
        event.getTextChannel().sendMessage(message.build()).complete();
    }

    private static String doubleDigit(double d){
        return new DecimalFormat("##.##").format(d);
    }
    private static String millionConvert(double d){
        return String.format("%.2f Mio.", d/ 1000000.0);
    }
}
