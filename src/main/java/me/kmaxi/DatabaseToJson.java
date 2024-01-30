package me.kmaxi;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseToJson {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/escooterdata"; // Replace with your database name
        String user = "root";
        String password = "";

        String query = "SELECT xGrid, yGrid, xPos, yPos, IdleTime, TimeStamp, EntryID FROM table_44423"; // Replace with your table name

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            Map<String, JSONArray> gridMap = new HashMap<>();

            while (rs.next()) {
                String key = rs.getInt("xGrid") + "_" + rs.getInt("yGrid");
                JSONObject details = new JSONObject();
                details.put("xPos", rs.getDouble("xPos"));
                details.put("yPos", rs.getDouble("yPos"));
                details.put("idleTime", rs.getString("IdleTime"));
                details.put("timeStamp", rs.getDouble("TimeStamp"));
                details.put("entryId", rs.getInt("EntryID"));

                // Add to the map
                gridMap.computeIfAbsent(key, k -> new JSONArray()).add(details);
            }

            // Create the final JSON object
            JSONObject finalOutput = new JSONObject();
            JSONArray gridDataList = new JSONArray();

            for (Map.Entry<String, JSONArray> entry : gridMap.entrySet()) {
                JSONObject gridData = new JSONObject();
                gridData.put("key", entry.getKey());
                gridData.put("gridInfos", entry.getValue());
                gridDataList.add(gridData);
            }

            finalOutput.put("gridDataList", gridDataList);

            try (FileWriter file = new FileWriter("output.json")) {
                file.write(finalOutput.toJSONString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
