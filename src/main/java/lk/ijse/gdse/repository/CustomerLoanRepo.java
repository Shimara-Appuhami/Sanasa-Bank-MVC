package lk.ijse.gdse.repository;

import javafx.scene.control.Alert;
import lk.ijse.gdse.db.DbConnection;
import lk.ijse.gdse.model.CustomerLoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CustomerLoanRepo {
    private Connection connection;

    public CustomerLoanRepo() throws SQLException {
        connection = DbConnection.getInstance().getConnection();
    }

    public boolean saveCustomerLoan(List<CustomerLoan> clList) throws SQLException {
        if (clList.isEmpty()) {
            return true;
        }

        System.out.println("Saving customer loan details...");
        String sql = "INSERT INTO customer_loan_details (loan_id, c_id) VALUES (?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            //           DbConnection.getInstance().getConnection().setAutoCommit(false);
            for (CustomerLoan cl : clList) {
                pst.setString(1, cl.getLoanId());
                pst.setString(2, cl.getCustomerId());
                pst.addBatch();
            }
            int[] batchResults = pst.executeBatch();
            for (int result : batchResults) {
                if (result <= 0) {
                    return false;
                }
            }
            System.out.println("CustomerLoan saved");
            return true;

        } catch (SQLException e) {
            System.out.println("Error saving customer loan details: " + e.getMessage());
            // DbConnection.getInstance().getConnection().rollback();
            return false;
        }
        //  finally {
        //   DbConnection.getInstance().getConnection().commit();
        //   DbConnection.getInstance().getConnection().setAutoCommit(true);
        // }
    }
}