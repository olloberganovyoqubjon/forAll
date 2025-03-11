package uz.forall.youtube.helper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;

public class ConfigReader {

    public static String readConfigFileToString(String name) {
        try {
            FileReader reader = new FileReader("config.json");
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            String asString = jsonObject.get(name).getAsString();
            reader.close();
            return asString;
        } catch (IOException e) {
            return null;
        }
    }
}

