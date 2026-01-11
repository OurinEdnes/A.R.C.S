public class UAV {
    protected String ID;
    protected float batt;
    protected String Stat;
    protected String Cordinat;
    protected float x;
    protected float y;
    protected float z;
    private UavComp sensor; // Composite

    public UAV(String cordinat, String stat, float batt, String ID, UavComp sensor) {
        this.Cordinat = cordinat;
        this.Stat = stat;
        this.batt = batt;
        this.ID = ID;
        this.sensor = sensor;

        String[] part = cordinat.split(","); // pisah koordinat by koma

        this.x = Float.parseFloat(part[0]);
        this.y = Float.parseFloat(part[1]);
        this.z = Float.parseFloat(part[2]);
    }

    // ==== Setter ====
    public void setBatt(float batt) {
        this.batt = batt;
    }

    public void setStat(String stat) {
        Stat = stat;
    }

    public void setCordinat(String cordinat) {
        Cordinat = cordinat;

        String[] part = cordinat.split(","); // pisah koordinat by koma

        this.x = Float.parseFloat(part[0]);
        this.y = Float.parseFloat(part[1]);
        this.z = Float.parseFloat(part[2]);
    }


    //  ==== Getter ====
    public String getID() {
        return ID;
    }

    public float getBatt() {
        return batt;
    }

    public String getStat() {
        return Stat;
    }

    public String getCordinat() {
        return Cordinat;
    }

    public void getSensor() {
        sensor.infoSensor();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void Cetak(){
        System.out.println("ID       : " + ID);
        System.out.println("Battery  : " + batt + "%");
        System.out.println("Status   : " + Stat);
        System.out.println("------------------------------------");
    }
}
