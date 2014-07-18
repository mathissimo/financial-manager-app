package dsh;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Connector {

    public final static String DBUSERNAME = ""; //User
    public final static String DBPASSWORT = ""; //Passwort 
    public final static String DBCONNECTION = "jdbc:postgresql://"; //Pfad zur Datenbank
    public final static String DBNAME = ""; //DB Name
    private static Connector uniqueInstance = null;
    private static final String treiber = "org.postgresql.Driver";

    public Connector() throws InstantiationException {
        try {
            try {
                Class.forName(treiber).newInstance();
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    public static Connector getInstance() {
        if (uniqueInstance == null) {
            try {
                uniqueInstance = new Connector();
            } catch (InstantiationException ex) {
                Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }
        return uniqueInstance;
    }

    public Connection connect() {
        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DBCONNECTION + "/" + DBNAME, DBUSERNAME, DBPASSWORT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;

    }
    
    public Connection close(){
        Connection con = null;
        String go = ("Verbindung wurde geschlossen");
        int i = 0;
        try{
            con.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(go);
        return con;
        
    }
}