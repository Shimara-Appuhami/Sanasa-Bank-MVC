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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.gdse.Util.Regex;
import lk.ijse.gdse.model.Customer;
import lk.ijse.gdse.model.Reminder;
import lk.ijse.gdse.model.tm.ReminderTm;
import lk.ijse.gdse.repository.CustomerRepo;
import lk.ijse.gdse.repository.InquiryRepo;
import lk.ijse.gdse.repository.PaymentRepo;
import lk.ijse.gdse.repository.ReminderRepo;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReminderFormController {


    public JFXComboBox<String> cmbType;
    public JFXComboBox<String> cmbStatus;
    public TextField txtLoanType;
    public TextField txtNic;
    public TableColumn colNic;
    public TableColumn colLoanType;
    public DatePicker txtDate;
    public TextField txtEmail;
    @FXML
    private JFXButton btnBack;

    @FXML
    private TableColumn<Reminder, String> colDate;

    @FXML
    private TableColumn<Reminder, String> colInvoiceNo;

    @FXML
    private TableColumn<Reminder, String> colMessage;

    @FXML
    private TableColumn<Reminder, String> colReminderId;

    @FXML
    private TableColumn<Reminder, String> colStatus;

    @FXML
    private TableColumn<Reminder, String> colType;

    @FXML
    private TableView<ReminderTm> tblReminder;

    @FXML
    private TextField txtMessage;

    @FXML
    private TextField txtReminderId;


    private void initializeComboBoxType() {
        ObservableList<String> type = FXCollections.observableArrayList(
                "Loan payment reminder",
                "overdue notice"

        );
        cmbType.setItems(type);
    }
    private void initializeComboBoxStatus() {
        ObservableList<String> status = FXCollections.observableArrayList(
                "Email",
                "Letter"

        );
        cmbStatus.setItems(status);
    }
    @FXML
    void initialize() {
        initializeComboBoxStatus();
        initializeComboBoxType();
        //getPaymentIds();
        lastReminderId();
        setDate();
        setCellValueFactory();
        loadAllReminders();
    }

    private void setCellValueFactory() {


        colReminderId.setCellValueFactory(new PropertyValueFactory<>("r_id"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colLoanType.setCellValueFactory(new PropertyValueFactory<>("loan_type"));
        colMessage.setCellValueFactory(new PropertyValueFactory<>("r_message"));
        colType.setCellValueFactory(new PropertyValueFactory<>("r_type"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("r_date"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("r_status"));

    }

    private void loadAllReminders() {
        ObservableList<ReminderTm> obList = FXCollections.observableArrayList();

        try {
            List<Reminder> reminderList = ReminderRepo.getAll();
            for (Reminder reminder : reminderList) {
                ReminderTm tm = new ReminderTm(
                        reminder.getRId(),
                        reminder.getNic(),
                        reminder.getLoanType(),
                        reminder.getRMessage(),
                        reminder.getRType(),
                        reminder.getRDate(),
                        reminder.getRStatus()
                );

                obList.add(tm);
            }

            tblReminder.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void txtNicOnAction(ActionEvent event) throws SQLException {
        String nic = txtNic.getText();


        Reminder reminder = ReminderRepo.searchById(nic);
        if (reminder != null) {
//              txtReminderId.setText(reminder.getRId());
//            txtNic.setText(reminder.getNic());
            txtLoanType.setText(reminder.getLoanType());
//            txtMessage.setText(reminder.getRMessage());
//            cmbType.setValue(reminder.getRType());
//            txtDate.setValue(LocalDate.parse(reminder.getRDate()));
//            cmbStatus.setValue(reminder.getRStatus());
            txtEmail.setText(reminder.getEmail());

        } else {
            String type = ReminderRepo.getLoanType(nic);
            String email=ReminderRepo.getEmail(nic);

            if (type != null) {
                txtLoanType.setText(type);
                System.out.println("type ok");
                txtEmail.setText(email);
                System.out.println("email ok");

                new Alert(Alert.AlertType.INFORMATION, "Loan Type and email found").show();
            } else {
                txtLoanType.setText(null);

                new Alert(Alert.AlertType.ERROR, "Loan Type not found for NIC: " + nic).show();
            }
        }
    }
    private void setCustomerData(Reminder reminder) {
        if (reminder != null) {
            txtReminderId.setText(reminder.getRId());
            txtNic.setText(reminder.getNic());
            cmbType.setValue(reminder.getLoanType());
            txtMessage.setText(reminder.getRMessage());
            cmbType.setValue(reminder.getRType());
            txtDate.setValue(LocalDate.parse(reminder.getRDate()));
            cmbStatus.setValue(reminder.getRStatus());

        } else {
            System.out.println("Reminder data is null.");
        }
    }
    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtReminderId.getText();
        String nic = txtNic.getText();
        String loanType = txtLoanType.getText();
        String message = txtMessage.getText();
        String type = String.valueOf(cmbType.getValue());
        String date = String.valueOf(txtDate.getValue());
        String status = cmbStatus.getValue();



        Reminder reminder = new Reminder(id,nic,loanType, message, type, date, status);

        try {if (isValied()){
            boolean isSaved = ReminderRepo.save(reminder);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Reminder saved successfully").show();
                initialize();
                clearFields();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Failed to save reminder").show();
        }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error saving reminder: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtReminderId.getText();
        String nic = txtNic.getText();
        String loanType = txtLoanType.getText();
        String message = txtMessage.getText();
        String type = String.valueOf(cmbType.getValue());
        String date = String.valueOf(txtDate.getValue());
        String status = cmbStatus.getValue();


        Reminder reminder = new Reminder(id,nic,loanType, message, type, date, status);

        try {if (isValied()){
            boolean isUpdated = ReminderRepo.update(reminder);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Reminder updated successfully").show();
                initialize();
                clearFields();
            }  } else {
            new Alert(Alert.AlertType.WARNING, "Failed to update reminder").show();
        }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error updating reminder: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtReminderId.getText();

        try {
            boolean isDeleted = ReminderRepo.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Reminder deleted successfully").show();
                initialize();
                clearFields();
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to delete reminder").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error deleting reminder: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtReminderId.clear();
        txtNic.clear();
        txtLoanType.clear();
        txtMessage.clear();
        cmbType.setValue(null);
        txtDate.setValue(null);
        cmbStatus.setValue(null);
    }


    public void txtTypeOnAction(ActionEvent event) {
    }

    public void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard-form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) btnBack.getScene().getWindow(); // Use any control's scene
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }


    public void txtDateOnAction(ActionEvent event) {
    }


    public void txtStatusOnAction(ActionEvent event) {
    }

    private void lastReminderId() {
        try {
            String lastId = ReminderRepo.getLastReminderId();
            String nextId = generateNextId(lastId);
            txtReminderId.setText(nextId);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error generating next ID: " + e.getMessage());
        }
    }

    private String generateNextId(String lastId) {
        String nextId = null;
        try {
            if (lastId != null && !lastId.isEmpty()) {
                int lastNumericId = Integer.parseInt(lastId.substring(1));
                int nextNumericId = lastNumericId + 1;
                nextId = String.format("R%03d", nextNumericId);
            } else {
                nextId = "R001";
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error generating next ID: Invalid ID format");
        }
        return nextId;
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }
    private void setDate() {
        LocalDate now = LocalDate.now();
        txtDate.setValue(LocalDate.parse(String.valueOf(now)));
    }
   /* private void getPaymentIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> idList = ReminderRepo.getIds();

            for(String id : idList) {
                obList.add(id);
            }

            cmbInvoiceNo.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/

    public void txtLoanTypeOnAction(ActionEvent event) {
    }



    @FXML
    void btnSendOnAction(ActionEvent event) {
        try {
            String recipientEmail = txtEmail.getText();
            String subject = cmbType.getValue();
            String body = txtMessage.getText();
            if (cmbStatus.getValue() != null && cmbStatus.getValue().equals("Email")) {
                String encodedEmailBody = URLEncoder.encode(body, "UTF-8");
                String encodedSubject = URLEncoder.encode(subject, "UTF-8");
                String encodedRecipientEmail = URLEncoder.encode(recipientEmail, "UTF-8");

                String url = "https://mail.google.com/mail/?view=cm&fs=1&to=" + encodedRecipientEmail +
                        "&body=" + encodedEmailBody +
                        "&su=" + encodedSubject;

                Desktop.getDesktop().browse(new URI(url));
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select 'Email' as the status to send an email.").show();
            }
        } catch (IOException | URISyntaxException e) {
            System.out.println("An error occurred: " + e.getLocalizedMessage());
        }
    }


    public void txtNicOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.NIC,txtNic);
    }

    public void txtIdOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.REMINDERID,txtReminderId);
    }
    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.NIC,txtNic)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.REMINDERID,txtReminderId)) return false;

        return true;
    }

    public void txtMessageOnAction(ActionEvent event) {

    }

    public void txtSearchOnAction(ActionEvent event) {
    }

    public void txtEmailOnAction(ActionEvent event){
    }
}