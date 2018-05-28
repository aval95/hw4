import database.Database;
import java.util.ArrayList;
import struct.Hire;
import struct.Offer;

/**
 * Class representing the application
 * @author myBike Team
 */
public class Application {
    public static void main(String args[]) {        
        //Connection to the database
        Database.connect();
        
        // Query 2
        System.out.println("\n-- List of hires of a customer");
        ArrayList<Hire> hireList = Database.getHiresList("19f8c9c8-14b2-43d9-b6a5-1d07268e840e");
        for(Hire hire:hireList) {
            System.out.println(hire);
        }//for
        
        //Query 3
        System.out.println("\n-- Rewards of a customer");
        ArrayList<Offer> offerList = Database.getOffersWithPoints("6697421068", 2);
        for(Offer offer:offerList) {
            System.out.println(offer);
        }//for
        
        // Query 4
        System.out.println("\n-- Most used docking stations");
        System.out.print(Database.getDockingStationsUsage());
        
        // Query 5
        System.out.println("\n-- Enabled card of each customer");
        System.out.print(Database.getEnabledCustomerCards());
        
        // Disconnection from the database and release of resources
        Database.close();
    }//main
}//Application
