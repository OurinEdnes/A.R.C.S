import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabasePortable {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:\\Project Folder\\GitHub\\A.R.C.S\\src\\main\\java\\UAV-LOG\\UAV-Log.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("🦈 Hore! Terkoneksi ke database file lokal!");

                // Coba bikin tabel (karena ini file baru)
                String Type_UAV = "CREATE TABLE Type_UAV IF NOT EXISTS barang (\n"
                        + " id char(6) PRIMARY KEY NOT NULL,\n"
                        + " nama text NOT NULL"
                        + ");";

                String UavComp = "CREATE TABLE UAVCOMP IF NOT EXISTS barang (\n"
                        + " id char(6) PRIMARY KEY NOT NULL,\n"
                        + " Cam text,\n"
                        + " Lidar text,\n"
                        + " Thermal text,\n"
                        + " CamAdd text\n"
                        + ");";

                String Pilot = "CREATE TABLE UAVCOMP IF NOT EXISTS barang (\n"
                        + " id char(6) PRIMARY KEY NOT NULL,\n"
                        + " Nama text,\n"
                        + " LicenseID text\n"
                        + ");";

                String GCS = "CREATE TABLE UAVCOMP IF NOT EXISTS barang (\n"
                        + " id char(6) PRIMARY KEY NOT NULL,\n"
                        + " Location text\n"
                        + ");";

                String task = "CREATE TABLE UAVCOMP IF NOT EXISTS barang (\n"
                        + " id int PRIMARY KEY NOT NULL,\n"
                        + " MissionName text,\n"
                        + " Objective text\n"
                        + ");";

                Statement stmt = conn.createStatement();
                stmt.execute(Type_UAV);
                System.out.println("📦 Tabel UAV berhasil dibuat/ditemukan.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}