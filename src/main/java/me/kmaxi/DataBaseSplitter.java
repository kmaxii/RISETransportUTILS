package me.kmaxi;

import java.sql.*;

public class DataBaseSplitter {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/escooterdata";
    private static final String USER = "root";
    private static final String PASS = "";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            // Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Execute a query to get all data
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql = "SELECT * FROM vehicles";
            ResultSet rs = stmt.executeQuery(sql);

            // Process the result set
            while (rs.next()) {
                int entryID = rs.getInt("EntryID");
                String vehicleID = rs.getString("VehicleID");
                double x = rs.getDouble("X");
                double y = rs.getDouble("Y");
                double timeStamp = rs.getDouble("TimeStamp");
                String idleTime = rs.getString("IdleTime");
                double xPos = rs.getDouble("xPos");
                double yPos = rs.getDouble("yPos");

                // Convert timestamp to date and format it (assuming timeStamp is in Excel serial number format)
                String tableName = "Table_" + convertTimeStampToDate(timeStamp);

                // Create table if it doesn't exist
                createTableIfNotExist(conn, tableName);

                // Insert data into the respective table
                insertData(conn, tableName, entryID, vehicleID, x, y, timeStamp, idleTime, xPos, yPos);
            }

            // Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } finally {
            // Finally block used to close resources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }

    private static String convertTimeStampToDate(double timeStamp) {
        // Implement conversion logic from Excel serial number to date string
        // Placeholder for actual implementation

        return (int) timeStamp + ""; // Example: 441.0 -> "441"
    }

    private static void createTableIfNotExist(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData dbm = conn.getMetaData();
        // Check if table exists
        try (ResultSet tables = dbm.getTables(null, null, tableName, null)) {
            if (!tables.next()) {
                // Table does not exist, create it
                try (Statement stmt = conn.createStatement()) {
                    String sql = "CREATE TABLE " + tableName +
                            " (EntryID INT PRIMARY KEY, " +
                            "VehicleID VARCHAR(255), " +
                            "X DOUBLE, " +
                            "Y DOUBLE, " +
                            "TimeStamp DOUBLE, " +
                            "IdleTime VARCHAR(255), " +
                            "xPos DOUBLE, " +
                            "yPos DOUBLE)";
                    stmt.executeUpdate(sql);
                }
            }
        }
    }

    private static void insertData(Connection conn, String tableName, int entryID, String vehicleID, double x, double y, double timeStamp, String idleTime, double xPos, double yPos) throws SQLException {
        String sql = "INSERT INTO " + tableName + " (EntryID, VehicleID, X, Y, TimeStamp, IdleTime, xPos, yPos) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entryID);
            pstmt.setString(2, vehicleID);
            pstmt.setDouble(3, x);
            pstmt.setDouble(4, y);
            pstmt.setDouble(5, timeStamp);
            pstmt.setString(6, idleTime);
            pstmt.setDouble(7, xPos);
            pstmt.setDouble(8, yPos);

            pstmt.executeUpdate();
        }
    }
}
