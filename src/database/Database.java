package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import struct.Hire;
import struct.Offer;

/**
 *
 * @author Alex
 */
public class Database {
    private static Connection conn = null;
    
    private static long start;
    private static long end;
    
    // JDBC driver to be used
    private static final String DRIVER = "org.postgresql.Driver";
    
    private static final String DATABASE = "jdbc:postgresql://localhost:5433/dbms1718";
    private static final String USER = "webdb";
    private static final String PASSWORD = "webdb";
    
    /**
     * Constructor of the class representing the Database
     */
    public Database() {
        try{
            Class.forName(DRIVER);
            System.out.printf("Driver %s successfully registered\n", DRIVER);
        } catch(ClassNotFoundException e) {
            System.err.printf("Error while loading DB driver %s\n %s", DRIVER, e.getMessage());
        }//try catch
    }//Database
    
    /**
     * Connect to the PostgreSQL database
     * @return a Connection object
     */
    public static Connection connect() {
        try {
            start = System.currentTimeMillis();
            conn = DriverManager.getConnection(DATABASE, USER, PASSWORD);
            end = System.currentTimeMillis();
            System.out.printf("Connection to database %s successfully established in %,d milliseconds\n", DATABASE, end-start);
        } catch (SQLException e) {
            System.err.println("Error connecting to database " + DATABASE);
            while(e != null) {
                System.out.printf("- Message: %s\n", e.getMessage());
                System.out.printf("- SQL status code: %s\n", e.getSQLState());
                System.out.printf("- SQL error code: %s\n", e.getErrorCode());
                System.out.println();
                e = e.getNextException();
            }//while
        }//try catch
        return conn;
    }//connect
    
    public static boolean close() {
        boolean result = false;
        try {
            if(conn != null) {
                start = System.currentTimeMillis();
                conn.close();
                end = System.currentTimeMillis();
                System.out.printf("Connection successfully closed in %,d milliseconds\n", end-start);
                result = true;
            }//if conn
        } catch (SQLException e) {
            System.err.println("Error while releasing resources");
            while(e != null) {
                System.out.printf("- Message: %s\n", e.getMessage());
                System.out.printf("- SQL status code: %s\n", e.getSQLState());
                System.out.printf("- SQL error code: %s\n", e.getErrorCode());
                System.out.println();
                e = e.getNextException();
            }//while
        } finally {
            conn = null;
        }//try catch finally
        return result;
    }//close
    
