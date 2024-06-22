package lk.ijse.gdse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.gdse.Util.Regex;
import lk.ijse.gdse.model.Account;

import lk.ijse.gdse.model.Customer;
import lk.ijse.gdse.model.tm.AccountTm;
import lk.ijse.gdse.repository.AccountRepo;
import lk.ijse.gdse.repository.CustomerRepo;
import lk.ijse.gdse.repository.LoanRepo;


import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AccountFormController {


    public JFXButton btnBack;
    public JFXComboBox cmbCustomerId;
    public JFXComboBox<String> cmbAccountType;
    public JFXComboBox<String> cmbStatus;
    public TextField txtNic;
    public DatePicker txtOpenDate;
    @FXML
    private TableColumn<Account, String> colAccountBalance;

    @FXML
    private TableColumn<Account, String> colAccountType;

    @FXML
    private TableColumn<Account, String> colCustomerId;

    @FXML
    private TableColumn<Account, String> colOpenDate;

    @FXML
    private TableColumn<Account, String> colStatus;

    @FXML
    private TableColumn<Account, String> colaccountNo;

    @FXML
    private TableView<AccountTm> tblAccount;

    @FXML
    private TextField txtAccountBalance;





    @FXML
    private TextField txtaccountNo;
    private void initializeComboBoxType() {
        ObservableList<String> accountType = FXCollections.observableArrayList(
                "Saving Account",
                "Fix Account"

        );
        cmbAccountType.setItems(accountType);
    }
    private void initializeComboBoxStatus() {
        ObservableList<String> status = FXCollections.observableArrayList(
                "Active",
                "Closed"
        );
        cmbStatus.setItems(status);
    }
    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtaccountNo.clear();
        cmbCustomerId.getSelectionModel().clearSelection();
        cmbAccountType.getSelectionModel().clearSelection();
        txtAccountBalance.clear();
        txtOpenDate.setValue(null);
        cmbStatus.getSelectionModel().clearSelection();
        txtNic.clear();
    }


    @FXML
    void btnDeleteOnAction(ActionEvent actionEvent) {
        String accountNo = txtaccountNo.getText();

        if (!accountNo.isEmpty()) {
            try {
                boolean isDeleted = AccountRepo.delete(accountNo);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Account deleted successfully!").show();
                    initialize();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete Account!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Error deleting Account: " + e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter an Account No to delete!").show();
        }
    }


    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String accountNo = txtaccountNo.getText();
        String CustomerId = String.valueOf(cmbCustomerId.getValue());
        String AccountType = cmbAccountType.getValue();
        String AccountBalance = txtAccountBalance.getText();
        String OpenDate = String.valueOf(txtOpenDate.getValue());
        String Status = cmbStatus.getValue();


        Account account = new Account(accountNo, CustomerId, AccountType, AccountBalance,OpenDate,Status);


        try {if(isValied()){
            boolean isSaved = AccountRepo.save(account);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Account saved successfully").show();
                initialize();
                clearFields();
            }
            } else {
                new Alert(Alert.AlertType.WARNING, "Account saving failed").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error saving Account: " + e.getMessage()).show();
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String accountNo = txtaccountNo.getText();
        String CustomerId = String.valueOf(cmbCustomerId.getValue());
        String AccountType = cmbAccountType.getValue();
        String AccountBalance = txtAccountBalance.getText();
        String OpenDate = String.valueOf(txtOpenDate.getValue());
        String Status = cmbStatus.getValue();


        Account account = new Account(accountNo, CustomerId, AccountType, AccountBalance,OpenDate,Status);
        try {if(isValied()){
            boolean isUpdated = AccountRepo.update(account);

            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Account updated").show();
                initialize();
                clearFields();
            }
            } else {
                new Alert(Alert.AlertType.WARNING, "Account update failed").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error updating Account: " + e.getMessage()).show();
        }
    }
    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        String nic = txtNic.getText();


            Account account = AccountRepo.searchById(nic);
            if (account != null) {
                txtaccountNo.setText(account.getANo());
                cmbCustomerId.setValue(account.getCId());
                cmbAccountType.setValue(account.getAType());
                txtAccountBalance.setText(account.getABalance());
                txtOpenDate.setValue(LocalDate.parse(account.getOpenDate()));
                cmbStatus.setValue(account.getStatus());

            } else {
                String customerId = LoanRepo.getCustId(nic);
                if (customerId != null) {
                    cmbCustomerId.setValue(customerId);
                    new Alert(Alert.AlertType.INFORMATION, "Customer ID is found").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Customer not found for NIC: " + nic).show();
                }
            }
    }
   /* private void setCustomerData(Account account) {
        if (account != null) {
            txtaccountNo.setText(account.getANo());
            cmbCustomerId.setValue(account.getCId());
            cmbAccountType.setValue(account.getAType());
            txtAccountBalance.setText(account.getABalance());
            txtOpenDate.setText(account.getOpenDate());
            cmbStatus.setValue(account.getStatus());

        } else {
            // No loan found, try to find related customer ID
            String customerId = LoanRepo.getCustId(nic);
            if (customerId != null) {
                cmbCustomerId.setValue(customerId);
                new Alert(Alert.AlertType.INFORMATION, "Your Customer ID is found").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Customer not found for NIC: " + nic).show();
            }
        }

        // Reset fields and table





}*/
    public void initialize() throws SQLException {
        getAccountIds();
        initializeComboBoxStatus();
        initializeComboBoxType();
        lastAccountId();
        setDate();
        setCellValueFactory();
        loadAllCustomers();
    }
    @FXML

    public void setCellValueFactory() throws SQLException {
        colaccountNo.setCellValueFactory(new PropertyValueFactory<>("a_no"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("c_id"));
        colAccountType.setCellValueFactory(new PropertyValueFactory<>("a_type"));
        colAccountBalance.setCellValueFactory(new PropertyValueFactory<>("a_balance"));
        colOpenDate.setCellValueFactory(new PropertyValueFactory<>("open_date"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadAllCustomers() {
        ObservableList<AccountTm> obList = FXCollections.observableArrayList();

        try {
            List<Account> accountList = AccountRepo.getAll();
            for (Account account : accountList) {
                AccountTm tm = new AccountTm(
                        account.getANo(),
                        account.getCId(),
                        account.getAType(),
                        account.getABalance(),
                        account.getOpenDate(),
                        account.getStatus()
                );

                obList.add(tm);
            }

            tblAccount.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void txtAccountTypeOnAction(ActionEvent event) {
    }

    public void txtAccountBalanceOnAction(ActionEvent event) {

    }

    public void txtOpenDateOnAction(ActionEvent event) {

    }

    public void txtStatusOnAction(ActionEvent event) {

    }

    public void txtCustomerIDOnAction(ActionEvent event) {

    }

    private void lastAccountId() {
        try {
            String lastId = AccountRepo.getLastAccountId();
            String nextId = generateNextId(lastId);
            txtaccountNo.setText(nextId);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error generating next Account No: " + e.getMessage());
        }
    }

    private String generateNextId(String lastId) {
        String nextId = null;
        try {
            if (lastId != null && !lastId.isEmpty()) {
                int lastNumericId = Integer.parseInt(lastId.substring(1)); // Extract numeric part of the ID
                int nextNumericId = lastNumericId + 1;
                nextId = String.format("A%03d", nextNumericId);
            } else {
                nextId = "A001";
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error generating next Account no: Invalid ID format");
        }
        return nextId;
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }
    private void setDate() {
        LocalDate now = LocalDate.now();
        txtOpenDate.setValue(LocalDate.parse(String.valueOf(now)));
    }
    private void getAccountIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> idList = AccountRepo.getIds();

            for(String id : idList) {
                obList.add(id);
            }

            cmbCustomerId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void txtAccountNoOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.ACCOUNTID,txtaccountNo);
    }

    public void txtOpenDateOnKeyReleased(KeyEvent keyEvent) {
       // Regex.setTextColor(lk.ijse.gdse.Util.TextField.DATE,txtOpenDate);
    }

    public void txtNicOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.NIC,txtNic);
    }
    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.ACCOUNTID,txtaccountNo)) return false;
      //  if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.DATE,txtOpenDate)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.NIC,txtNic)) return false;

        return true;
    }
}