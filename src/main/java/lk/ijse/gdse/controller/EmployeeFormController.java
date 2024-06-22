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
import lk.ijse.gdse.model.Customer;
import lk.ijse.gdse.model.Employee;
import lk.ijse.gdse.model.tm.EmployeeTm;
import lk.ijse.gdse.repository.CustomerRepo;
import lk.ijse.gdse.repository.EmployeeRepo;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class EmployeeFormController {

    public JFXButton btnBack;

    public TextField txtPosition;

    @FXML
    private TableColumn<Employee, String> colAddress;

    @FXML
    private TableColumn<Employee, String> colContact;

    @FXML
    private TableColumn<Employee, String> colId;

    @FXML
    private TableColumn<Employee, String> colPosition;

    @FXML
    private TableColumn<Employee, String> colName;

    @FXML
    private TableColumn<Employee, String> colSalary;

    @FXML
    private TableView<EmployeeTm> tblEmployee;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmployeeId;


    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSalary;

    @FXML
    void initialize() throws SQLException {
        lastEmployeeId();
        setCellValueFactories();
        loadEmployees();

    }

    private void setCellValueFactories() {
        colId.setCellValueFactory(new PropertyValueFactory<>("e_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("e_name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("e_contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("e_address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("e_salary"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
    }

    private void loadEmployees() throws SQLException {
        ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();

        try {
            List<Employee> employeeList = EmployeeRepo.getAll();
            for (Employee employee : employeeList) {
                EmployeeTm tm = new EmployeeTm(
                        employee.getEmployeeId(),
                        employee.getEmployeeName(),
                        employee.getEmployeeContact(),
                        employee.getEmployeeAddress(),
                        employee.getEmployeeSalary(),
                        employee.getPosition()

                );

                obList.add(tm);
            }

            tblEmployee.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String eId = txtEmployeeId.getText();
        String eName = txtName.getText();
        String eContact = txtContact.getText();
        String eAddress = txtAddress.getText();
        String eSalary = txtSalary.getText();
        String position = txtPosition.getText();

        Employee employee = new Employee(eId, eName, eContact, eAddress, eSalary, position);
        try {if (isValied()){
            boolean isSaved = EmployeeRepo.save(employee);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Employee saved successfully").show();
                initialize();
                clearFields();

            }
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to save employee").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error saving employee: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String eId = txtEmployeeId.getText();
        String eName = txtName.getText();
        String eContact = txtContact.getText();
        String eAddress = txtAddress.getText();
        String eSalary = txtSalary.getText();
        String position = txtPosition.getText();

        Employee employee = new Employee(eId, eName, eContact, eAddress, eSalary, position);
        try {if (isValied()){
            boolean isUpdated = EmployeeRepo.update(employee);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Employee updated successfully").show();

                initialize();
                clearFields();

            }
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to update employee").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error updating employee: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String eId = txtEmployeeId.getText();
        try {
            boolean isDeleted = EmployeeRepo.delete(eId);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Employee deleted successfully").show();
                initialize();
                clearFields();
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to delete employee").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error deleting employee: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtEmployeeId.clear();
        txtName.clear();
        txtContact.clear();
        txtAddress.clear();
        txtSalary.clear();
        txtPosition.clear();
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        String id = txtEmployeeId.getText();

        try {
            Employee employee = EmployeeRepo.searchById(id);
            setEmployeeData(employee);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error searching Employee: " + e.getMessage()).show();
        }
    }

    private void setEmployeeData(Employee employee) {
        if (employee != null) {
            txtEmployeeId.setText(employee.getEmployeeId());
            txtName.setText(employee.getEmployeeName());
            txtContact.setText(employee.getEmployeeContact());
            txtAddress.setText(employee.getEmployeeAddress());
            txtSalary.setText(employee.getEmployeeSalary());
            txtPosition.setText(employee.getPosition());
        } else {
            System.out.println("Employee data is null.");
        }
    }

    public void txtNameOnAction(ActionEvent event) {
    }

    public void txtContactOnAction(ActionEvent event) {
    }

    public void txtAddressOnAction(ActionEvent event) {
    }

    public void txtSalaryOnAction(ActionEvent event) {
    }



    public void btnBackOnAction(ActionEvent event) throws IOException {

        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard-form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }

    private void lastEmployeeId() {
        try {
            String lastId = EmployeeRepo.getLastEmployeeId();
            String nextId = generateNextId(lastId);
            txtEmployeeId.setText(nextId);
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
                nextId = String.format("E%03d", nextNumericId);
            } else {
                nextId = "E001";
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error generating next ID: Invalid ID format");
        }
        return nextId;
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }

    public void txtSalaryOnKeyReleased(KeyEvent keyEvent) {
         Regex.setTextColor(lk.ijse.gdse.Util.TextField.AMOUNT,txtSalary);
    }

    public void txtContactOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.CONTACT,txtContact);
    }

    public void txtNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.NAME,txtName);
    }

    public void txtIdOnKeyReleased(KeyEvent keyEvent) {

        Regex.setTextColor(lk.ijse.gdse.Util.TextField.EMPLOYEEID,txtEmployeeId);
    }
    public boolean isValied(){
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.AMOUNT,txtSalary)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.CONTACT,txtContact)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.NAME,txtName)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.EMPLOYEEID,txtEmployeeId)) return false;


        return true;
    }

    public void txtPositionOnAction(ActionEvent event) {
    }
}