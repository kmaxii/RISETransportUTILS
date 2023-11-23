package me.kmaxi;

import com.github.goober.coordinatetransformation.positions.SWEREF99Position;
import com.github.goober.coordinatetransformation.positions.WGS84Position;
import com.google.gson.*;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CoordinateConverter {
    public static void main(String[] args) throws Exception {
        String jsonFilePath = "C:\\Users\\esst9\\Downloads\\test.json";
       // String jsonFilePath = "C:\\Users\\esst9\\Downloads\\path_to_your_json_file.json";
        String outputFilePath = "path_for_converted_json_file.json";

        String jsonString = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        convertCoordinates(jsonObject);

        try (Writer writer = new FileWriter(outputFilePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(jsonObject, writer);
        }
    }

    private static void convertCoordinates(JsonObject jsonObject) {
        JsonArray stopPoints = jsonObject.getAsJsonArray("stopPoints");
        for (JsonElement element : stopPoints) {
            JsonObject stopPoint = element.getAsJsonObject();
            JsonObject geometry = stopPoint.getAsJsonObject("geometry");
            double northing = geometry.get("northingCoordinate").getAsDouble();
            double easting = geometry.get("eastingCoordinate").getAsDouble();

            SWEREF99Position sweref99Position = new SWEREF99Position(northing, easting, SWEREF99Position.SWEREFProjection.sweref_99_tm);
            WGS84Position wgs84Position = sweref99Position.toWGS84();

            geometry.addProperty("northingCoordinate", wgs84Position.getLatitude());
            geometry.addProperty("eastingCoordinate", wgs84Position.getLongitude());
        }
    }
}
