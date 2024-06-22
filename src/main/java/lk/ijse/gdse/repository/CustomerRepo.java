package lk.ijse.gdse.repository;

import javafx.scene.control.Alert;
import lk.ijse.gdse.db.DbConnection;
import lk.ijse.gdse.model.Customer;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepo {
    public static boolean save(Customer customer) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isSaved = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "INSERT INTO customer VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, customer.getCId());
            pstm.setString(2, customer.getCName());
            pstm.setString(3, customer.getCEmail());
            pstm.setString(4, customer.getCContact());
            pstm.setString(5, customer.getCAddress());
            pstm.setString(6, customer.getCAge());
            pstm.setString(7, customer.getDateOfBirth());
            pstm.setString(8,customer.getNic());
            pstm.setString(9, customer.getRegistrationDate());
            pstm.setString(10, customer.getAnnualIncome());


            isSaved = pstm.executeUpdate() > 0;
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return isSaved;
    }


    public static boolean update(Customer customer) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isUpdated = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "UPDATE customer SET c_name=?, c_email=?, c_contact=?, c_address=?, c_age=?, date_of_birth=?,nic=?, registration_date=?, annual_income=? WHERE c_id=?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(10, customer.getCId());
            pstm.setString(1, customer.getCName());
            pstm.setString(2, customer.getCEmail());
            pstm.setString(3, customer.getCContact());
            pstm.setString(4, customer.getCAddress());
            pstm.setString(5, customer.getCAge());
            pstm.setString(6, customer.getDateOfBirth());
            pstm.setString(7,customer.getNic());
            pstm.setString(8, customer.getRegistrationDate());
            pstm.setString(9, customer.getAnnualIncome());


            isUpdated = pstm.executeUpdate() > 0;
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return isUpdated;
    }

    public static boolean delete(String cId) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isDeleted = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "DELETE FROM customer WHERE c_id = ?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, cId);

            isDeleted = pstm.executeUpdate() > 0;
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,"This customer got a loan ,Can't delete details").show();
        }

        return isDeleted;
    }

    public static Customer searchById(String cId) throws SQLException {
        String sql = "SELECT * FROM customer WHERE c_id = ?";
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        Customer customer = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, cId);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                String id = resultSet.getString("c_id");
                String name = resultSet.getString("c_name");
                String email = resultSet.getString("c_email");
                String contact = resultSet.getString("c_contact");
                String address = resultSet.getString("c_address");
                String age = resultSet.getString("c_age");
                String birth = resultSet.getString("date_of_birth");
                String nic=resultSet.getString("nic");
                String registrationDate = resultSet.getString("registration_date");
                String income = resultSet.getString("annual_income");


                customer = new Customer(id, name, email, contact, address, age, birth,nic, registrationDate, income);
            } else {
                // Customer with the provided ID not found
                System.out.println("Customer not found for ID: " + cId);
            }
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return customer;
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
    public static List<Customer> getAll() throws SQLException {
        String sql = "SELECT * FROM customer";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Customer> customerArrayList = new ArrayList<>();

        while (resultSet.next()) {
            String id=resultSet.getString("c_id");
            String name = resultSet.getString("c_name");
            String email = resultSet.getString("c_email");
            String contact = resultSet.getString("c_contact");
            String address = resultSet.getString("c_address");
            String age = resultSet.getString("c_age");
            String birth = resultSet.getString("date_of_birth");
            String nic=resultSet.getString("nic");
            String registrationDate = resultSet.getString("registration_date");
            String income = resultSet.getString("annual_income");

            Customer customer = new Customer(id, name, email, contact, address, age, birth,nic, registrationDate, income);

            customerArrayList.add(customer);
        }
        return customerArrayList;

    }
    public static String getLastCustomerId() throws SQLException {
        String sql = "SELECT c_id FROM customer ORDER BY c_id DESC LIMIT 1";

        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        String lastId = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            pstm = connection.prepareStatement(sql);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                lastId = resultSet.getString("c_id");
            }
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return lastId;
    }
   /* public static List<String> getIds() throws SQLException {
        String sql = "SELECT in_id FROM inquiries";
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
*/
    public static Customer searchByNic(String nic) throws SQLException {
        String sql = "SELECT * FROM customer WHERE nic = ?";
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        Customer customer = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, nic);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                String id = resultSet.getString("c_id");
                String name = resultSet.getString("c_name");
                String email = resultSet.getString("c_email");
                String contact = resultSet.getString("c_contact");
                String address = resultSet.getString("c_address");
                String age = resultSet.getString("c_age");
                String birth = resultSet.getString("date_of_birth");
                String nicc=resultSet.getString("nic");
                String registrationDate = resultSet.getString("registration_date");
                String income = resultSet.getString("annual_income");


                customer = new Customer(id, name, email, contact, address, age, birth,nicc, registrationDate, income);
            } else {
                System.out.println("Customer not found for ID: " + nic);
            }
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return customer;

    }
}