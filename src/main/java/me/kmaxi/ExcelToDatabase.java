package me.kmaxi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Iterator;

public class ExcelToDatabase {

    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/escooterdata";
        String username = "root";
        String password = "";

        String excelFilePath = "IdleTIme_data.xlsx";

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {

            IOUtils.setByteArrayMaxOverride(200_000_000); // Adjust the size accordingly


            // Create table if not exists
            String sqlCreate = "CREATE TABLE IF NOT EXISTS Vehicles (" +
                    "VehicleID VARCHAR(255), " +
                    "X DOUBLE, " +
                    "Y DOUBLE, " +
                    "TimeStamp DOUBLE, " +
                    "IdleTime VARCHAR(255), " +
                    "EntryID INT NOT NULL AUTO_INCREMENT, " +
                    "PRIMARY KEY (EntryID))";
            try (Statement statement = connection.createStatement()) {
                statement.execute(sqlCreate);
            }

            // Read Excel file
            FileInputStream inputStream = new FileInputStream(excelFilePath);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();

            // Skip header row
            if (rowIterator.hasNext()) rowIterator.next();

            // Prepare SQL statement
            String sqlInsert = "INSERT INTO Vehicles (VehicleID, X, Y, TimeStamp, IdleTime) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();

 /*               String vehicleID = cellIterator.next().getStringCellValue();
                System.out.println("VehicleID: " + vehicleID);

                Double x = cellIterator.next().getNumericCellValue();
                System.out.println("X: " + x);

                Double y = cellIterator.next().getNumericCellValue();
                System.out.println("Y: " + y);

               // System.out.println("Type: " + cellIterator.next().getCellType());
                Double timeStamp = cellIterator.next().getNumericCellValue();
                System.out.println("TimeStamp: " + timeStamp);

                String idleTime = cellIterator.next().getStringCellValue();
                System.out.println("IdleTime: " + idleTime);*/

                preparedStatement.setString(1, cellIterator.next().getStringCellValue()); // VehicleID


                preparedStatement.setDouble(2, cellIterator.next().getNumericCellValue()); // X

                preparedStatement.setDouble(3, cellIterator.next().getNumericCellValue()); // Y

                preparedStatement.setDouble(4, cellIterator.next().getNumericCellValue()); // TimeStamp

                String idleTime = cellIterator.hasNext() ? cellIterator.next().getStringCellValue() : "'00:00:00";

                preparedStatement.setString(5, idleTime); // IdleTime

                preparedStatement.addBatch();

                if (nextRow.getRowNum() % 1000 == 0) {
                    preparedStatement.executeBatch(); // Execute every 1000 rows.
                }
            }

            preparedStatement.executeBatch(); // Execute the remaining rows

            workbook.close();
            inputStream.close();
            System.out.println("Data successfully inserted into the database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
