package lk.ijse.gdse.repository;


import javafx.scene.control.Alert;
import lk.ijse.gdse.db.DbConnection;
import lk.ijse.gdse.model.Loan;
import lk.ijse.gdse.model.tm.InterestRateTm;
import lk.ijse.gdse.model.tm.LoanTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanRepo {
    public static boolean save(Loan loan) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isSaved = false;

        System.out.println("Saving order : " + loan);
        String loanSql = "INSERT INTO loan (loan_id, c_id, application, loan_type, loan_amount, loan_term, collateral, purpose, percentage,nic,date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

        try {
            //DbConnection.getInstance().getConnection().setAutoCommit(false);

            PreparedStatement loanStatement = DbConnection.getInstance().getConnection().prepareStatement(loanSql);
            loanStatement.setString(1, loan.getLoanId());
            loanStatement.setString(2, loan.getCustomerId());
            loanStatement.setString(3, loan.getApplication());
            loanStatement.setString(4, loan.getLoanType());
            loanStatement.setDouble(5, Double.parseDouble(loan.getLoanAmount()));
            loanStatement.setString(6, loan.getLoanTerm());
            loanStatement.setString(7, loan.getCollateral());
            loanStatement.setString(8, loan.getPurpose());
            loanStatement.setDouble(9, Double.parseDouble(String.valueOf(loan.getPercentage())));
            loanStatement.setString(10,loan.getNic());
            loanStatement.setString(11,loan.getDate());

            int OrderRowsAffected = loanStatement.executeUpdate();

            boolean isLoanSaved = OrderRowsAffected > 0;
            if (!isLoanSaved) {
                //  DbConnection.getInstance().getConnection().rollback();
                return false;
            }
           // new Alert(Alert.AlertType.INFORMATION,"Loan save successfully").show();
            System.out.println("isLoanSaved = " + isLoanSaved);

            DbConnection.getInstance().getConnection().commit();
            return true;

        } catch (Exception e) {
           // new Alert(Alert.AlertType.INFORMATION,e.getMessage()).show();
            System.out.println("Transaction rolled back");
            //DbConnection.getInstance().getConnection().rollback();
            throw e;
            // }finally {
            //DbConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }


    public static boolean update(Loan loan) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isUpdated = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "UPDATE loan SET c_id=?, application=?, loan_amount=?, loan_type=?, loan_term=?, collateral=?, purpose=?, percentage=?, nic=?, date=? WHERE loan_id=?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, loan.getCustomerId());
            pstm.setString(2, loan.getApplication());
            pstm.setDouble(3, Double.parseDouble(loan.getLoanAmount()));
            pstm.setString(4, loan.getLoanType());
            pstm.setString(5, loan.getLoanTerm());
            pstm.setString(6, loan.getCollateral());
            pstm.setString(7, loan.getPurpose());
            pstm.setDouble(8, loan.getPercentage());
            pstm.setString(9, loan.getNic());
            pstm.setString(10, loan.getDate());
            pstm.setString(11, loan.getLoanId());

            isUpdated = pstm.executeUpdate() > 0;
        } catch (SQLException e) {
           // e.printStackTrace();
        }

        return isUpdated;
    }



    public static boolean delete(String loanId) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isDeleted = false;

        String disableForeignKeyChecks = "SET FOREIGN_KEY_CHECKS=0";
        String enableForeignKeyChecks = "SET FOREIGN_KEY_CHECKS=1";

        String deleteChildRows = "DELETE FROM customer_loan_details WHERE loan_id = ?";
        String deleteParentRow = "DELETE FROM loan WHERE loan_id = ?";

        try {
            connection = DbConnection.getInstance().getConnection();
            pstm = connection.prepareStatement(deleteChildRows);
            pstm.setString(1, loanId);
            pstm.executeUpdate();


            pstm = connection.prepareStatement(deleteParentRow);
            pstm.setString(1, loanId);
            int parentRowAffected = pstm.executeUpdate();

            isDeleted = parentRowAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting rows: " + e.getMessage());

        }

        return isDeleted;
    }


    public static LoanTm searchById(String nic) throws SQLException {
        String sql = "select * from loan where nic=?";
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        LoanTm loanTm = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, nic);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                String loanId = resultSet.getString("loan_id");
                String application = resultSet.getString("application");
                String loanAmount = resultSet.getString("loan_amount");
                String loanType = resultSet.getString("loan_type");
                String loanTerm = resultSet.getString("loan_term");
                String collateral = resultSet.getString("collateral");
                String purpose = resultSet.getString("purpose");
                String customerId = resultSet.getString("c_id");
                double percentage=resultSet.getDouble("percentage");
                 nic =resultSet.getString("nic");
                 String date=resultSet.getString("date");

                 loanTm= new LoanTm(loanId, application,loanAmount, loanType,  loanTerm, collateral, purpose, customerId,percentage,nic,date);
            }
        } catch (SQLException e){
           // new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        return loanTm;
    }


    /* public static void load(TableView<Inquiry> tableView) {
        String sql = "SELECT * FROM inquiries";
        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String id = resultSet.getString("in_id");
                String type = resultSet.getString("in_type");
                String inquiryDate = resultSet.getString("in_date");
                String responseDate = resultSet.getString("response_date");
                Inquiry inquiry = new Inquiry(id, type, inquiryDate, responseDate);
                tableView.getItems().add(inquiry);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching inquiries: " + e.getMessage());
        }
    }*/
    public static List<LoanTm> getAll() throws SQLException {
        String sql = "SELECT * FROM loan";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<LoanTm> loanList = new ArrayList<>();

        while (resultSet.next()) {

            String loanId = resultSet.getString("loan_id");
            String application = resultSet.getString("application");
            String loanAmount = resultSet.getString("loan_amount");
            String loanType = resultSet.getString("loan_type");
            String loanTerm = resultSet.getString("loan_term");
            String collateral = resultSet.getString("collateral");
            String purpose = resultSet.getString("purpose");
            String customerId = resultSet.getString("c_id");
            double percentage = resultSet.getDouble("percentage");
            String nic =resultSet.getString("nic");
            String date=resultSet.getString("date");
            LoanTm loan = new LoanTm(loanId, application, loanAmount, loanType, loanTerm, collateral, purpose,customerId, percentage,nic,date);
            loanList.add(loan);
        }
        return loanList;

    }

    public static String getLastLoanId() throws SQLException {
        String sql = "SELECT loan_id FROM loan ORDER BY loan_id DESC LIMIT 1";

        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        String lastId = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            pstm = connection.prepareStatement(sql);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                lastId = resultSet.getString("loan_id");
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        return lastId;
    }

    public static InterestRateTm searchByLoanType(String type) {
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        InterestRateTm interestRate = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "SELECT percentage FROM interest_rate WHERE loan_type=?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, type);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                double percentage = resultSet.getDouble("percentage");
                interestRate = new InterestRateTm(type, percentage);
            }

        } catch (SQLException e) {
            //e.printStackTrace();
        }

        return interestRate;

}

    public static String getCustId(String nic) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        String customerId = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "SELECT c_id FROM customer WHERE nic = ?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, nic);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                customerId = resultSet.getString("c_id");
            }

        } catch (SQLException e) {
           // e.printStackTrace();

        }

        return customerId;
    }


}

   /* public static LoanTm getLoanById(String loanId) throws SQLException {
        String sql = "SELECT * FROM loan WHERE nic = ?";
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        LoanTm loan = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, loanId);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                loan = new LoanTm();
                loan.setLoan_id(resultSet.getString("loan_id"));
                loan.setLoan_amount(String.valueOf(resultSet.getDouble("loan_amount")));
                loan.setPercentage(String.valueOf(resultSet.getDouble("percentage")));
                // Set other loan details as needed
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();


        }
        return loan;
    }*/




