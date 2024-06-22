package lk.ijse.gdse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.gdse.db.DbConnection;

import java.awt.*;
import java.io.IOException;
import java.security.cert.PolicyNode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public class DashboardFormController {
    public AnchorPane rootNode;
    public JFXButton btnInquiry;
    public JFXButton btnAccount;
    public JFXButton btnCustomer;
    public JFXButton btnLoan;

    public JFXButton btnInterestRate;

    public JFXButton btnEmployee;
    public JFXButton btnPayment;
    public JFXButton btnReminder;
    public JFXButton btnBalance;
    public Label lblCustomerCount;
    public Label lblEmployeeCount;

    public JFXButton btnCreateAccount;
    public Label lblDate;
    public JFXButton btnDashboard;
    public Label lblInquiryCount;
    public JFXButton btnSignOut;
    private int employeeCount;
    private int customerCount;
    private int inquiryCount;
    @FXML
    private AnchorPane node;


    public void initialize() {
        setDate();

        try {
            customerCount = getCustomerCount();
            employeeCount=getEmployeeCount();
            inquiryCount=getWeekInquiryCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        setCustomerCount(customerCount);
        setEmployeeCount(employeeCount);
        setInquiryCount(inquiryCount);
    }
    private void setCustomerCount(int customerCount) {
        lblCustomerCount.setText(String.valueOf(customerCount));
    }
    private void setEmployeeCount(int customerCount) {
        lblEmployeeCount.setText(String.valueOf(employeeCount));
    }
    private void setInquiryCount(int customerCount) {
        lblInquiryCount.setText(String.valueOf(inquiryCount));
    }

    public int getWeekInquiryCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS inquiry_count FROM inquiries WHERE WEEK(in_date) = WEEK(NOW())";

        try {
            Connection connection = DbConnection.getInstance().getConnection();

             PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("inquiry_count");
            }
        }catch (SQLException e){
            new Alert(Alert.AlertType.CONFIRMATION,e.getMessage()).show();
        }
        return 0;
    }
    private int getCustomerCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS customer_count FROM customer";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("customer_count");
        }
        return 0;
    }
    private int getEmployeeCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS employee_count FROM employee";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("employee_count");
        }
        return 0;
    }

    @FXML
void btnInquiryOnAction(ActionEvent event) throws IOException {
        Parent root =  FXMLLoader.load(getClass().getResource("/view/inquiry-form.fxml"));
        this.node.getChildren().clear();
        this.node.getChildren().add(root);

}
/*@FXML
    public void btnAccountBalanceOnAction(ActionEvent event) throws IOException {
    Parent root =  FXMLLoader.load(getClass().getResource("/view/accountBalance-form.fxml"));
    this.node.getChildren().clear();
    this.node.getChildren().add(root);
    }*/
@FXML
    public void btnAccountOnAction(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/view/account-form.fxml"));
    this.node.getChildren().clear();
    this.node.getChildren().add(root);



}
    @FXML
    public void btnCustomerOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/customer2-form.fxml"));
        this.node.getChildren().clear();
        this.node.getChildren().add(root);

    }
    @FXML
    public void btnLoanOnAction(ActionEvent event) throws IOException {
        Parent root =  FXMLLoader.load(getClass().getResource("/view/loan-form.fxml"));
        this.node.getChildren().clear();
        this.node.getChildren().add(root);



    }

    public void btnInterestRateOnAction(ActionEvent event) throws IOException {
        Parent root =  FXMLLoader.load(getClass().getResource("/view/interestRate-form.fxml"));
        this.node.getChildren().clear();
        this.node.getChildren().add(root);


    }

   /* @FXML
    public void btnCustomerLoanOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerLoan-form.fxml"));
        this.node.getChildren().clear();
        this.node.getChildren().add(root);
    }*/
    @FXML
    public void btnEmployeeOnAction(ActionEvent event) throws IOException {
        Parent root =  FXMLLoader.load(getClass().getResource("/view/employee-form.fxml"));
        this.node.getChildren().clear();
        this.node.getChildren().add(root);


    }
    @FXML
    public void btnPaymentOnAction(ActionEvent event) throws IOException {
        Parent root =  FXMLLoader.load(getClass().getResource("/view/payment-form.fxml"));
        this.node.getChildren().clear();
        this.node.getChildren().add(root);
    }
    @FXML
    public void btnReminderOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/reminder-form.fxml"));
        this.node.getChildren().clear();
        this.node.getChildren().add(root);
    }
    @FXML
    public void btnBalanceOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/balance-form.fxml"));
        this.node.getChildren().clear();
        this.node.getChildren().add(root);
    }


    public void txtDateOnAction(MouseEvent mouseEvent) {

    }



    public void lblDateOnAction(MouseEvent mouseEvent) {
    }

    public void btnCreateAccountOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/registration-form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.setScene(scene);

        stage.setTitle("Registration Form");

        stage.show();
    }
    private void setDate() {
        LocalDate now = LocalDate.now();
        lblDate.setText(String.valueOf(LocalDate.parse(String.valueOf(now))));
    }

    public void btnDashboardOnAction(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard-form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) btnDashboard.getScene().getWindow(); // Use any control's scene
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }

    public void btnSignOutOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login-form.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnSignOut.getScene().getWindow(); // Get the stage from the progress bar scene
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();
    }
}
