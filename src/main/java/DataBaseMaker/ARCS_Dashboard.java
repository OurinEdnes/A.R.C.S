package DataBaseMaker;

import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

// 🦈 Class GUI Aiko Special Edition - Dashboard Commander
public class ARCS_Dashboard extends JFrame {

    // Kita panggil asisten database kita yang pintar!
    private DatabasePortable dbms;

    // Komponen UI
    private JTabbedPane tabbedPane;
    private JTextArea logArea;

    public ARCS_Dashboard() {
        // 1. Setup Jendela Utama (The Frame)
        setTitle("A.R.C.S - Data Control Center");
        setSize(800, 600); // Aiko gedein dikit biar lega
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Biar muncul di tengah layar
        setLayout(new BorderLayout());

        // Header Keren a la Aiko
        JLabel titleLabel = new JLabel("A.R.C.S. Database Commander", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204)); // Warna Biru Laut
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(titleLabel, BorderLayout.NORTH);

        // 2. Area Log (Disiapin duluan biar bisa nangkep log inisialisasi)
        logArea = new JTextArea(8, 20);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setBackground(new Color(30, 30, 30)); // Dark mode log
        logArea.setForeground(new Color(100, 255, 100)); // Hacker green text
        JScrollPane scrollLog = new JScrollPane(logArea);
        scrollLog.setBorder(BorderFactory.createTitledBorder("🦈 System Log (Console Output)"));
        add(scrollLog, BorderLayout.SOUTH);

        // 3. JURUS RAHASIA: Redirect System.out ke JTextArea
        // Jadi apapun yang di-print sama DatabasePortable bakal muncul disini!
        redirectSystemStreams();

        // 4. Inisialisasi Database (Panggil Class Sebelah)
        System.out.println("--- Mengubungi Markas A.R.C.S... ---");
        try {
            this.dbms = new DatabasePortable();
            this.dbms.createNewTables(); // Bikin tabel kalau belum ada
        } catch (Exception e) {
            System.out.println("ERROR FATAL: " + e.getMessage());
        }

        // 5. Tabbed Pane (5 Tab Sesuai Tabel)
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // --- TAB 1: PILOT ---
        tabbedPane.addTab("Pilot", createPilotPanel());

        // --- TAB 2: TIPE UAV ---
        tabbedPane.addTab("Tipe UAV", createUAVTypePanel());

        // --- TAB 3: KOMPONEN ---
        tabbedPane.addTab("Komponen", createComponentPanel());

        // --- TAB 4: GCS ---
        tabbedPane.addTab("GCS", createGCSPanel());

