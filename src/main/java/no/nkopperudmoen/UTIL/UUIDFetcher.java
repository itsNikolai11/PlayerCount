package no.nkopperudmoen.UTIL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class UUIDFetcher {
    public static String getName(UUID uuid) {
        //TODO implementer
        //https://api.mojang.com/user/profiles/5e3106d6-8179-4a73-aa3e-21b5cc25dd59/names
        return "EmptyName";
    }

    public static UUID getUUID(String playername) {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + playername;
        try {
            URL urlen = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlen.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            if (connection.getResponseCode() != 200) {
                return null;
            }
            String currentLine;
            StringBuilder jsonString = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((currentLine = reader.readLine()) != null) {
                jsonString.append(currentLine);
            }
            connection.disconnect();
            JSONParser parser = new JSONParser();
            JSONObject result = (JSONObject) parser.parse(jsonString.toString());
            String uuid = result.get("id").toString();
            System.out.println(uuid);

            return UUID.fromString(HyphenateUUID(uuid));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String HyphenateUUID(String uuid) {
        StringBuilder hyphenated = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            if (i == 7 || i == 11 || i == 15 || i == 19) {
                hyphenated.append(uuid.charAt(i)).append("-");
            } else {
                hyphenated.append(uuid.charAt(i));
            }
        }
        System.out.println(hyphenated);
        return hyphenated.toString();
    }
}
