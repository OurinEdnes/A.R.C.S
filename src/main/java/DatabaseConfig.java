import org.bytedeco.javacv.FrameGrabber;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {
    // 📍 Path Database & Log File (Sesuaikan sama path Senpai ya!)
    static final String Uav_Log_DB = "jdbc:sqlite:C:/Project Folder/GitHub/A.R.C.S/src/main/java/UAV-LOG/UAV-Log.db";
    public String LogCord = "C:/Project Folder/GitHub/A.R.C.S/src/main/java/UAV-LOG/20260113-182529.txt"; // Ganti ke nama file txt yg bener

    // Variable Data Dinamis (Dari TXT)
    public String alltitude;
    public String latitude_X;
    public String longitude_Z;

    // Object Data Statis (Dari Database SQLite)
    public UavComp UavComp; // Object Komponen
    public Type_UAV typeUAV;
    public Pilot pilot;
    public GCS gcs;
    public FlightMission flightMission;

    // 🏗️ CONSTRUCTOR UTAMA
    public DatabaseConfig() throws FrameGrabber.Exception {
        System.out.println("🦈 Loading Konfigurasi A.R.C.S...");

        // 1. AMBIL DATA DINAMIS (Dari File TXT via jurus Tail)
        String lastLine = tail(LogCord);
        if (lastLine != null) {
            try {
                String[] temp = lastLine.split(",");
                // [0]Type, [1]Date, [2]Lat, [3]Lon, [4]Acc, [5]Alt
                this.latitude_X = temp[2];
                this.longitude_Z = temp[3];
                this.alltitude = temp[5] + " mdpl";
            } catch (Exception e) {
                System.out.println("💥 Error parsing TXT: " + e.getMessage());
            }
        }

        // 2. AMBIL DATA STATIS (Dari Database SQLite)

        // === UavComp ===
        String dataComp = GetLog("SELECT * FROM UAV_Components LIMIT 1");
        if (dataComp != null) {
            String[] c = dataComp.split(",");
            boolean hasLidar = (c.length > 2 && !c[2].equalsIgnoreCase("null") && !c[2].isEmpty());
            boolean hasThermal = (c.length > 3 && !c[3].equalsIgnoreCase("null") && !c[3].isEmpty());

            // ID di index 0, logic boolean custom
            this.UavComp = new UavComp(c[1], hasLidar, hasThermal, 1);
        }

        // === Type_UAV ===
        String dataType = GetLog("SELECT * FROM Type_UAV LIMIT 1");
        if (dataType != null) {
            String[] t = dataType.split(",");
            int krit = 0;
            try {
                krit = Integer.parseInt(t[1]);
            } catch (NumberFormatException e) {
                // Handle jika parsing gagal
            }
            // Format: (Lokasi, Status, Baterai, ID, Kritikal, KomponenObjek)
            this.typeUAV = new Type_UAV(this.latitude_X + "," + this.longitude_Z + "," + this.alltitude, "DISARMING", 100.0f, t[0], krit, this.UavComp);
        }

        // === Pilot ===
        String dataPilot = GetLog("SELECT * FROM Pilot LIMIT 1");
        if (dataPilot != null) {
            String[] p = dataPilot.split(",");
            this.pilot = new Pilot(p[1], p[2]);
        }

        // === GCS ===
        String dataGCS = GetLog("SELECT * FROM GCS LIMIT 1");
        if (dataGCS != null) {
            String[] g = dataGCS.split(",");
            this.gcs = new GCS(g[0], g[1]);
        }

        // === FlightMission ===
        String dataMission = GetLog("SELECT * FROM Mission_Task LIMIT 1");
        if (dataMission != null) {
            String[] m = dataMission.split(",");
            // Asumsi constructor FlightMission(Name, Objective, Type_UAV)
            this.flightMission = new FlightMission(m[1], m[2], this.typeUAV);
        }

        System.out.println("✅ Konfigurasi Selesai Diload!");
    }

    // 🥷 JURUS TAIL
    public static String tail(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return null;

        try (RandomAccessFile fileHandler = new RandomAccessFile(file, "r")) {
            long fileLength = fileHandler.length() - 1;
            StringBuilder sb = new StringBuilder();

            for (long pointer = fileLength; pointer != -1; pointer--) {
                fileHandler.seek(pointer);
                int readByte = fileHandler.readByte();

                if (readByte == 0xA) {
                    if (pointer == fileLength) continue;
                    break;
                } else if (readByte == 0xD) {
                    if (pointer == fileLength - 1) continue;
                    break;
                }
                sb.append((char) readByte);
            }
            return sb.reverse().toString();
        } catch (IOException e) {
            return null;
        }
    }

    // 📦 JURUS GETLOG
    public static String GetLog(String query) {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(Uav_Log_DB);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            int colCount = rs.getMetaData().getColumnCount();

            if (rs.next()) {
                for (int i = 1; i <= colCount; i++) {
                    String val = rs.getString(i);
                    if (val == null) val = "null";

                    sb.append(val);
                    if (i < colCount) sb.append(",");
                }
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println("💥 Gagal baca DB: " + e.getMessage());
            return null;
        }
        return sb.toString();
    }

    // Override toString biar pas di-print di Main outputnya ganteng
    @Override
    public String toString() {
        return "=== STATUS DATABASE A.R.C.S ===\n" +
                "Lat/Lon  : " + latitude_X + " / " + longitude_Z + "\n" +
                "Altitude : " + alltitude + "\n" +
                "Drone ID : " + (typeUAV != null ? typeUAV.getID() : "Unknown") + "\n" +
                "===============================";
    }
}