        // --- TAB 5: MISSION ---
        tabbedPane.addTab("Misi", createMissionPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ==================================================================================
    // 🏭 PANEL BUILDERS (Pabrik Formulir)
    // ==================================================================================

    // 1. PANEL PILOT
    private JPanel createPilotPanel() {
        JPanel panel = createBasePanel();

        JTextField txtId = addInput(panel, "ID Pilot (ex: PLT-001):");
        JTextField txtNama = addInput(panel, "Nama Pilot:");
        JTextField txtLicense = addInput(panel, "License ID:");

        JButton btnSave = new JButton("Simpan Data Pilot");
        styleButton(btnSave);
        panel.add(new JLabel(""));
        panel.add(btnSave);

        btnSave.addActionListener(e -> {
            // Panggil method dari class DatabasePortable
            dbms.insertPilot(txtId.getText(), txtNama.getText(), txtLicense.getText());
            clearFields(txtId, txtNama, txtLicense);
        });

        return panel;
    }

    // 2. PANEL TIPE UAV
    private JPanel createUAVTypePanel() {
        JPanel panel = createBasePanel();

        JTextField txtId = addInput(panel, "ID Tipe (ex: COMP-01):");
        JTextField txtNama = addInput(panel, "Nama Tipe UAV:");

        JButton btnSave = new JButton("Simpan Tipe UAV");
        styleButton(btnSave);
        panel.add(new JLabel(""));
        panel.add(btnSave);

        btnSave.addActionListener(e -> {
            dbms.insertUAVType(txtId.getText(), txtNama.getText());
            clearFields(txtId, txtNama);
        });

        return panel;
    }

    // 3. PANEL KOMPONEN
    private JPanel createComponentPanel() {
        JPanel panel = createBasePanel();

        JTextField txtId = addInput(panel, "ID Komponen (ex: COMP-01):");
        JTextField txtCam = addInput(panel, "Kamera Utama:");
        JTextField txtLidar = addInput(panel, "Lidar Sensor:");
        JTextField txtThermal = addInput(panel, "Thermal Cam:");
        JTextField txtCamAdd = addInput(panel, "Video Stream URL:");

        JButton btnSave = new JButton("Pasang Komponen");
        styleButton(btnSave);
        panel.add(new JLabel(""));
        panel.add(btnSave);

        btnSave.addActionListener(e -> {
            dbms.insertComponent(txtId.getText(), txtCam.getText(), txtLidar.getText(), txtThermal.getText(), txtCamAdd.getText());
            clearFields(txtId, txtCam, txtLidar, txtThermal, txtCamAdd);
        });

        return panel;
    }

    // 4. PANEL GCS
    private JPanel createGCSPanel() {
        JPanel panel = createBasePanel();

        JTextField txtId = addInput(panel, "ID GCS (ex: GCS-01):");
        JTextField txtLoc = addInput(panel, "Lokasi Markas:");

        JButton btnSave = new JButton("Set Lokasi GCS");
        styleButton(btnSave);
        panel.add(new JLabel(""));
        panel.add(btnSave);

        btnSave.addActionListener(e -> {
            dbms.insertGCS(txtId.getText(), txtLoc.getText());
            clearFields(txtId, txtLoc);
        });

        return panel;
    }

    // 5. PANEL MISI
    private JPanel createMissionPanel() {
        JPanel panel = createBasePanel();

        JTextField txtId = addInput(panel, "ID Misi (Angka):");
        JTextField txtMission = addInput(panel, "Nama Misi:");
        JTextField txtObj = addInput(panel, "Tujuan (Objective):");

        JButton btnSave = new JButton("Deploy Misi");
        styleButton(btnSave);
        btnSave.setBackground(new Color(196, 82, 82)); // Merah buat tombol bahaya/penting
        panel.add(new JLabel(""));
        panel.add(btnSave);

        btnSave.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                dbms.insertMission(id, txtMission.getText(), txtObj.getText());
                clearFields(txtId, txtMission, txtObj);
            } catch (NumberFormatException ex) {
                System.out.println("ERROR: ID Misi harus angka woy!");
            }
        });

        return panel;
    }

    // ==================================================================================
    // 🛠️ UTILITIES & HELPER (Biar codingan ga berantakan)
    // ==================================================================================

    private JPanel createBasePanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10)); // Grid flexible
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    private JTextField addInput(JPanel panel, String labelText) {
        panel.add(new JLabel(labelText));
        JTextField field = new JTextField();
        panel.add(field);
        return field;
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(0, 150, 255));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    // JURUS REDIRECTION LOG: Mengubah System.out jadi text di JTextArea
    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                updateLog(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) {
                updateLog(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    private void updateLog(String text) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(text);
            logArea.setCaretPosition(logArea.getDocument().getLength()); // Auto scroll ke bawah
        });
    }

    // --- MAIN EXECUTION ---
    public static void main(String[] args) {
        // Load Driver SQLite (Aiko pastikan ada)
        try { Class.forName("org.sqlite.JDBC"); } catch (Exception e) {}

        // Jalankan GUI
        SwingUtilities.invokeLater(() -> {
            new ARCS_Dashboard().setVisible(true);
        });
    }
}