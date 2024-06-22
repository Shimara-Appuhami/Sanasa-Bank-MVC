package lk.ijse.gdse.repository;

import javafx.scene.control.Alert;
import lk.ijse.gdse.db.DbConnection;
import lk.ijse.gdse.model.Balance;
import lk.ijse.gdse.model.Inquiry;
import lk.ijse.gdse.model.tm.BalanceTm;
import lk.ijse.gdse.model.tm.LoanTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BalanceRepo {

    public static boolean save(Balance balance) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isSaved = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "INSERT INTO balance (b_id, loan_id, principal_balance, interest_balance, total_balance, last_updated_date) VALUES (?, ?, ?, ?, ?, ?)";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, balance.getBId());
            pstm.setString(3, balance.getPrincipalBalance());
            pstm.setString(4, balance.getInterestBalance());
            pstm.setString(5, balance.getTotalBalance());
            pstm.setString(6, balance.getLastUpdatedDate());
            pstm.setString(2,balance.getLoanId());

            isSaved = pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        return isSaved;
    }

    public static boolean update(Balance balance) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isUpdated = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "UPDATE balance SET loan_id=?,principal_balance=?, interest_balance=?, total_balance=?, last_updated_date=? WHERE b_id=?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(6, balance.getBId());
            pstm.setString(2, balance.getPrincipalBalance());
            pstm.setString(3, balance.getInterestBalance());
            pstm.setString(4, balance.getTotalBalance());
            pstm.setString(5, balance.getLastUpdatedDate());
            pstm.setString(1,balance.getLoanId());

            isUpdated = pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        return isUpdated;
    }

    public static boolean delete(String bId) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isDeleted = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "DELETE FROM balance WHERE b_id=?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, bId);

            isDeleted = pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        return isDeleted;
    }

    public static List<BalanceTm> getAll() throws SQLException {
        String sql = "SELECT * FROM balance";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<BalanceTm> balanceList = new ArrayList<>();
        while (resultSet.next()) {
            String bId = resultSet.getString("b_id");
            String loanId=resultSet.getString("loan_id");
            String principalBalance = resultSet.getString("principal_balance");
            String interestBalance = resultSet.getString("interest_balance");
            String totalBalance = resultSet.getString("total_balance");
            String lastUpdatedDate = resultSet.getString("last_updated_date");

            BalanceTm balance = new BalanceTm(bId,loanId, principalBalance, interestBalance, totalBalance, lastUpdatedDate);
            balanceList.add(balance);
        }
        return balanceList;

    }


    public static BalanceTm searchById(String nic) throws SQLException {
        String sql = "SELECT b.b_id, b.loan_id, b.principal_balance, b.interest_balance, b.total_balance, b.last_updated_date " +
                "FROM balance b " +
                "JOIN loan l ON b.loan_id = l.loan_id " +
                "JOIN customer c ON c.c_id = l.c_id " +
                "WHERE c.nic = ?";
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        BalanceTm balance = null;


        try {
            connection = DbConnection.getInstance().getConnection();
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, nic);
            resultSet = pstm.executeQuery();
            if (resultSet.next()) {

                balance.setB_id(resultSet.getString("b_id"));
                balance.setLoan_id(resultSet.getString("loan_id"));
                balance.setPrincipal_balance(resultSet.getString("principal_balance"));
                balance.setInterest_balance(resultSet.getString("interest_balance"));
                balance.setTotal_balance(resultSet.getString("total_balance"));
                balance.setLast_updated_date(resultSet.getString("last_updated_date"));
                // balance = new BalanceTm(loanId, application, loanAmount, loanType, loanTerm, collateral, purpose, customerId, percentage, nic, date);
            }
        } catch (SQLException e) {
            // new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        return balance;
    }


    public static String getLastBalanceId() throws SQLException {
        String balanceId = null;
        String sql = "SELECT b_id FROM balance ORDER BY b_id DESC LIMIT 1";

        try {
            Connection connection = DbConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql);
            ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                balanceId = resultSet.getString("b_id");
            }
        } catch (SQLException e) {


        }
        return balanceId;
    }

    /*public static BalanceTm calculatePaymentAmount(String nic, double loanAmount, double interestRate) {
        Connection connection = null;
        PreparedStatement pstm = null;
        BalanceTm balanceTm = null;
        ResultSet resultSet = null;

        try {
            connection = DbConnection.getInstance().getConnection();

            // Query to get loan details based on customer ID (NIC)
            String loanQuery = "SELECT l.loan_amount, SUM(p.p_amount),p.p_date AS payment_count\n" +
                    "FROM loan l\n" +
                    "JOIN payment p ON l.loan_id = p.loan_id\n" +
                    "WHERE p.nic = ?\n" +
                    "GROUP BY l.loan_amount;";
            pstm = connection.prepareStatement(loanQuery);
            pstm.setString(1, nic);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                String id = resultSet.getString("b_id");
                 nic = resultSet.getString("nic");
                String principalBalance = resultSet.getString("principal_balance");
                String interestBalance = resultSet.getString("interest_balance");
                String totalBalance = resultSet.getString("total_balance");
                String lastUpdatedDate = resultSet.getString("last_updated_date");
                balanceTm = new BalanceTm(id, nic, principalBalance, interestBalance, totalBalance, lastUpdatedDate);
            }
        } catch (SQLException e) {
            //    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        return balanceTm;
    }
*/
    public static String getAmount(String nic) {
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        String amount = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "SELECT loan_amount FROM loan WHERE nic = ?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, nic); // Set the NIC value to the prepared statement
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                amount = resultSet.getString("loan_amount");
            }

        } catch (SQLException e) {

        }

        return amount;
    }

    public static String getPayBalance(String nic) {
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        String amount = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "select SUM(p_amount) from payment where nic=?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, nic);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                amount = resultSet.getString("SUM(p_amount)");
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        return amount;

    }

    public static String getTotalBalance(String nic) {
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        String totalBalance = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "SELECT " +
                    "    (SELECT loan_amount FROM loan WHERE nic = ?) - " +
                    "    COALESCE((SELECT SUM(p_amount) FROM payment WHERE nic = ?), 0) AS total_balance";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, nic);
            pstm.setString(2, nic);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                totalBalance = resultSet.getString("total_balance");
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        return totalBalance;
    }


    public static String getLastPaymentDate(String nic) {
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        String lastPaymentDate = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "SELECT MAX(p_date) AS last_payment_date FROM payment WHERE nic=?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, nic);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                lastPaymentDate = resultSet.getString("last_payment_date");
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        return lastPaymentDate;
    }

    public static String getLoanId(String nic) {
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        String loanId = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "SELECT loan_id FROM loan WHERE nic=?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, nic);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                loanId = resultSet.getString("loan_id");
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        return loanId;
    }
}