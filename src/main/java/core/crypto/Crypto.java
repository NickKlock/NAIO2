package core.crypto;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

public class Crypto {

    public static void getJson( String currency){
        JSONParser parser = new JSONParser();

        try {
            URL url = new URL("http://api.coincap.io/v2/assets/?search="+currency); // URL to Parse
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept-Encoding","gzip");
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                /*JSONArray a = (JSONArray) parser.parse(inputLine);

                // Loop through each item
                for (Object o : a) {
                    JSONObject object = (JSONObject) o;

                    String id = (String) object.get("id");
                    System.out.println("Currency ID : " + id);
                    System.out.println("\n");
                }*/
            }
            System.out.println(stringBuilder.toString());
            in.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }
    }

    private static String readAll(Reader rd) throws IOException {

        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
