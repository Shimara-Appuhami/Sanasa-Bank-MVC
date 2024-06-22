package lk.ijse.gdse.repository;

import javafx.scene.control.Alert;
import lk.ijse.gdse.db.DbConnection;
import lk.ijse.gdse.model.Customer;
import lk.ijse.gdse.model.Reminder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReminderRepo {
    public static boolean save(Reminder reminder) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isSaved = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "INSERT INTO reminder VALUES (?, ?, ?, ?, ?, ?,?)";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, reminder.getRId());
            pstm.setString(2, reminder.getNic());
            pstm.setString(3, reminder.getLoanType());
            pstm.setString(4, reminder.getRMessage());
            pstm.setString(5, reminder.getRType());
            pstm.setString(6, reminder.getRDate());
            pstm.setString(7, reminder.getRStatus());
            isSaved = pstm.executeUpdate() > 0;
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return isSaved;
    }

    public static boolean update(Reminder reminder) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isUpdated = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "UPDATE reminder SET nic=? ,loan_type=?, r_message=?, r_type=?, r_date=?, r_status=? WHERE r_id=?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(7, reminder.getRId());
            pstm.setString(1, reminder.getNic());
            pstm.setString(2, reminder.getLoanType());
            pstm.setString(3, reminder.getRMessage());
            pstm.setString(4, reminder.getRType());
            pstm.setString(5, reminder.getRDate());
            pstm.setString(6, reminder.getRStatus());
            isUpdated = pstm.executeUpdate() > 0;
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return isUpdated;
    }


    public static boolean delete(String rId) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isDeleted = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "DELETE FROM reminder WHERE r_id=?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, rId);

            isDeleted = pstm.executeUpdate() > 0;
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        return isDeleted;
    }

    public static Reminder searchById(String rId) throws SQLException {
        String sql = "SELECT * from reminder where nic = ?";


        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        Reminder reminder = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, rId);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                String reminderId = resultSet.getString("r_id");
                String nic = resultSet.getString("nic");
                String loanType = resultSet.getString("loan_type");
                String massage = resultSet.getString("r_message");
                String type = resultSet.getString("r_type");
                String date = resultSet.getString("r_date");
                String status = resultSet.getString("r_status");
                String email=resultSet.getString("c_email");

                reminder = new Reminder(reminderId,nic,loanType, massage, type, date, status,email);
            } else {
                System.out.println("Reminder not found for ID: " + rId);
            }
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return reminder;
    }


    public static List<Reminder> getAll() throws SQLException {
        List<Reminder> reminderList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "SELECT * FROM reminder";
            pstm = connection.prepareStatement(sql);
            resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                Reminder reminder = new Reminder(
                        resultSet.getString("r_id"),
                        resultSet.getString("nic"),
                        resultSet.getString("loan_type"),
                        resultSet.getString("r_message"),
                        resultSet.getString("r_type"),
                        resultSet.getString("r_date"),
                        resultSet.getString("r_status")
                );
                reminderList.add(reminder);
            }
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return reminderList;
    }


    public static String getLastReminderId() throws SQLException {
        String sql = "SELECT r_id FROM reminder ORDER BY r_id DESC LIMIT 1";

        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        String lastId = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            pstm = connection.prepareStatement(sql);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                lastId = resultSet.getString("r_id");
            }
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return lastId;
    }
    public static List<String> getIds() throws SQLException {
        String sql = "SELECT p_invoice_no FROM payment";
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

    public static String getLoanType(String nic) {
        String loanType = null;
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "SELECT ir.loan_type FROM customer c " +
                    "JOIN customer_loan_details cl ON c.c_id = cl.c_id " +
                    "JOIN loan l ON cl.loan_id = l.loan_id " +
                    "JOIN interest_rate ir ON l.loan_type = ir.loan_type " +
                    "WHERE c.nic = ?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, nic);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                loanType = resultSet.getString("loan_type");
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            e.printStackTrace();
        }

        return loanType;
    }

    public static String getEmail(String nic) {
        String email = null;
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "SELECT c_email from customer where nic = ?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, nic);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                email = resultSet.getString("c_email");
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            e.printStackTrace();
        }

        return email;
    }
}