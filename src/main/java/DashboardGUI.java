import org.bytedeco.javacv.FrameGrabber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DashboardGUI extends JFrame {
    // Komponen GUI
    private JLabel lblStatus, lblBatt, lblCoord, lblID;
    private JProgressBar barBaterai;
    private JTextArea logArea;
    private JButton btnStart, btnStop;

    // Data Drone (Disimpan biar bisa diakses tombol)
    private Type_UAV selectedDrone;
    private UavComp droneSensor;

    public DashboardGUI(Type_UAV drone, UavComp sensor) {
        this.selectedDrone = drone;
        this.droneSensor = sensor;

        // 1. Setup Jendela Utama (Frame)
        setTitle("A.R.C.S. Ground Control Station");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Ganti tema jadi Gelap ala Hacker 😎
        getContentPane().setBackground(Color.DARK_GRAY);

        // === PANEL KIRI (Visualisasi Dummy / Tempat Kamera nanti) ===
        JPanel panelKiri = new JPanel();
        panelKiri.setBackground(Color.BLACK);
        panelKiri.setBorder(BorderFactory.createTitledBorder("Live Feed Placeholder"));
        panelKiri.setPreferredSize(new Dimension(500, 500));
        add(panelKiri, BorderLayout.CENTER);

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

        lblCoord = buatLabel("LOC: " + drone.getCordinat());

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