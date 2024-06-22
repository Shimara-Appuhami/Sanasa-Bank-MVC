package lk.ijse.gdse.repository;


import javafx.scene.control.Alert;
import lk.ijse.gdse.db.DbConnection;
import lk.ijse.gdse.model.Inquiry;
import lk.ijse.gdse.model.InterestRate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InterestRateRepo {
    public static boolean save(InterestRate interestRate) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isSaved = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "INSERT INTO interest_rate  VALUES (?, ?, ?)";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, interestRate.getRateId());
            pstm.setString(2, interestRate.getLoanType());
            pstm.setString(3, interestRate.getPercentage());


            isSaved = pstm.executeUpdate() > 0;
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return isSaved;
    }


    public static boolean update(InterestRate interestRate) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isUpdated = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "UPDATE interest_rate SET loan_type=?, percentage=? WHERE rate_id=?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(3, interestRate.getRateId());
            pstm.setString(1, interestRate.getLoanType());
            pstm.setString(2, interestRate.getPercentage());


            isUpdated = pstm.executeUpdate() > 0;
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return isUpdated;
    }

    public static boolean delete(String rateId) throws SQLException {
        Connection connection = null;
        PreparedStatement pstm = null;
        boolean isDeleted = false;

        try {
            connection = DbConnection.getInstance().getConnection();
            String sql = "DELETE FROM interest_rate WHERE rate_id=?";
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, rateId);

            isDeleted = pstm.executeUpdate() > 0;
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        return isDeleted;
    }

    public static InterestRate searchById(String loanTYpe) throws SQLException {
        String sql = "SELECT * FROM interest_rate WHERE loan_type = ?";
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        InterestRate interestRate = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, loanTYpe);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                String rateId = resultSet.getString("rate_id");
                String percentage = resultSet.getString("percentage");
                 interestRate = new InterestRate( rateId,loanTYpe, percentage);
            }
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return interestRate;
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
    public static List<InterestRate> getAll() throws SQLException {
        String sql = "SELECT * FROM interest_rate";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<InterestRate> interestRateList = new ArrayList<>();

        while (resultSet.next()) {
            String rateId = resultSet.getString("rate_id");
            String loanType = resultSet.getString("loan_type");
            String percentage = resultSet.getString("percentage");

            InterestRate interestRate = new InterestRate(rateId, loanType, percentage);
            interestRateList.add(interestRate);
        }
        return interestRateList;

    }
    public static String getInterestRateId() throws SQLException {
        String sql = "SELECT rate_id FROM interest_rate ORDER BY rate_id DESC LIMIT 1";

        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        String lastId = null;

        try {
            connection = DbConnection.getInstance().getConnection();
            pstm = connection.prepareStatement(sql);
            resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                lastId = resultSet.getString("rate_id");
            }
        } catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        return lastId;
    }
  /*  public static List<String> getIds() throws SQLException {
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
    }*/
  public static double getInterestRateByType(String loanType) throws SQLException {
      String sql = "SELECT percentage FROM interest_rate WHERE loan_type = ?";
      Connection connection = null;
      PreparedStatement pstm = null;
      ResultSet resultSet = null;
      double interestRate = 0.0;

      try {
          connection = DbConnection.getInstance().getConnection();
          pstm = connection.prepareStatement(sql);
          pstm.setString(1, loanType);
          resultSet = pstm.executeQuery();

          if (resultSet.next()) {
              interestRate = resultSet.getDouble("percentage");
          }
      } catch (SQLException e) {
          new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
      }
          return interestRate;
      }


}