import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabasePortable {

    // 🦈 Aiko Upgrade: Path Dinamis (Jurus Anti-Nyasar)
    // 1. Cari tau kita lagi di folder mana
    private static final String PROJECT_ROOT = System.getProperty("user.dir");

    // 2. Sambungin ke lokasi database di dalam project structure
    private static final String DB_PATH = PROJECT_ROOT + "/src/main/java/UAV-LOG/UAV-Log.db";

    // 3. Format JDBC URL buat SQLite
    static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    public static void main(String[] args) {
        System.out.println(" --- MEMULAI SISTEM DATABASE A.R.C.S (PORTABLE MODE) --- \n");

        // Debugging Path biar Senpai tenang hatinya
        System.out.println("📂 Root Project : " + PROJECT_ROOT);
        System.out.println("🎯 Target DB    : " + DB_PATH);
        System.out.println("🔗 JDBC URL     : " + DB_URL);
        System.out.println("--------------------------------------------------\n");

        createNewTables();

        System.out.println("\n --- SEKSI PILOT ---");
        insertPilot("PLT-001", "Handoyo", "LIC-GOD-MODE");
        showData("Pilot", "SELECT * FROM Pilot");

        // 3. Simulasi Data TIPE UAV
        System.out.println("\n --- SEKSI TIPE UAV ---");
        insertUAVType("COMP-01", "Fixed Wing Long Range");
        showData("Type_UAV", "SELECT * FROM Type_UAV");

        // 4. Simulasi KOMPONEN
        System.out.println("\n --- SEKSI KOMPONEN ---");
        insertComponent("COMP-01", "Sony A6000", "Velodyne VLP-16", "FLIR Vue Pro", 2);
        showData("UAV_Components", "SELECT * FROM UAV_Components");

        // 5. Simulasi GCS (Ground Control Station)
        System.out.println("\n --- SEKSI GCS ---");
        insertGCS("GCS-DIY", "Yogyakarta Base HQ");
        showData("GCS", "SELECT * FROM GCS");

        // 6. Simulasi MISI (Task)
        System.out.println("\n --- SEKSI MISI ---");
        insertMission(101, "Searching Korban", "Locating korban bencana");
        showData("Mission_Task", "SELECT * FROM Mission_Task");

        System.out.println("\n --- SEMUA SISTEM BERJALAN LANCAR! --- ");
    }

    public static void createNewTables() {
        String[] sqls = {
                "CREATE TABLE IF NOT EXISTS Type_UAV (id CHAR(6) PRIMARY KEY, nama TEXT);",
                "CREATE TABLE IF NOT EXISTS UAV_Components (id CHAR(6) PRIMARY KEY, Cam TEXT, Lidar TEXT, Thermal TEXT, CamAdd INT);",
                "CREATE TABLE IF NOT EXISTS Pilot (id CHAR(6) PRIMARY KEY, Nama TEXT, LicenseID TEXT);",
                "CREATE TABLE IF NOT EXISTS GCS (id CHAR(6) PRIMARY KEY, Location TEXT);",
                "CREATE TABLE IF NOT EXISTS Mission_Task (id INTEGER PRIMARY KEY, MissionName TEXT, Objective TEXT);"
        };

        // Load Driver Explicitly (Just in case)
        try { Class.forName("org.sqlite.JDBC"); } catch (Exception e) {}

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            for (String sql : sqls) {
                stmt.execute(sql);
            }
            System.out.println("Status: Struktur Database Siap Tempur!");

        } catch (SQLException e) {
            System.out.println("💥 Gagal bikin tabel: " + e.getMessage());
            System.out.println("Cek apakah folder 'UAV-LOG' sudah ada?");
        }
    }

    public static void insertPilot(String id, String nama, String license) {
        String sql = "INSERT INTO Pilot(id, Nama, LicenseID) VALUES(?,?,?)";
        executeInsert(sql, id, nama, license);
    }

    // Insert Tipe UAV
    public static void insertUAVType(String id, String nama) {
        String sql = "INSERT INTO Type_UAV(id, nama) VALUES(?,?)";
        executeInsert(sql, id, nama);
    }

    // Insert Komponen
    public static void insertComponent(String id, String cam, String lidar, String thermal, int camAdd) {
        String sql = "INSERT INTO UAV_Components(id, Cam, Lidar, Thermal, CamAdd) VALUES(?,?,?,?,?)";
        executeInsert(sql, id, cam, lidar, thermal, camAdd);
    }

    // Insert GCS
    public static void insertGCS(String id, String location) {
        String sql = "INSERT INTO GCS(id, Location) VALUES(?,?)";
        executeInsert(sql, id, location);
    }

    // Insert Misi
    public static void insertMission(int id, String name, String obj) {
        String sql = "INSERT INTO Mission_Task(id, MissionName, Objective) VALUES(?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, obj);
            pstmt.executeUpdate();
            System.out.println("Misi Baru: " + name);
        } catch (SQLException e) {
            if (!e.getMessage().contains("UNIQUE")) System.out.println("Error Insert Misi: " + e.getMessage());
        }
    }

    private static void executeInsert(String sql, Object... args) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
            pstmt.executeUpdate();
            System.out.println("Data Masuk: " + args[0]);

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                System.out.println("Data " + args[0] + " sudah ada, skip.");
            } else {
                System.out.println("Error Insert: " + e.getMessage());
            }
        }
    }

    public static void showData(String tableName, String query) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("   [ Isi Tabel " + tableName + " ]");
            int colCount = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                StringBuilder row = new StringBuilder("   -> ");
                for (int i = 1; i <= colCount; i++) {
                    String colName = rs.getMetaData().getColumnName(i);
                    String val = rs.getString(i);
                    row.append(colName).append(": ").append(val).append(" | ");
                }
                System.out.println(row.toString());
            }
            System.out.println("   -----------------------------------");

        } catch (SQLException e) {
            System.out.println("Gagal baca data: " + e.getMessage());
        }
    }
}