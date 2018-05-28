/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package struct;

/**
 *
 * @author Alex
 */
public class DockingStationUsage {
    private int docking_station_id;
    private String station_name;
    private int number_of_unlocks;

    public DockingStationUsage(int station_id, String station_name, int number_of_unlocks) {
        this.docking_station_id = station_id;
        this.station_name = station_name;
        this.number_of_unlocks = number_of_unlocks;
    }

    public int getDockingStation_id() {
        return docking_station_id;
    }

    public String getStation_name() {
        return station_name;
    }

    public int getNumber_of_unlocks() {
        return number_of_unlocks;
    }

    public void setDockingStation_id(int station_id) {
        this.docking_station_id = station_id;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public void setNumber_of_unlocks(int number_of_unlocks) {
        this.number_of_unlocks = number_of_unlocks;
    }

    @Override
    public String toString() {
        return "DockingStationUsage{" + "docking_station_id=" + docking_station_id + ", station_name=" + station_name + ", number_of_unlocks=" + number_of_unlocks + '}';
    }
}
