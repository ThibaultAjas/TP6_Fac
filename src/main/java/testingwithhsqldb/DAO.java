package testingwithhsqldb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

public class DAO {

    private final DataSource myDataSource;

    public DAO(DataSource dataSource) {
        myDataSource = dataSource;
    }

    /**
     * Renvoie le nom d'un client à partir de son ID
     *
     * @param id la clé du client à chercher
     * @return le nom du client (LastName) ou null si pas trouvé
     * @throws SQLException
     */
    public String nameOfCustomer(int id) throws SQLException {
        String result = null;

        String sql = "SELECT LastName FROM Customer WHERE ID = ?";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement statement = myConnection.prepareStatement(sql)) {
            statement.setInt(1, id); // On fixe le 1° paramètre de la requête
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    // est-ce qu'il y a un résultat ? (pas besoin de "while", 
                    // il y a au plus un enregistrement)
                    // On récupère les champs de l'enregistrement courant
                    result = resultSet.getString("LastName");
            }
        }
        // dernière ligne : on renvoie le résultat
        return result;
    }

    public int insertProduct(int id, String name, float price) /* throws NegativePriceException */ {
//        if (price < 0) throw new NegativePriceException("Price must NOT be negative.");
        String sql = "INSERT INTO PRODUCT VALUES(?, ?, ?)";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement stmt = myConnection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setBigDecimal(3, new BigDecimal(price));
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Product getProduct(int id) throws SQLException {
        String sql = "SELECT NAME, PRICE FROM PRODUCT WHERE ID = ?";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement stmt = myConnection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return new Product(id, rs.getString("NAME"), rs.getInt("PRICE"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void afficheProductTable() {
        String sql = "SELECT ID, NAME, PRICE FROM PRODUCT";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement stmt = myConnection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                System.out.println(rs.getInt("ID") + " | " + rs.getString("NAME") + " | " + rs.getFloat("PRICE"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
