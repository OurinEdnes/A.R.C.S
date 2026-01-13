public class Type_UAV extends UAV {
    protected String type;
    protected String fungsi;
    String StatI;

    public Type_UAV(String coordinate, String stat, float batt, String ID, int a, UavComp sensor) {
        super(coordinate, stat, batt, ID, sensor);

        if (a == 1) {
            this.type = "Fixed Wing Long Range";
        } else if (a == 2) {
            this.type = "V-TOL";
        } else if (a == 3) {
            this.type = "Lela";
        } else {
            this.type = "UnknownDrone";
            this.fungsi = "Tidak diketahui";
            return;
        }
        this.fungsi = "Operasi penyelamatan darurat";
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
