import java.util.ArrayList;

public class GCS {
    private String stationID;
    private String location;
    private ArrayList<UAV> drones;   // AGGREGATION: GCS tidak memiliki UAV sepenuhnya

    public GCS(String stationID, String location) {
        this.stationID = stationID;
        this.location = location;
        this.drones = new ArrayList<>();
    }

    public void addDrone(UAV drone) {
        drones.add(drone);
        System.out.println("UAV " + drone.getID() + " connected to GCS.");
    }

    public void monitorAll() {
        System.out.println("\n=== Ground Control Monitoring (" + stationID + ") ===");
        System.out.println("Location: " + location);
        System.out.println("Total Connected UAV: " + drones.size());
        System.out.println("------------------------------------");

        for (UAV d : drones) {
            System.out.println("ID       : " + d.getID());
            System.out.println("Battery  : " + d.getBatt() + "%");
            System.out.println("Status   : " + d.getStat());
            System.out.println("Position : " + d.getCordinat());
            System.out.println("------------------------------------");
        }
    }
}
