public class Pilot {
    private String name;
    private String licenseID;
    private UAV controlledUAV;   // Association

    public Pilot(String name, String licenseID) {
        this.name = name;
        this.licenseID = licenseID;
    }

    // Connect Pilot to UAV
    public void assignUAV(UAV drone) {
        this.controlledUAV = drone;
        System.out.println("Pilot " + name + " assigned to UAV " + drone.getID());
    }

    public void controlUAV() {
        if (controlledUAV != null) {
            System.out.println("\n=== UAV Control Panel ===");
            System.out.println("Pilot : " + name);
            System.out.println("Operating UAV : " + controlledUAV.getID());
            System.out.println("Battery : " + controlledUAV.getBatt());
            System.out.println("Status : " + controlledUAV.getStat());
            System.out.println("=========================");
        } else {
            System.out.println("Pilot " + name + " has no UAV assigned!");
        }
    }

    // Getter
    public String getName() { return name; }
    public String getLicenseID() { return licenseID; }
}