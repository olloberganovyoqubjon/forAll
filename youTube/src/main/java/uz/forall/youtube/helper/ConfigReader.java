package uz.forall.youtube.helper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;

public class ConfigReader {

    public static String readConfigFileToString(String name) {
        try {
            FileReader reader = new FileReader("config.jsonc");
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            String asString = jsonObject.get(name).getAsString();
            reader.close();
            return asString;
        } catch (IOException e) {
            return null;
        }
    }
    public static int readConfigFileToInt(String name) {
        try {
            FileReader reader = new FileReader("config.jsonc");
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            int asInt = jsonObject.get(name).getAsInt();
            reader.close();
            return asInt;
        } catch (IOException e) {
            return 0;
        }
    }
}

