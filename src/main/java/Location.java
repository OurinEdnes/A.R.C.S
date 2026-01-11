public class Location {
    protected float x;
    protected float y;
    protected float z;
    private UAV uav;
    protected float d1;

    public Location(float d1, String mid_cordinate) {
        this.d1 = d1;

        String[] part = mid_cordinate.split(",");

        this.x = Float.parseFloat(part[0]);
        this.y = Float.parseFloat(part[1]);
        this.z = Float.parseFloat(part[2]);
    }

    public float getD1() {
        return d1;
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
}
