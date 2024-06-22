package lk.ijse.gdse.repository;


import javafx.scene.control.Alert;
import lk.ijse.gdse.db.DbConnection;
import lk.ijse.gdse.model.Customer;
import lk.ijse.gdse.model.Inquiry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InquiryRepo {
    public static boolean save(Inquiry inquiry) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isSaved = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "INSERT INTO inquiries VALUES (?, ?, ?, ?,?,?)";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, inquiry.getInId());
            pstm.setString(2, inquiry.getNic());
            pstm.setString(3, inquiry.getCId());
            pstm.setString(4, inquiry.getInType());
            pstm.setString(5, inquiry.getInDate());
            pstm.setString(6, inquiry.getResponseDate());

            isSaved = pstm.executeUpdate() > 0;
        } catch (SQLException e){
          //  new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return isSaved;
    }


    public static boolean update(Inquiry inquiry) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isUpdated = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "UPDATE inquiries SET nic=?,c_id=?,in_type=?, in_date=?, response_date=? WHERE in_id=?";
            pstm = connection.prepareStatement(sql);

            pstm.setString(1, inquiry.getNic());
            pstm.setString(2, inquiry.getCId());
            pstm.setString(3, inquiry.getInType());
            pstm.setString(4, inquiry.getInDate());
            pstm.setString(5, inquiry.getResponseDate());
            pstm.setString(6, inquiry.getInId());

            isUpdated = pstm.executeUpdate() > 0;
        }catch (SQLException e){
           // new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return isUpdated;
    }

    public static boolean delete(String inId) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isDeleted = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "DELETE FROM inquiries WHERE in_id = ?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, inId);

            isDeleted = pstm.executeUpdate() > 0;
        } catch (SQLException e){
          //  new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        return isDeleted;
    }

    public static Inquiry searchById(String nic) throws SQLException {
        String sql = "SELECT * FROM inquiries WHERE nic = ?";
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        Inquiry inquiry = null;

        try {
            connection = DbConnection.getInstance().getConnection(); // Get the database connection
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, nic);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                String id = resultSet.getString("in_id");
                String nicc = resultSet.getString("nic");
                String cId = resultSet.getString("c_id");
                String type = resultSet.getString("in_type");
                String inquiryDate = resultSet.getString("in_date");
                String responseDate = resultSet.getString("response_date");
                inquiry = new Inquiry(id,nicc,cId,type, inquiryDate, responseDate);
            }
        }catch (SQLException e){
        //    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        return inquiry;
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
    public static List<Inquiry> getAll() throws SQLException {
        String sql = "SELECT * FROM inquiries";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Inquiry> inquiryList = new ArrayList<>();

        while (resultSet.next()) {
            String id = resultSet.getString("in_id");
            String nic = resultSet.getString("nic");
            String cId = resultSet.getString("c_id");
            String type = resultSet.getString("in_type");
            String inquiryDate = resultSet.getString("in_date");
            String responseDate = resultSet.getString("response_date");
            Inquiry inquiry = new Inquiry(id,nic,cId, type, inquiryDate, responseDate);
            inquiryList.add(inquiry);
        }
        return inquiryList;

    }
    public static String getLastInquiryId() throws SQLException {
        String sql = "SELECT in_id FROM inquiries ORDER BY in_id DESC LIMIT 1";

        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        String lastId = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            pstm = connection.prepareStatement(sql);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                lastId = resultSet.getString("in_id");
            }
        } catch (SQLException e){
          //  new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return lastId;
    }
    public static String searching(String nic) throws SQLException {
        String sql = "SELECT c_id FROM customer WHERE nic = ?";
        String customerId = null;

        try {
            Connection connection = DbConnection.getInstance().getConnection();

            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, nic);

            ResultSet resultSet = pstm.executeQuery();
            if (resultSet.next()) {
                customerId = resultSet.getString("c_id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerId;

    }

   // public static Inquiry getCustId(String id) {
        public static String getCustId(String nic) throws SQLException {
            Connection connection = null;
            PreparedStatement pstm = null;
            ResultSet resultSet = null;
            String customerId = null;

            try {
                connection = DbConnection.getInstance().getConnection();
                String sql = "SELECT c_id FROM customer WHERE nic = ?";
                pstm = connection.prepareStatement(sql);
                resultSet = pstm.executeQuery(sql);

                pstm.setString(1, nic);

                if (resultSet.next()) {
                    customerId = resultSet.getString("c_id");
                }

            } catch (SQLException e) {
                //new Alert(Alert.AlertType.CONFIRMATION,e.getMessage()).show();
            }
            return customerId;

        }

}