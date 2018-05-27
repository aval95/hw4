import database.Database;
import java.util.ArrayList;
import struct.Hire;

/**
 *
 * @author Alex
 */
public class Application {
    public static void main(String args[]) {
        Database db = new Database();
        
        db.connect();
        ArrayList<Hire> list = Database.getHiresList("19f8c9c8-14b2-43d9-b6a5-1d07268e840e");
        
        for(Hire hire:list) {
            System.out.println(hire);
        }//for
    }
}
