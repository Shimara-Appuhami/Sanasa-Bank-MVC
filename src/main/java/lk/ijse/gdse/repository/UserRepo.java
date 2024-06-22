package lk.ijse.gdse.repository;

import javafx.scene.control.Alert;
import lk.ijse.gdse.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRepo {

    public static boolean updatePasswordByNIC(String nic, String newPassword) throws SQLException {
        boolean isUpdated = false;

        try
        {
            Connection connection = DbConnection.getInstance().getConnection();

             PreparedStatement pstm = connection.prepareStatement("UPDATE users SET password = ?");

            // Set parameters
            pstm.setString(1, newPassword);
            //pstm.setString(2, nic);

            // Execute the update statement
            isUpdated = pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            // Handle the SQL exception
            new Alert(Alert.AlertType.CONFIRMATION,e.getMessage()).show();// Log the exception or show an error message
        }

        return isUpdated;
    }
}
