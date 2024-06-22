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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lk.ijse.gdse.Util.Regex;
import lk.ijse.gdse.db.DbConnection;
import lk.ijse.gdse.model.Customer;


import lk.ijse.gdse.model.tm.CustomerTm;

import lk.ijse.gdse.repository.CustomerRepo;
import lk.ijse.gdse.repository.InquiryRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;


import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CustomerFormController {

    public JFXButton btnBack;
    public JFXComboBox cmbInquiryId;
    public TextField txtNic;
    public TableColumn colNic;
    public DatePicker txtBirth;
    public DatePicker txtRegistration;
    public JFXButton btnPrintBill;
    public JFXButton btnClear;
    public Label imgPhotoOnAction;
    public Rectangle imgPhoto;
    @FXML
    private TableColumn<Customer, String> colAddress;

    @FXML
    private TableColumn<Customer, String> colAge;

    @FXML
    private TableColumn<Customer, String> colBirth;

    @FXML
    private TableColumn<Customer, String> colContact;

    @FXML
    private TableColumn<Customer, String> colEmail;

    @FXML
    private TableColumn<Customer, String> colId;

    @FXML
    private TableColumn<Customer, String> colIncome;


    @FXML
    private TableColumn<Customer, String> colName;

    @FXML
    private TableColumn<Customer, String> colRegistration;

    @FXML
    private TableView<CustomerTm> tblCustomer;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtAge;


    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtIncome;


    @FXML
    private TextField txtName;



    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    //txt dila field clear karanna dunna

    private void clearFields() {
        txtName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtAddress.clear();
        txtAge.clear();
        txtBirth.setValue(null);
        txtNic.clear();
        txtRegistration.setValue(null);
        txtIncome.clear();
    }


    @FXML
    void btnDeleteOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();//id eka dunnama txt ekata dunna text eka get karanawa
        if (!id.isEmpty()) {
            try {
                boolean isDeleted = CustomerRepo.delete(id);//repo ekata gihin quiry eka delete kara ganna method eka hadanawa
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer deleted successfully!").show();
                    initialize();//delete karama table eka update wenna
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete Customer!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Error deleting Customer: " + e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter an Customer ID to delete!").show();
        }
    }


    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String email = txtEmail.getText();
        String contact = txtContact.getText();
        String address = txtAddress.getText();
        String age = txtAge.getText();
        String birth = String.valueOf(txtBirth.getValue());
        String nic=txtNic.getText();
        String registration = String.valueOf(txtRegistration.getValue());
        String income = txtIncome.getText();



        Customer customer = new Customer(id, name, email, contact,address,age,birth,nic,registration,income);//Customer type object ekak create karagatga


        try {if (isValied()){//validation
            boolean isSaved = CustomerRepo.save(customer);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Customer saved successfully").show();
                initialize();
                clearFields();
            }
            } else {
                new Alert(Alert.AlertType.WARNING, "Customer saving failed").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error saving Customer: " + e.getMessage()).show();
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String email = txtEmail.getText();
        String contact = txtContact.getText();
        String address = txtAddress.getText();
        String age = txtAge.getText();
        String birth = String.valueOf(txtBirth.getValue());
        String nic=txtNic.getText();
        String registration = String.valueOf(txtRegistration.getValue());
        String income = txtIncome.getText();



        Customer customer = new Customer(id, name, email, contact,address,age,birth,nic,registration,income);

        try {if (isValied()){
            boolean isUpdated = CustomerRepo.update(customer);

            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Customer updated").show();
                initialize();
                clearFields();
            }
            } else {
                new Alert(Alert.AlertType.WARNING, "Customer update failed").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error updating Customer: " + e.getMessage()).show();
        }
    }
    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        String id = txtId.getText();
        String nic=txtNic.getText();

        try {

            Customer customer = CustomerRepo.searchById(id);
             customer=CustomerRepo.searchByNic(nic);
            setCustomerData(customer);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error searching Customer: " + e.getMessage()).show();
        }
    }

    private void setCustomerData(Customer customer) {
        if (customer != null) {
            txtId.setText(customer.getCId());
            txtName.setText(customer.getCName());
            txtEmail.setText(customer.getCEmail());
            txtContact.setText(customer.getCContact());
            txtAddress.setText(customer.getCAddress());
            txtAge.setText(customer.getCAge());
            txtBirth.setValue(LocalDate.parse(customer.getDateOfBirth()));
            txtNic.setText(customer.getNic());
            txtRegistration.setValue(LocalDate.parse(customer.getRegistrationDate()));
            txtIncome.setText(customer.getAnnualIncome());
        } else {

            System.out.println("Customer data is null.");
        }
    }

    public void initialize() throws SQLException {
        lastCustomerId();
        setDate();
        //getCustomerIds();
        setCellValueFactory();
        loadAllCustomers();
    }
    @FXML

    public void setCellValueFactory() throws SQLException {//table view ekata data add wenawa
        colId.setCellValueFactory(new PropertyValueFactory<>("c_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("c_name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("c_email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("c_contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("c_address"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("c_age"));
        colBirth.setCellValueFactory(new PropertyValueFactory<>("date_of_birth"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colRegistration.setCellValueFactory(new PropertyValueFactory<>("registration_date"));
        colIncome.setCellValueFactory(new PropertyValueFactory<>("annual_income"));


    }

    private void loadAllCustomers() {//observable list ekata data da gannawa
        ObservableList<CustomerTm> obList = FXCollections.observableArrayList();

        try {
            List<Customer> customerList = CustomerRepo.getAll();
            for (Customer customer : customerList) {
                CustomerTm tm = new CustomerTm(
                        customer.getCId(),
                        customer.getCName(),
                        customer.getCEmail(),
                        customer.getCContact(),
                        customer.getCAddress(),
                        customer.getCAge(),
                        customer.getDateOfBirth(),
                        customer.getNic(),
                        customer.getRegistrationDate(),
                        customer.getAnnualIncome()
                );

                obList.add(tm);//Tm type eken
            }

            tblCustomer.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void txtRegistrationOnAction(ActionEvent event) {
        
    }

    public void txtIncomeOnAction(ActionEvent event) {
    }

    public void txtInquiryIdOnAction(ActionEvent event) {
    }

    public void txtAgeOnAction(ActionEvent event) {
    }

    public void txtEmailOnAction(ActionEvent event) {
    }

    public void txtContactOnAction(ActionEvent event) {
    }

    public void txtAddressOnAction(ActionEvent event) {
    }

    public void txtBirthOnAction(ActionEvent event) {

    }

    private void lastCustomerId() {//limit quiry eken last eka gannawa
        try {
            String lastId = CustomerRepo.getLastCustomerId();
            String nextId = generateNextId(lastId);
            txtId.setText(nextId);//eka id field ekata set kara gannawa
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
                nextId = String.format("C%03d", nextNumericId);//genarate wenna oni type eka
            } else {
                nextId = "C001";
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
        txtRegistration.setValue(LocalDate.parse(String.valueOf(now)));//date eka denawa auto
    }
    public void txtNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.NAME,txtName);
    }

    public void txtEmailOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.EMAIL,txtEmail);
    }

    public void txtContactOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.CONTACT,txtContact);
    }

    public void txtAgeOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.AGE,txtAge);
    }



    public void txtIdOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.ID,txtId);
    }


    public void txtNicOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.NIC,txtNic);
    }
    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.ID,txtId)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.NAME,txtName)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.EMAIL,txtEmail)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.CONTACT,txtContact)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.AGE,txtAge)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.NIC,txtNic)) return false;


        return true;
    }

    public void txtRegisterDateOnKeyReleased(KeyEvent keyEvent) {
    }
  /*  private void getCustomerIds() {
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
  public void btnPrintBillOnAction(ActionEvent actionEvent) throws JRException, SQLException {
      JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/customer-report.jrxml");
      JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DbConnection.getInstance().getConnection());
      JasperViewer.viewReport(jasperPrint,false);

  }
}