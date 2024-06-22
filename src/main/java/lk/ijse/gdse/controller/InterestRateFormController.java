package lk.ijse.gdse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.gdse.Util.Regex;
import lk.ijse.gdse.model.Inquiry;
import lk.ijse.gdse.model.InterestRate;
import lk.ijse.gdse.model.tm.InquiryTm;
import lk.ijse.gdse.model.tm.InterestRateTm;
import lk.ijse.gdse.repository.CustomerRepo;
import lk.ijse.gdse.repository.InquiryRepo;
import lk.ijse.gdse.repository.InterestRateRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class InterestRateFormController {


    public TableView tblInterestRate;
    public JFXButton btnBack;

    public TableColumn colLoanType;
    public JFXComboBox <String>cmbLoanType;



    @FXML
    private TableColumn<InterestRate, String> colPercentage;

    @FXML
    private TableColumn<InterestRate, String> colRateId;


    @FXML
    private TextField txtPercentage;

    @FXML
    private TextField txtRateId;

    private void initializeComboBoxLoanType() {
        ObservableList<String> loanType = FXCollections.observableArrayList(
                "Housing Loan",
                "Educational Loan",
                "Personal Loans",
                "Business Loans",
                "Awrudu Loan",
                "Leasing"
        );
        cmbLoanType.setItems(loanType);
    }
    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtRateId.clear();
        cmbLoanType.setValue(null);
        txtPercentage.clear();



    }
    @FXML
    void btnDeleteOnAction(ActionEvent actionEvent) {
        String rateId = txtRateId.getText();

        if (!rateId.isEmpty()) {
            try {
                boolean isDeleted = InterestRateRepo.delete(rateId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Interest Rate deleted successfully!").show();
                    initialize();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete Interest Rate!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Error deleting Interest Rate: " + e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter an Interest Rate ID to delete!").show();
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String rateId = txtRateId.getText();
        String loanType = String.valueOf(cmbLoanType.getValue());
        String percentage = txtPercentage.getText();

        InterestRate interestRate = new InterestRate(rateId, loanType, percentage);
        try {if (isValied()){
            boolean isUpdated = InterestRateRepo.update(interestRate);

            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Interest Rate updated").show();
                initialize();
                clearFields();
            }
            } else {
                new Alert(Alert.AlertType.WARNING, "Interest Rate update failed").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error updating Interest Rate: " + e.getMessage()).show();
        }
    }
    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        String loanType = cmbLoanType.getValue();


        try {
            InterestRate interestRate  = InterestRateRepo.searchById(loanType);
            if (interestRate != null) {
                txtRateId.setText(interestRate.getRateId());
                txtPercentage.setText(interestRate.getPercentage());

            } else {
                new Alert(Alert.AlertType.INFORMATION, "Interest Rate not found!").show();
                initialize();

            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error searching Interest Rate: " + e.getMessage()).show();
        }
    }


    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String rateId = txtRateId.getText();
        String loanType = String.valueOf(cmbLoanType.getValue());
        String percentage = txtPercentage.getText();


        InterestRate interestRate = new InterestRate(rateId, loanType, percentage);

        try {if (isValied()){
            boolean isSaved = InterestRateRepo.save(interestRate);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Interest Rate saved successfully").show();

                initialize();
                clearFields();
            }
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to save Interest Rate").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error saving Interest Rate: " + e.getMessage()).show();
        }
    }
    public void initialize() {
        initializeComboBoxLoanType();
        //getInquiryIds();
        lastInterestRateId();
        setCellValueFactory();
        loadInterestRates();

    }
    public void setCellValueFactory() {
        colRateId.setCellValueFactory(new PropertyValueFactory<>("rate_id"));
        colLoanType.setCellValueFactory(new PropertyValueFactory<>("loan_type"));
        colPercentage.setCellValueFactory(new PropertyValueFactory<>("percentage"));

    }
    private void loadInterestRates() {
        ObservableList<InterestRateTm> obList = FXCollections.observableArrayList();

        try {
            List<InterestRate> interestRateList = InterestRateRepo.getAll();
            for (InterestRate interestRate : interestRateList) {
                InterestRateTm tm = new InterestRateTm(
                        interestRate.getRateId(),
                        interestRate.getLoanType(),
                        interestRate.getPercentage()
                );

                obList.add(tm);
            }

            tblInterestRate.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void txtPercentageOnAction(ActionEvent event) {
    }
    public void btnBackOnAction(ActionEvent event) throws IOException {

        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard-form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) btnBack.getScene().getWindow(); // Use any control's scene
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }
    private void lastInterestRateId() {
        try {
            String lastId = InterestRateRepo.getInterestRateId();
            String nextId = generateNextId(lastId);
            txtRateId.setText(nextId);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error generating next ID: " + e.getMessage());
        }
    }

    private String generateNextId(String lastId) {
        String nextId = null;
        try {
            if (lastId != null && !lastId.isEmpty()) {
                int lastNumericId = Integer.parseInt(lastId.substring(3));
                int nextNumericId = lastNumericId + 1;
                nextId = String.format("INR%03d", nextNumericId);
                nextId = "INR001";
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error generating next ID: Invalid ID format");
        }
        return nextId;
    }


    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }
   /* private void getInquiryIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> idList = CustomerRepo.getIds();

            for(String id : idList) {
                obList.add(id);
            }

            cmbInquiryId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

*/
    public void cmbLoanTypeOnAction(ActionEvent event) {
    }

    public void txtRateIdOnAction(ActionEvent event) {
    }
    public void txtIdOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.INTERESTRATEID,txtRateId);
    }

    public void txtPercentageOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.PERCENTAGE,txtPercentage);
    }
    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.INTERESTRATEID,txtRateId)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.PERCENTAGE,txtPercentage)) return false;

        return true;
    }
}