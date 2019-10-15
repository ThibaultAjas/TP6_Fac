package testingwithhsqldb;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;
import org.junit.Ignore;

public class HsqlDBTest {

    private static DataSource myDataSource;
    private static Connection myConnection;

    private DAO myObject;

    @Before
    public void setUp() throws IOException, SqlToolError, SQLException {
        // On crée la connection vers la base de test "in memory"
        myDataSource = getDataSource();
        myConnection = myDataSource.getConnection();
        // On crée le schema de la base de test
        executeSQLScript(myConnection, "schema.sql");
        // On y met des données
        executeSQLScript(myConnection, "bigtestdata.sql");

        myObject = new DAO(myDataSource);
//        myObject.afficheProductTable();
    }

    private void executeSQLScript(Connection connexion, String filename) throws IOException, SqlToolError, SQLException {
        // On initialise la base avec le contenu d'un fichier de test
        String sqlFilePath = HsqlDBTest.class.getResource(filename).getFile();
        SqlFile sqlFile = new SqlFile(new File(sqlFilePath));

        sqlFile.setConnection(connexion);
        sqlFile.execute();
        sqlFile.closeReader();
    }

    @After
    public void tearDown() throws IOException, SqlToolError, SQLException {
        myConnection.close(); // La base de données de test est détruite ici
        myObject = null; // Pas vraiment utile
    }

    @Test
    public void findExistingCustomer() throws SQLException {
        String name = myObject.nameOfCustomer(0);
        assertNotNull("Customer exists, name should not be null", name);
        assertEquals("Bad name found !", "Steel", name);
    }

    @Test
    public void nonExistingCustomerReturnsNull() throws SQLException {
        String name = myObject.nameOfCustomer(-1);
        assertNull("name should be null, customer does not exist !", name);
    }

    @Test
    public void testInsertingAndGettingValidProduct() throws SQLException {
        int res = myObject.insertProduct(666, "Teemo's shroom", 10);
        assertEquals(1, res);
    }
    
    @Test
    public void test1() throws SQLException {
        Product p = myObject.getProduct(32);
        System.out.println(p);
    }
    
    @Test (expected = AssertionError.class)
    public void testInsertingExistingProduct() {
        Product p1 = new Product(666, "Teemo's shroom", 10);
        myObject.insertProduct(p1.getId(), p1.getName(), p1.getPrice());
        Product p2 = new Product(666, "Teemo's dart", 50);
        myObject.insertProduct(p2.getId(), p2.getName(), p2.getPrice());
    }
    
    @Test (expected = AssertionError.class)
    public void testInsertingProductWithNegativePrice() {
        Product p = new Product(666, "Teemo's shroom", -10);
        myObject.insertProduct(p.getId(), p.getName(), p.getPrice());
    }

    public static DataSource getDataSource() {
        org.hsqldb.jdbc.JDBCDataSource ds = new org.hsqldb.jdbc.JDBCDataSource();
        ds.setDatabase("jdbc:hsqldb:mem:testcase;shutdown=true");
        ds.setUser("sa");
        ds.setPassword("sa");
        return ds;
    }
}
