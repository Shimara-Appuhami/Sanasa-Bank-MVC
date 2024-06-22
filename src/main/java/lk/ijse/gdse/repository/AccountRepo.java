package lk.ijse.gdse.repository;

import javafx.scene.control.Alert;
import lk.ijse.gdse.db.DbConnection;
import lk.ijse.gdse.model.Account;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountRepo {
    public static boolean save(Account account) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isSaved = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "INSERT INTO account VALUES (?, ?, ?, ?, ?, ?)";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, account.getANo());
            pstm.setString(2, account.getCId());
            pstm.setString(3, account.getAType());
            pstm.setString(4, account.getABalance());
            pstm.setString(5, account.getOpenDate());
            pstm.setString(6, account.getStatus());

            isSaved = pstm.executeUpdate() > 0;
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return isSaved;
    }


    public static boolean update(Account account) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isUpdated = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "UPDATE account SET c_id=?, a_type=?, a_balance=?, open_date=?, status=? WHERE a_no=?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(6, account.getANo());
            pstm.setString(1, account.getCId());
            pstm.setString(2, account.getAType());
            pstm.setString(3, account.getABalance());
            pstm.setString(4, account.getOpenDate());
            pstm.setString(5, account.getStatus());

            isUpdated = pstm.executeUpdate() > 0;
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();

        }

        return isUpdated;
    }

    public static boolean delete(String aNo) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isDeleted = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "DELETE FROM account WHERE a_no = ?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, aNo);

            isDeleted = pstm.executeUpdate() > 0;
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return isDeleted;
    }

    public static Account searchById(String nic) throws SQLException {
        String sql = "SELECT l.a_no, l.c_id, l.a_type, l.a_balance, l.open_date, l.status " +
                "FROM account l " +
                "JOIN customer c ON l.c_id = c.c_id " +
                "WHERE c.nic = ?";

        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        Account account = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, nic);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                String aNo1 = resultSet.getString("a_no");
                String cId = resultSet.getString("c_id");
                String aType = resultSet.getString("a_type");
                String aBalance = resultSet.getString("a_balance");
                String openDate = resultSet.getString("open_date");
                String status = resultSet.getString("status");


                account = new Account(aNo1, cId, aType, aBalance, openDate, status);
            } else {
                System.out.println("Account not found for ID: " + nic);
            }
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return account;
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
    public static List<Account> getAll() throws SQLException {
        String sql = "SELECT * FROM account";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Account> accountList = new ArrayList<>();

        while (resultSet.next()) {
            String aNo=resultSet.getString("a_no");
            String cId = resultSet.getString("c_id");
            String aType = resultSet.getString("a_type");
            String aBalance = resultSet.getString("a_balance");
            String openDate = resultSet.getString("open_date");
            String status = resultSet.getString("status");
            Account account = new Account(aNo,cId, aType,aBalance, openDate, status);

            accountList.add(account);
        }
        return accountList;

    }
    public static String getLastAccountId() throws SQLException {
        String sql = "SELECT a_no FROM account ORDER BY a_no DESC LIMIT 1";

        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        String lastId = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            pstm = connection.prepareStatement(sql);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                lastId = resultSet.getString("a_no");
            }
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return lastId;
    }
    public static List<String> getIds() throws SQLException {
        String sql = "SELECT c_id FROM customer";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }
}