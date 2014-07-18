/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dsh;
import java.sql.*;

/**
 *
 * @author maak
 */
public class ConnectionDB{
    private Connector connector;
    private Connection connection;
    private Statement statement;
    private boolean blockDB=false;
    
    public ConnectionDB () throws Exception{
        blockDB=false;
        initConnectionDB ();
    }
    
    public ConnectionDB (boolean blockDBwhileEditing) throws Exception{
        blockDB=blockDBwhileEditing;
        initConnectionDB ();
    }
    
    public void initConnectionDB () throws Exception {
        this.connector = new Connector();
        this.connection = connector.connect();
        this.connector = Connector.getInstance();
        System.out.println("connector:" + this.connector);
        System.out.println("connection:" + this.getConnection());
        // start transaction: turn off autoCommit
        if (blockDB) {
            this.getConnection().setAutoCommit(false);
        }
        this.statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        System.out.println("statement:" + this.statement);        
    }
    
    public void closeConnection () throws Exception {
        this.statement.close ();
        this.connection.close();
    }
    
    public void resetSerial (String table, String column) {
        try {
            this.statement.executeQuery("select setval (pg_get_serial_sequence('"+table+"','"+column+"'),(SELECT max("+column+") from "+table+"))");
        } catch (Exception e) {
            System.out.println("reset Serial: "+e);
            DshApp.getApplication().showErrorMessage("DB-Connection failed!!\n("+e+")");
        }        
    }
    
    public ResultSet getResultSet (String query) {
        ResultSet myResults = null;
        try {
            myResults = this.statement.executeQuery(query);
              System.out.println("myResults: "+myResults);
        } catch (Exception e) {
            System.out.println("getResultSet: "+e);
            DshApp.getApplication().showErrorMessage("DB-Connection failed!!\n("+e+")");
        }        
        return myResults;
    }
    
    public ResultSet updateQueryWithSpecificReturnValues (String query, String [] returningColumn) {
        try {
            this.statement.executeUpdate(query,returningColumn);
            return this.statement.getGeneratedKeys();
        } catch (Exception e) {
            System.out.println("ConnectionDB:updateQueryWithSpecificReturnValues: "+e);
            e.printStackTrace();
            DshApp.getApplication().showErrorMessage("DB-Connection failed!!\n("+e+")");
        }
        return null;
    }

    public int updateQueryWithIntReturnValue (String query) {
        try {
            return this.statement.executeUpdate(query);      
        } catch (Exception e) {
            System.out.println("ConnectionDB:updateQueryWithIntReturnValue: "+e);
            e.printStackTrace();
            DshApp.getApplication().showErrorMessage("DB-Connection failed!!\n("+e+")");
        }
        return -1;
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }
    
}
