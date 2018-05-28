import database.Database;
import java.util.ArrayList;
import struct.Hire;
import struct.Offer;

/**
 *
 * @author Alex
 */
public class Application {
    public static void main(String args[]) {
        Database db = new Database();
        
        Database.connect();
        
        // Query 2
        ArrayList<Hire> hireList = Database.getHiresList("19f8c9c8-14b2-43d9-b6a5-1d07268e840e");
        for(Hire hire:hireList) {
            System.out.println(hire);
        }//for
        
        //Query 3
        ArrayList<Offer> offerList = Database.getOffersWithPoints("19f8c9c8-14b2-43d9-b6a5-1d07268e840e");
        for(Offer offer:offerList) {
            System.out.println(offer);
        }//for
        
        // Query 4
        System.out.print(Database.getDockingStationsUsage());
        
        // Query 5
        System.out.print(Database.getEnabledCustomerCards());
        
        Database.close();
    }
}
