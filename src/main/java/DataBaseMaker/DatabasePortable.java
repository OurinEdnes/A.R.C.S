package DataBaseMaker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

public class DatabasePortable {
    // 1. Cari tau kita lagi di folder mana
    private static final String PROJECT_ROOT = System.getProperty("user.dir");

    // 2. Sambungin ke lokasi database (Pastikan path ini valid di struktur project Senpai ya!)
    // Aiko ganti sedikit path-nya biar aman kalau dijalankan langsung tanpa package structure yang ribet
    private static final String DB_DIR_PATH = PROJECT_ROOT + File.separator + "UAV-LOG";
    private static final String DB_FILE_PATH = DB_DIR_PATH + File.separator + "UAV-Log.db";

    // 3. Format JDBC URL buat SQLite
    static final String DB_URL = "jdbc:sqlite:" + DB_FILE_PATH;

//    public static void main(String[] args) {
//        System.out.println(" --- MEMULAI SISTEM DATABASE A.R.C.S (PORTABLE MODE by Aiko) --- \n");
//
//        // Debugging Path biar Senpai tenang hatinya
//        System.out.println("Root Project : " + PROJECT_ROOT);
//        System.out.println("Target Folder: " + DB_DIR_PATH);
//        System.out.println("Target DB    : " + DB_FILE_PATH);
//        System.out.println("--------------------------------------------------\n");
//
//        DatabasePortable db = new DatabasePortable();
//        db.createNewTables();
//
//        System.out.println("\n --- SEKSI PILOT (Auto-Reset Mode) ---");
//        // Data lama akan hilang, diganti yang ini
//        db.insertPilot("PLT-001", "Handoyo", "LIC-GOD-MODE");
//        showData("Pilot", "SELECT * FROM Pilot");
//
//        // 3. Simulasi Data TIPE UAV
//        System.out.println("\n --- SEKSI TIPE UAV (Auto-Reset Mode) ---");
//        db.insertUAVType("COMP-01", "Fixed Wing Long Range");
//        showData("Type_UAV", "SELECT * FROM Type_UAV");
//
//        // 4. Simulasi KOMPONEN
//        System.out.println("\n --- SEKSI KOMPONEN (Auto-Reset Mode) ---");
//        // Aiko note: CamAdd tadinya INT, sekarang harus TEXT karena isinya URL
//        db.insertComponent("COMP-01", "Sony A6000", "Velodyne VLP-16", "FLIR Vue Pro", "http://192.168.137.225:8080/video");
//        showData("UAV_Components", "SELECT * FROM UAV_Components");
//
//        // 5. Simulasi GCS (Ground Control Station)
//        System.out.println("\n --- SEKSI GCS (Auto-Reset Mode) ---");
//        db.insertGCS("GCS-DIY", "Yogyakarta Base HQ");
//        showData("GCS", "SELECT * FROM GCS");
//
//        // 6. Simulasi MISI (Task)
//        System.out.println("\n --- SEKSI MISI (Auto-Reset Mode) ---");
//        db.insertMission(101, "Searching Korban", "Locating korban bencana");
//        showData("Mission_Task", "SELECT * FROM Mission_Task");
//
//        System.out.println("\n--- SEMUA SISTEM BERJALAN LANCAR! OTSUKARE SENPAI! --- ");
//    }

    public void createNewTables() {
        // Aiko Check: Bikin folder dulu kalau belum ada!
        File directory = new File(DB_DIR_PATH);
        if (!directory.exists()) {
            if(directory.mkdirs()){
                System.out.println("Magic: Folder 'UAV-LOG' berhasil dibuat otomatis!");
            } else {
                System.out.println("Warning: Gagal bikin folder, mungkin perlu izin admin/root.");
            }
        }

        // Mengubah CamAdd dari INT ke TEXT agar bisa menyimpan URL
        String[] sqls = {
                "CREATE TABLE IF NOT EXISTS Type_UAV (id CHAR(6) PRIMARY KEY, nama TEXT);",
                "CREATE TABLE IF NOT EXISTS UAV_Components (id CHAR(6) PRIMARY KEY, Cam TEXT, Lidar TEXT, Thermal TEXT, CamAdd TEXT);",
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
            System.out.println("Status: Struktur Database Siap Tempur! (Connected to SQLite)");

        } catch (SQLException e) {
            System.out.println("🦈 Gagal bikin tabel: " + e.getMessage());
            System.out.println("Cek apakah folder 'UAV-LOG' bisa diakses?");
        }
    }

    public void insertPilot(String id, String nama, String license) {
        executeUpdate("DELETE FROM Pilot");

        String sql = "INSERT INTO Pilot(id, Nama, LicenseID) VALUES(?,?,?)";
        executeInsert(sql, id, nama, license);
    }

    // Insert Tipe UAV
    public void insertUAVType(String id, String nama) {
        executeUpdate("DELETE FROM Type_UAV");

        String sql = "INSERT INTO Type_UAV(id, nama) VALUES(?,?)";
        executeInsert(sql, id, nama);
    }

    // Insert Komponen
    public void insertComponent(String id, String cam, String lidar, String thermal, String camAdd) {
        executeUpdate("DELETE FROM UAV_Components");

        String sql = "INSERT INTO UAV_Components(id, Cam, Lidar, Thermal, CamAdd) VALUES(?,?,?,?,?)";
        executeInsert(sql, id, cam, lidar, thermal, camAdd);
    }

    // Insert GCS
    public void insertGCS(String id, String location) {
        executeUpdate("DELETE FROM GCS");

        String sql = "INSERT INTO GCS(id, Location) VALUES(?,?)";
        executeInsert(sql, id, location);
    }

    // Insert Misi
    public void insertMission(int id, String name, String obj) {
        executeUpdate("DELETE FROM Mission_Task");

        String sql = "INSERT INTO Mission_Task(id, MissionName, Objective) VALUES(?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, obj);
            pstmt.executeUpdate();
            System.out.println("Misi Baru Ditambahkan (Data Lama Dihapus): " + name);
        } catch (SQLException e) {
            // Kalau error UNIQUE (ID kembar), kita biarin aja biar ga spam error
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
            System.out.println("Data Masuk (Data Lama Dihapus): " + args[0]);

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE") || e.getMessage().contains("primary key constraint")) {
                // Ignore duplicate entry quietly or just log simple message
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

            System.out.println("     [ Isi Tabel " + tableName + " ]");
            int colCount = rs.getMetaData().getColumnCount();

            if (!rs.isBeforeFirst()) {
                System.out.println("   (Tabel Kosong/Tidak ada data)");
            }

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

    public void executeUpdate(String sql, Object... params) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}