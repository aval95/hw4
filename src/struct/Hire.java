package struct;

/**
 * Class representing a row of Hire table
 * @author myBike Team
 */
public class Hire {
    private int hire_id;
    private float km_ride;
    private String card;
    private int organization;
    private int bike;
    private int docking_point_unlock;
    private String date_unlock;
    private int docking_point_lock;
    private String date_lock;

    public Hire(int hire_id, float km_ride, String card, int organization, int bike, int docking_point_unlock, String date_unlock, int docking_point_lock, String date_lock) {
        this.hire_id = hire_id;
        this.km_ride = km_ride;
        this.card = card;
        this.organization = organization;
        this.bike = bike;
        this.docking_point_unlock = docking_point_unlock;
        this.date_unlock = date_unlock;
        this.docking_point_lock = docking_point_lock;
        this.date_lock = date_lock;
    }
    
    public Hire(int hire_id, String card, int organization, int bike, int docking_point_unlock, String date_unlock) {
        this(hire_id, 0, card, organization, bike, docking_point_unlock, date_unlock, 0, null);
    }

    public int getHire_id() {
        return hire_id;
    }

    public float getKm_ride() {
        return km_ride;
    }

    public String getCard() {
        return card;
    }

    public int getOrganization() {
        return organization;
    }

    public int getBike() {
        return bike;
    }

    public int getDocking_point_unlock() {
        return docking_point_unlock;
    }

    public String getDate_unlock() {
        return date_unlock;
    }

    public int getDocking_point_lock() {
        return docking_point_lock;
    }

    public String getDate_lock() {
        return date_lock;
    }


    public void setKm_ride(float km_ride) {
        this.km_ride = km_ride;
    }

    public void setDocking_point_lock(int docking_point_lock) {
        this.docking_point_lock = docking_point_lock;
    }

    public void setDate_lock(String date_lock) {
        this.date_lock = date_lock;
    }

    @Override
    public String toString() {
        return "Hire{" + "hire_id=" + hire_id + ", km_ride=" + km_ride + ", card=" + card + ", organization=" + organization + ", bike=" + bike + ", docking_point_unlock=" + docking_point_unlock + ", date_unlock=" + date_unlock + ", docking_point_lock=" + docking_point_lock + ", date_lock=" + date_lock + '}';
    }
    
}//Hire
