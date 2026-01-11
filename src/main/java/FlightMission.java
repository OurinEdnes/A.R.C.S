public class FlightMission {
    private String missionName;
    private String objective;
    private UAV assignedUAV;   // Association, hanya reference

    public FlightMission(String missionName, String objective, UAV assignedUAV) {
        this.missionName = missionName;
        this.objective = objective;
        this.assignedUAV = assignedUAV; // UAV terhubung tapi tidak dimiliki
    }

    public void startMission() {
        System.out.println("\n=== Mission Activated ===");
        System.out.println("Mission : " + missionName);
        System.out.println("Objective : " + objective);
        System.out.println("Assigned UAV : " + assignedUAV.getID());
        System.out.println("Status UAV : " + assignedUAV.getStat());
        System.out.println("=========================");
    }

    // Getter opsional
    public String getMissionName() { return missionName; }
    public String getObjective() { return objective; }
    public UAV getAssignedUAV() { return assignedUAV; }
}
