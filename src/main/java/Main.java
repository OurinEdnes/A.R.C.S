import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        DatabaseConfig databaseConfig = new DatabaseConfig();
        System.out.println(databaseConfig.toString());

        System.out.println("Launching GCS Dashboard...");

        DashboardGUI dashboard = new DashboardGUI(databaseConfig.typeUAV, databaseConfig.UavComp);



// =======================================================================================
//        String ID_UAV;
//        boolean AdaID = false;
//
//        while(true) {
//            System.out.println("================================");
//            System.out.println("Put ID for Start!");
//            System.out.print("Start Drone: ");
//            ID_UAV = input.nextLine();
//
//            int Req = 0, Pull = -1;
//
//            for(Type_UAV i : UAVStat){
//                if(i.getID().equals(ID_UAV)){
//                    Pull = Req;
//                    UAVStat.get(Pull).setStat("Arming");
//                    AdaID = true;
//                    break;
//                }
//                Req++;
//            }
//
//            if(AdaID){
//                System.out.println("Connecting to " + ID_UAV + "...");
//                UAVStat.get(Pull).Cetak();
//
//                Krit.get(Pull).TampilkanGUI();
//                break;
//            } else {
//                System.out.println("Id UAV tidak diketahui! Coba lagi dong... 🦈");
//            }
//        }

    }
}
