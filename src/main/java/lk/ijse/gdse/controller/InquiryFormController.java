package lk.ijse.gdse.controller;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lk.ijse.gdse.Util.Regex;
import lk.ijse.gdse.db.DbConnection;
import lk.ijse.gdse.model.Customer;
import lk.ijse.gdse.model.Inquiry;
import lk.ijse.gdse.model.tm.InquiryTm;
import lk.ijse.gdse.repository.InquiryRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class InquiryFormController {

    public JFXButton btnBack;
    public TextField txtCustomerId;
    public TextField txtNic;
    public TextField txtInquiryId;
    public TableColumn colNic;
    public TableColumn colCustomerId;
    public DatePicker txtResponseDate;
    public JFXButton btnPrintBill;
    public Label imgPhotoOnAction;
    public Rectangle imgPhoto;
    public JFXButton btnClear;
    @FXML
    private TableColumn<Inquiry, String> colInquiryDate;

    @FXML
    private TableColumn<Inquiry, String> colInquiryId;

    @FXML
    private TableColumn<Inquiry, String> colInquiryType;

    @FXML
    private TableColumn<Inquiry, String> colResponseDate;

    @FXML
    private TableView<InquiryTm> tblInquiry;
    @FXML
    private TextField txtId;

    @FXML
    private TextField txtInquiryDate;


    @FXML
    private TextField txtType;
    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtInquiryId.clear();
        txtNic.clear();
        txtCustomerId.clear();
        txtType.clear();
        txtInquiryDate.clear();
        txtResponseDate.setValue(null);



    }

    @FXML
    void btnDeleteOnAction(ActionEvent actionEvent) {
        String inquiryId = txtInquiryId.getText();

        if (!inquiryId.isEmpty()) {
            try {
                boolean isDeleted = InquiryRepo.delete(inquiryId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Inquiry deleted successfully!").show();
                    initialize();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete inquiry!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Error deleting inquiry: " + e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter an Inquiry ID to delete!").show();
        }
    }


    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String inquiryId = txtInquiryId.getText();
        String nic = txtNic.getText();
        String customerId = txtCustomerId.getText();
        String inquiryType = txtType.getText();
        String inquiryDate = txtInquiryDate.getText();
        String responseDate = String.valueOf(txtResponseDate.getValue());



        Inquiry inquiry = new Inquiry(inquiryId, nic, customerId, inquiryType,inquiryDate,responseDate);


        try {if (isValied()){
            boolean isSaved = InquiryRepo.save(inquiry);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Inquiry saved successfully").show();
                initialize();
                clearFields();
            }
            } else {
                new Alert(Alert.AlertType.WARNING, "Inquiry saving failed").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error saving Inquiry: " + e.getMessage()).show();
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String inquiryId = txtInquiryId.getText();
        String nic = txtNic.getText();
        String customerId = txtCustomerId.getText();
        String inquiryType = txtType.getText();
        String inquiryDate = txtInquiryDate.getText();
        String responseDate = String.valueOf(txtResponseDate.getValue());



        Inquiry inquiry = new Inquiry(inquiryId, nic, customerId, inquiryType,inquiryDate,responseDate);

        try {if (isValied()){
            boolean isUpdated = InquiryRepo.update(inquiry);

            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Inquiry updated").show();
                initialize();
                clearFields();
            }
            } else {
                new Alert(Alert.AlertType.WARNING, "Inquiry update failed").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error updating Inquiry: ").show();
        }
    }
    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        String id = txtNic.getText();

        try {
            Inquiry inquiry  = InquiryRepo.searchById(id);
            if (inquiry != null ) {
                txtInquiryId.setText(inquiry.getInId());
                txtCustomerId.setText(inquiry.getCId());
                txtType.setText(inquiry.getInType());
                txtInquiryDate.setText(inquiry.getInDate());
                txtResponseDate.setValue(LocalDate.parse(inquiry.getResponseDate()));

            } else {
                String customerId = InquiryRepo.getCustId(id);
                if (inquiry != null) {
                    txtCustomerId.setText(customerId);
                   // new Alert(Alert.AlertType.INFORMATION, "Your Customer ID is ");
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Your Customer ID is found").show();
                    //new Alert(Alert.AlertType.ERROR, "Customer not found for NIC: " + id).show();
                }
                initialize();

            }
        } catch (SQLException e) {
            //new Alert(Alert.AlertType.ERROR, "Error searching Inquiry or Customer: " + e.getMessage()).show();
        }
    }

    @FXML
    void txtInquiryDateOnAction(ActionEvent event) {

    }

    @FXML
    void txtResponseDateOnAction(ActionEvent event) {

    }

    @FXML
    void txtTypeOnAction(ActionEvent event) {

    }
    public void initialize() throws SQLException {
        handleCustomerIdEntered();
        lastInquiryId();
        setDate();
        setCellValueFactory();
        loadAllCustomers();
    }
    @FXML

    public void setCellValueFactory() throws SQLException {
        colInquiryId.setCellValueFactory(new PropertyValueFactory<>("in_id"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("c_id"));
        colInquiryType.setCellValueFactory(new PropertyValueFactory<>("in_type"));
        colInquiryDate.setCellValueFactory(new PropertyValueFactory<>("in_date"));
        colResponseDate.setCellValueFactory(new PropertyValueFactory<>("response_date"));
    }

    private void loadAllCustomers() {
        ObservableList<InquiryTm> obList = FXCollections.observableArrayList();

        try {
            List<Inquiry> inquiryList = InquiryRepo.getAll();
            for (Inquiry inquiry : inquiryList) {
                InquiryTm tm = new InquiryTm(
                        inquiry.getInId(),
                        inquiry.getNic(),
                        inquiry.getCId(),
                        inquiry.getInType(),
                        inquiry.getInDate(),
                        inquiry.getResponseDate()
                );

                obList.add(tm);
            }

            tblInquiry.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void btnBackOnAction(ActionEvent event) throws IOException {

        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard-form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }
    private void lastInquiryId() {
        try {
            String lastId = InquiryRepo.getLastInquiryId();
            String nextId = generateNextId(lastId);
            txtInquiryId.setText(nextId);
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
                nextId = String.format("I%03d", nextNumericId);
            } else {
                nextId = "I001";
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
        txtInquiryDate.setText(String.valueOf(now));
    }

    public void txtCustomerIdOnAction(ActionEvent event) {

    }

    public void txtInquiryIdOnAction(ActionEvent event) {
    }
    private void handleCustomerIdEntered() {
        try {
            String nic = txtNic.getText();
            if (!nic.isEmpty()) {
                String id = InquiryRepo.searching(nic);
                txtCustomerId.setText(id);

            } else {
                //  new Alert(Alert.AlertType.INFORMATION, "NIC not found!").show();
            }
        } catch (SQLException e) {

            new Alert(Alert.AlertType.ERROR, "Error saving NIC: " + e.getMessage()).show();

            e.printStackTrace();
        }
    }
    public void txtCustIdOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.ID,txtCustomerId);
    }

    public void txtNicOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.NIC,txtNic);
    }

    public void txtIdOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.INQUIRYID,txtId);
    }
    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.ID,txtCustomerId)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.NIC,txtNic)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.INQUIRYID,txtId)) return false;



        return true;
    }

 public void btnPrintBillOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/inquiry-report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);

    }
}