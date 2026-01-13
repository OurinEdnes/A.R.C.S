import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        ArrayList<UavComp> Krit = new ArrayList<>();
        ArrayList<Type_UAV> UAVStat = new ArrayList<>();
        ArrayList<Pilot> PilotEi = new ArrayList<>();

        Krit.add(new UavComp("4K Cam", true, true, 1));
        Krit.add(new UavComp("4K Cam", true,  false, 3));


        // === INHERITANCE: ===
        UAVStat.add(new Type_UAV("45.22, 0, 111.55", "DISARMING", 100f, "DR-01", 3, Krit.get(0)));
        UAVStat.add(new Type_UAV("46.72, 0, 113.11", "DISARMING", 100f, "DR-02", 2, Krit.get(1)));


        // === ASSOCIATION:  ===
        PilotEi.add(new Pilot("Handoyo", "LNC-92120"));
        PilotEi.get(0).assignUAV(UAVStat.get(0));  // connect UAV to pilot

        // === AGGREGATION:  ===
        GCS gcs = new GCS("GCS-A01", "Base Station Alpha");
        gcs.addDrone(UAVStat.get(0));
        gcs.addDrone(UAVStat.get(1));

        // === ASSOCIATION:  ===
        FlightMission task1 = new FlightMission(
                "Searching Korban",
                "Locating korban bencana",
                UAVStat.get(0)
        );

        // === RUN ===
        PilotEi.get(0).controlUAV();
        task1.startMission();
        gcs.monitorAll();

        System.out.println("Launching GCS Dashboard...");

// Kita pilih drone pertama (index 0) buat dikontrol lewat GUI
// Bisa juga bikin menu drop-down nanti buat milih drone
        DashboardGUI dashboard = new DashboardGUI(UAVStat.get(0), Krit.get(0));

        System.out.println("================================");
        System.out.println("Put ID for Start!");
        System.out.print("Start Drone: ");
        String ID_UAV = input.nextLine();


        boolean AdaID = false;
        int Req = 0, Pull = -1;
        for(Type_UAV i : UAVStat){
            if(i.getID().equals(ID_UAV)){
                Pull = Req;
                UAVStat.get(Pull).setStat("Arming");

                AdaID = true;
                break;
            }
            Req++;
        }

        if(AdaID){
            UAVStat.get(Pull).Cetak();
            Krit.get(Pull).TampilkanGUI();
        } else{
            System.out.println("Id UAV tidak diketehui!D");
        }
    }
}
