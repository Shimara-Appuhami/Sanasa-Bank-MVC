package lk.ijse.gdse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse.db.DbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForgotFormController {

    @FXML
    private JFXButton btnLogin;

    @FXML
    private TextField txtNic;

    @FXML
    private AnchorPane node;

    @FXML
    private void txtNicOnAction(ActionEvent event) {
        // Handle action for txtNic field if needed
    }

    @FXML
    private void btnLoginOnAction(ActionEvent actionEvent) {
        String nic = txtNic.getText();

        try {
            checkCredential(nic);
        } catch (SQLException | IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error checking credentials: " + e.getMessage()).show();
        }
    }

    private void checkCredential(String nic) throws SQLException, IOException {
        String sql = "SELECT NIC FROM users WHERE NIC = ?";

        try {
            Connection connection = DbConnection.getInstance().getConnection();

             PreparedStatement pstm = connection.prepareStatement(sql);

            pstm.setString(1, nic);

                ResultSet resultSet = pstm.executeQuery();

                if (resultSet.next()) {
                    String dbNic = resultSet.getString("NIC");

                    if (nic.equals(dbNic)) {
                        navigateToChangePassword();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Sorry! NIC is incorrect!").show();
                    }
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Sorry! NIC not found!").show();
                }

        }catch (SQLException e){
            new Alert(Alert.AlertType.CONFIRMATION,e.getMessage()).show();
        }
    }

    private void navigateToChangePassword() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ChangePassword-form.fxml"));
        node.getChildren().clear();
        node.getChildren().add(root);
    }
}
