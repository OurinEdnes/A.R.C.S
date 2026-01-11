public class Type_UAV extends UAV {
    protected String type;
    protected String fungsi;
    String StatI;

    public Type_UAV(String coordinate, String stat, float batt, String ID, int a, UavComp sensor) {
        super(coordinate, stat, batt, ID, sensor);

        if (a == 1) {
            this.type = "DeliveryDrone";
            this.fungsi = "Mengirim barang";
        } else if (a == 2) {
            this.type = "SurveyDrone";
            this.fungsi = "Pemantauan dan pemetaan wilayah";
        } else if (a == 3) {
            this.type = "RescueDrone";
            this.fungsi = "Operasi penyelamatan darurat";
        } else {
            this.type = "UnknownDrone";
            this.fungsi = "Tidak diketahui";
        }
    }

    public String getFungsi() {
        return fungsi;
    }

    public String getType() {
        return type;
    }

    @Override
    public void setStat(String stat) {
        super.setStat(stat);
    }

    @Override
    public void Cetak() {
        super.Cetak();
    }
}