    public static ArrayList<Hire> getHiresList(String customer) {
        if(customer == null)
            return null;
        if(conn == null) {
            System.err.println("Connection to database not yet established. Connect to database before doing queries");
            return null;
        }//if
        ResultSet rs = null;
        ArrayList<Hire> list = new ArrayList<>();
        String sql = "SELECT * FROM mbt.Hire AS H WHERE (card, organization) IN (SELECT CC.card_id, CC.organization FROM (SELECT C.card_id, C.organization FROM mbt.Card AS C WHERE C.customer = ?::uuid) AS CC);";
        PreparedStatement pstmtHireList = null;
        
        int hire_id;
        float km_ride;
        String card;
        int organization;
        int bike;
        int docking_point_unlock;
        String date_unlock;
        int docking_point_lock;
        String date_lock;
        
        try {
            start = System.currentTimeMillis();
            pstmtHireList = conn.prepareStatement(sql);
            end = System.currentTimeMillis();
            System.out.printf("Statement successfully created in %,d milliseconds\n", end-start);
            
            pstmtHireList.setString(1, customer);
            
            start = System.currentTimeMillis();
            rs = pstmtHireList.executeQuery();
            end = System.currentTimeMillis();
            System.out.printf("Query successfully executed in %,d milliseconds\n", end-start);
            
            while(rs.next()) {
                hire_id = rs.getInt("hire_id");
                km_ride = rs.getFloat("KM_ride");
                card = rs.getString("card");
                organization = rs.getInt("organization");
                bike = rs.getInt("bike");
                docking_point_unlock = rs.getInt("docking_point_unlock");
                date_unlock = rs.getString("date_unlock");
                docking_point_lock = rs.getInt("docking_point_lock");
                date_lock = rs.getString("date_lock");
                list.add(new Hire(hire_id, km_ride, card, organization, bike, docking_point_unlock, date_unlock, docking_point_lock, date_lock));
            }//while
        } catch(SQLException e) {
            System.err.printf("Error executing query %s\n", pstmtHireList);
            while(e != null) {
                System.out.printf("- Message: %s\n", e.getMessage());
                System.out.printf("- SQL status code: %s\n", e.getSQLState());
                System.out.printf("- SQL error code: %s\n", e.getErrorCode());
                System.out.println();
                e = e.getNextException();
            }//while
        } finally {
            try {
                if(rs != null) {
                    start = System.currentTimeMillis();
                    rs.close();
                    end = System.currentTimeMillis();
                    System.out.printf("Result set successfully closed in %,d milliseconds\n", end-start);
                }//if rs
                
                if(pstmtHireList != null) {
                    start = System.currentTimeMillis();
                    pstmtHireList.close();
                    end = System.currentTimeMillis();
                    System.out.printf("Statement successfully closed in %,d milliseconds\n", end-start);
                }//if pstmt
                
                System.out.println("Resources successfully released");
            } catch (SQLException e) {
                System.err.println("Error while releasing resources");
                while(e != null) {
                    System.out.printf("- Message: %s\n", e.getMessage());
                    System.out.printf("- SQL status code: %s\n", e.getSQLState());
                    System.out.printf("- SQL error code: %s\n", e.getErrorCode());
                    System.out.println();
                    e = e.getNextException();
                }//while
            } finally {
                rs = null;
                pstmtHireList = null;
                
                System.out.println("Resources released to the garbage collector");
            }//try catch finally
        }//try catch finally
        return list;
    }//getHireList
    
    
    public static ArrayList<Offer> getOffersWithPoints(int points) {
        if(points < 0)
            return null;
        if(conn == null) {
            System.err.println("Connection to database not yet established. Connect to database before doing queries");
            return null;
        }//if
        ResultSet rs = null;
        ArrayList<Offer> list = new ArrayList<>();
        String sql = "SELECT * FROM Offer AS O WHERE O.reward_cost <= (SELECT C.current_points FROM Card AS C WHERE C.card_id = ?);";
        PreparedStatement pstmtOfferList = null;
        
        int offer_id;
        int reward_cost;
        String description;
        String shop;
        
        try {
            start = System.currentTimeMillis();
            pstmtOfferList = conn.prepareStatement(sql);
            end = System.currentTimeMillis();
            System.out.printf("Statement successfully created in %,d milliseconds\n", end-start);
            
            pstmtOfferList.setInt(1, points);
            
            start = System.currentTimeMillis();
            rs = pstmtOfferList.executeQuery();
            end = System.currentTimeMillis();
            System.out.printf("Query successfully executed in %,d milliseconds\n", end-start);
            
            while(rs.next()) {
                offer_id = rs.getInt("offer_id");
                reward_cost = rs.getInt("reward_cost");
                description = rs.getString("description");
                shop = rs.getString("shop");
                list.add(new Offer(offer_id, reward_cost, description, shop));
            }//while
        } catch(SQLException e) {
            System.err.printf("Error executing query %s\n", pstmtOfferList);
            while(e != null) {
                System.out.printf("- Message: %s\n", e.getMessage());
                System.out.printf("- SQL status code: %s\n", e.getSQLState());
                System.out.printf("- SQL error code: %s\n", e.getErrorCode());
                System.out.println();
                e = e.getNextException();
            }//while
        } finally {
            try {
                if(rs != null) {
                    start = System.currentTimeMillis();
                    rs.close();
                    end = System.currentTimeMillis();
                    System.out.printf("Result set successfully closed in %,d milliseconds\n", end-start);
                }//if rs
                
                if(pstmtOfferList != null) {
                    start = System.currentTimeMillis();
                    pstmtOfferList.close();
                    end = System.currentTimeMillis();
                    System.out.printf("Statement successfully closed in %,d milliseconds\n", end-start);
                }//if pstmt
                
                System.out.println("Resources successfully released");
            } catch (SQLException e) {
                System.err.println("Error while releasing resources");
                while(e != null) {
                    System.out.printf("- Message: %s\n", e.getMessage());
                    System.out.printf("- SQL status code: %s\n", e.getSQLState());
                    System.out.printf("- SQL error code: %s\n", e.getErrorCode());
                    System.out.println();
                    e = e.getNextException();
                }//while
            } finally {
                rs = null;
                pstmtOfferList = null;
                
                System.out.println("Resources released to the garbage collector");
            }//try catch finally
        }//try catch finally
        return list;
    }//getOffersWithPoints
    
    
}
