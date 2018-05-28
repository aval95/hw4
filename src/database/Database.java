package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import struct.DockingStationUsage;
import struct.Hire;
import struct.Offer;

/**
 * Class representing the database, it provides all methods for some of the principal queries
 * @author myBike Team
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
    
    static {
        try{
            Class.forName(DRIVER);
            System.out.printf("Driver %s successfully registered\n", DRIVER);
        } catch(ClassNotFoundException e) {
            System.err.printf("Error while loading DB driver %s\n %s", DRIVER, e.getMessage());
        }//try catch
    }//static
    
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
    
    
    public static ArrayList<Offer> getOffersWithPoints(String card_id, int organization) {
        if(card_id == null || organization <= 0)
            return null;
        if(conn == null) {
            System.err.println("Connection to database not yet established. Connect to database before doing queries");
            return null;
        }//if
        ResultSet rs = null;
        ArrayList<Offer> list = new ArrayList<>();
        String sql = "SELECT * FROM mbt.Offer AS O WHERE O.reward_cost <= (SELECT C.current_points FROM mbt.Card AS C WHERE C.card_id = ? AND C.organization = ?);";
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
            
            pstmtOfferList.setString(1, card_id);
            pstmtOfferList.setInt(2, organization);
            
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
    
    public static String getDockingStationsUsage() {
        if(conn == null) {
            System.err.println("Connection to database not yet established. Connect to database before doing queries");
            return null;
        }//if
        ResultSet rs = null;
        String usage = "";
        String sql = "SELECT DS.docking_station_id, DS.station_name, COUNT(*) AS number_of_unlocks FROM mbt.Hire AS H LEFT JOIN mbt.DockingPoint AS DP ON H.Docking_point_unlock = DP.docking_point_id LEFT JOIN mbt.DockingStation AS DS ON DP.docking_station = DS.docking_station_id GROUP BY DS.docking_station_id  ORDER BY number_of_unlocks DESC;";
        Statement stmt = null;
        
        int docking_station_id;
        String station_name;
        int number_of_unlocks;
        
        try {
            start = System.currentTimeMillis();
            stmt = conn.createStatement();
            end = System.currentTimeMillis();
            System.out.printf("Statement successfully created in %,d milliseconds\n", end-start);
            
            start = System.currentTimeMillis();
            rs = stmt.executeQuery(sql);
            end = System.currentTimeMillis();
            System.out.printf("Query successfully executed in %,d milliseconds\n", end-start);
            
            while(rs.next()) {
                docking_station_id = rs.getInt("docking_station_id");
                station_name = rs.getString("station_name");
                number_of_unlocks = rs.getInt("number_of_unlocks");
                DockingStationUsage dsu = new DockingStationUsage(docking_station_id, station_name, number_of_unlocks);
                usage += dsu + "\n";
            }//while
        } catch(SQLException e) {
            System.err.printf("Error executing query %s\n", stmt);
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
                
                if(stmt != null) {
                    start = System.currentTimeMillis();
                    stmt.close();
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
                stmt = null;
                
                System.out.println("Resources released to the garbage collector");
            }//try catch finally
        }//try catch finally
        return usage;
    }//getDockingStationsUsage
    
    public static String getEnabledCustomerCards() {
        if(conn == null) {
            System.err.println("Connection to database not yet established. Connect to database before doing queries");
            return null;
        }//if
        ResultSet rs = null;
        String usage = "";
        String sql = "SELECT C.customer_id, C.name, C.surname, CD.card_id, CD.organization FROM mbt.Customer AS C INNER JOIN mbt.Card AS CD ON C.customer_id = CD.customer WHERE CD.enabled = true;";
        Statement stmt = null;
        
        String customer_id;
        String customer_name;
        String customer_surname;
        String card_id;
        int organization;
        
        try {
            start = System.currentTimeMillis();
            stmt = conn.createStatement();
            end = System.currentTimeMillis();
            System.out.printf("Statement successfully created in %,d milliseconds\n", end-start);
            
            start = System.currentTimeMillis();
            rs = stmt.executeQuery(sql);
            end = System.currentTimeMillis();
            System.out.printf("Query successfully executed in %,d milliseconds\n", end-start);
            
            while(rs.next()) {
                customer_id = rs.getString("customer_id");
                customer_name = rs.getString("name");
                customer_surname = rs.getString("surname");
                card_id = rs.getString("card_id");
                organization = rs.getInt("organization");
                usage += "EnabledCustomerCard{" + "customer_id=" + customer_id + ", name=" + customer_name + ", surname=" + customer_surname + ", card_id=" + card_id + ", organization=" + organization + "}\n";
            }//while
        } catch(SQLException e) {
            System.err.printf("Error executing query %s\n", stmt);
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
                
                if(stmt != null) {
                    start = System.currentTimeMillis();
                    stmt.close();
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
                stmt = null;
                
                System.out.println("Resources released to the garbage collector");
            }//try catch finally
        }//try catch finally
        return usage;
    }//getEnabledCustomerCards
    
    
}
