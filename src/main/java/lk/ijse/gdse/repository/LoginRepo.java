package lk.ijse.gdse.repository;

import lk.ijse.gdse.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginRepo {
    public static String getLastEnteredUsername() throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        String lastUsername = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "SELECT userId FROM users ORDER BY userId DESC LIMIT 1";
            pstm = connection.prepareStatement(sql);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                lastUsername = resultSet.getString("userId");
            }
        } catch (SQLException e) {
            // Handle or log the exception appropriately
            e.printStackTrace();
        }

        return lastUsername;
    }
}
