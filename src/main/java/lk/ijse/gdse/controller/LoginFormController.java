package lk.ijse.gdse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.gdse.db.DbConnection;
import lk.ijse.gdse.repository.InquiryRepo;
import lk.ijse.gdse.repository.LoginRepo;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginFormController {
@FXML
    public Hyperlink txtForgot;
    public Hyperlink txtExit;



    @FXML
    private TextField txtPassword;


    @FXML
    private TextField txtUsername;



    @FXML
    private void initialize() {
        suggestLastUsername();
    }
    @FXML
    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        String userId = txtUsername.getText();
        String pw = txtPassword.getText();

        try {
            checkCredential(userId, pw);
           // navigateToTheDashboard();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        //navigateToTheDashboard();
    }
@FXML
    private void checkCredential(String userId, String pw) throws SQLException, IOException {
        String sql = "SELECT userId, password FROM users WHERE userId = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, userId);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String dbPw = resultSet.getString("password");

            if(pw.equals(dbPw)) {
                navigateToTheDashboard();
            } else {
                new Alert(Alert.AlertType.ERROR, "sorry! password is incorrect!").show();
            }

        } else {
            new Alert(Alert.AlertType.INFORMATION, "sorry! user id can't be find!").show();
        }
    }
@FXML
private void navigateToTheDashboard() throws IOException {
    AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard-form.fxml"));

    Scene scene = new Scene(rootNode);

    Stage stage = (Stage) txtUsername.getScene().getWindow();
    stage.setScene(scene);
    stage.centerOnScreen();
    stage.setTitle("Dashboard Form");
    ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), rootNode);
    scaleTransition.setFromX(0.5);
    scaleTransition.setFromY(0.5);
    scaleTransition.setToX(1.0);
    scaleTransition.setToY(1.0);
    scaleTransition.play();
}

    @FXML
    public void txtClickOnAction(ActionEvent actionEvent) throws IOException {

    }

    public void txtUsernameOnAction(ActionEvent actionEvent) {

    }

    public void txtPasswordOnAction(ActionEvent actionEvent) {

    }

    public void txtForgotOnAction(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/Forgot-form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) txtUsername.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Forgot Form");

    }
    private void suggestLastUsername() {
        try {
            String lastUsername = LoginRepo.getLastEnteredUsername();
            if (lastUsername != null && !lastUsername.isEmpty()) {
                txtUsername.setText(lastUsername);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading last username: " + e.getMessage()).show();
        }
    }

    public void txtExitOnAction(ActionEvent event) {
        System.exit(0);
    }
}
