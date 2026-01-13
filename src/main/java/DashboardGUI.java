import org.bytedeco.javacv.FrameGrabber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

public class DashboardGUI extends JFrame {
    // Komponen GUI
    private JLabel lblStatus, lblBatt, lblCoord, lblID;
    private JProgressBar barBaterai;
    private JTextArea logArea;
    private JButton btnStart, btnStop;

    // Data Drone (Disimpan biar bisa diakses tombol)
    private Type_UAV selectedDrone;
    private UavComp droneSensor;

    // System.getProperty("user.dir") = Mengambil folder root project secara otomatis
    // Jadi mau folder projectnya dipindah kemana aja, dia tetep tau jalan pulang!
    // Asumsi: File S12.png ada di folder paling luar project (A.R.C.S/)
    public String PATH_GAMBAR_PETA = System.getProperty("user.dir") + "/S12.png";

    public DashboardGUI(Type_UAV drone, UavComp sensor) {
        this.selectedDrone = drone;
        this.droneSensor = sensor;

        // 1. Setup Jendela Utama (Frame)
        setTitle("A.R.C.S. Ground Control Station");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(Color.DARK_GRAY);

//        JFXPanel panelPeta = new JFXPanel();
//        panelPeta.setBorder(BorderFactory.createTitledBorder("📍 Live Tracking Map"));
//        panelPeta.setPreferredSize(new Dimension(500, 500));
//
//        add(panelPeta, BorderLayout.CENTER);
//
//        String htmlMap = "<html>"
//                + "<head>"
//                + "    <title>UAV Tracking</title>"
//                + "    <style>"
//                + "       html, body, gmp-map { height: 100%; margin: 0; padding: 0; }"
//                + "    </style>"
//                // Script Google Maps (Jangan lupa API KEY-nya ya Senpai!)
//                + "    <script src='https://maps.googleapis.com/maps/api/js?key=AIzaSyA6myHzS10YXdcazAFalmXvDkrYCp5cLc8&libraries=maps,marker&v=weekly' defer></script>"
//                + "</head>"
//                + "<body>"
//                + "    <gmp-map center='-7.82364089, 110.38523845' zoom='14' map-id='DEMO_MAP_ID'>"
//                + "        <gmp-advanced-marker position='-7.82364089, 110.38523845' title='Posisi UAV'></gmp-advanced-marker>"
//                + "    </gmp-map>"
//                + "</body>"
//                + "</html>";
//
//        Platform.runLater(() -> {
//            WebView webView = new WebView();
//            // Load HTML String tadi ke dalam browser mini ini
//            webView.getEngine().loadContent(htmlMap);
//
//            // Masukin browsernya ke panel
//            panelPeta.setScene(new Scene(webView));
//        });

        // === PANEL KIRI (GAMBAR STATIS) ===
        JPanel panelPeta = new JPanel(new BorderLayout());
        panelPeta.setBorder(BorderFactory.createTitledBorder("📍 Area Operasi (Static Map)"));
        panelPeta.setPreferredSize(new Dimension(600, 500));
        panelPeta.setBackground(Color.BLACK); // Background hitam biar elegan

        // Logic Load Gambar
        // Debugging: Print path-nya biar Senpai tau dia nyari kemana

        ImageIcon originalIcon = new ImageIcon(PATH_GAMBAR_PETA);

        // Cek gambarnya ketemu gak? (Kalau width -1 berarti gak ketemu)
        if (originalIcon.getIconWidth() == -1) {
            // Kalau gambar GAK ADA, tampilin pesan error keren
            JLabel errorLabel = new JLabel("<html><center>❌ MAP IMAGE NOT FOUND<br>Pastikan file S12.png ada di:<br>" + PATH_GAMBAR_PETA + "</center></html>", SwingConstants.CENTER);
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
            panelPeta.add(errorLabel, BorderLayout.CENTER);
        } else {
            Image img = originalIcon.getImage();

            Image resizedImg = img.getScaledInstance(600, 480, Image.SCALE_SMOOTH);
            JLabel mapLabel = new JLabel(new ImageIcon(resizedImg));

            panelPeta.add(mapLabel, BorderLayout.CENTER);
        }

        add(panelPeta, BorderLayout.CENTER);

        // === PANEL KANAN (Kontrol & Telemetry) ===
        JPanel panelKanan = new JPanel();
        panelKanan.setLayout(new BoxLayout(panelKanan, BoxLayout.Y_AXIS));
        panelKanan.setBackground(Color.DARK_GRAY);
        panelKanan.setPreferredSize(new Dimension(280, 500));

        // A. Bagian Telemetry (Info)
        JPanel panelInfo = new JPanel(new GridLayout(4, 1));
        panelInfo.setBackground(Color.DARK_GRAY);
        panelInfo.setBorder(BorderFactory.createTitledBorder(null, "TELEMETRY", 0, 0, null, Color.CYAN));

        lblID = buatLabel("ID: " + drone.getID());
        lblStatus = buatLabel("STATUS: " + drone.getStat());
        lblStatus.setForeground(Color.RED); // Awalnya Merah (Disarmed)

        lblCoord = buatLabel("LOC: " + drone.getX() +  ", " + drone.getZ());

        // Bar Baterai
        barBaterai = new JProgressBar(0, 100);
        barBaterai.setValue((int) drone.getBatt());
        barBaterai.setStringPainted(true);
        barBaterai.setForeground(Color.GREEN);

        panelInfo.add(lblID);
        panelInfo.add(lblStatus);
        panelInfo.add(lblCoord);
        panelInfo.add(barBaterai);

        // B. Bagian Tombol (Action)
        JPanel panelTombol = new JPanel();
        panelTombol.setBackground(Color.DARK_GRAY);

        btnStart = new JButton("ARM & START");
        btnStart.setBackground(Color.GREEN);
        btnStart.addActionListener(e -> aksiStart()); // Panggil fungsi aksiStart

        btnStop = new JButton("EMERGENCY STOP");
        btnStop.setBackground(Color.RED);
        btnStop.setForeground(Color.WHITE);

        panelTombol.add(btnStart);
        panelTombol.add(btnStop);

        // C. Bagian Log (Console Output)
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(Color.GREEN); // Tulisan ala Matrix
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollLog = new JScrollPane(logArea);
        scrollLog.setPreferredSize(new Dimension(250, 150));
        scrollLog.setBorder(BorderFactory.createTitledBorder(null, "SYSTEM LOG", 0, 0, null, Color.WHITE));

        // Gabungin Panel Kanan
        panelKanan.add(panelInfo);
        panelKanan.add(Box.createVerticalStrut(20)); // Spasi
        panelKanan.add(panelTombol);
        panelKanan.add(Box.createVerticalStrut(20)); // Spasi
        panelKanan.add(scrollLog);

        add(panelKanan, BorderLayout.EAST);

        setVisible(true);
        tulisLog("System Initialized...");
        tulisLog("Waiting for pilot command...");
    }

    // Helper biar gak capek ngetik setting label
    private JLabel buatLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial", Font.BOLD, 14));
        return l;
    }

    // Fungsi Logika Tombol Start
    private void aksiStart() {
        selectedDrone.setStat("ARMING");
        lblStatus.setText("STATUS: ARMING");
        lblStatus.setForeground(Color.GREEN);

        tulisLog("Command: ARMING received.");
        tulisLog("Motor spun up. Ready to fly.");

        // Jalankan GUI Kamera OpenCV di Thread baru biar GUI utama gak macet
        new Thread(() -> {
            tulisLog("Activating Sensor...");
            try {
                droneSensor.TampilkanGUI(); // Panggil method kamera kamu
            } catch (FrameGrabber.Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    // Fungsi nulis log
    public void tulisLog(String msg) {
        logArea.append("> " + msg + "\n");
        // Auto scroll ke bawah
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}