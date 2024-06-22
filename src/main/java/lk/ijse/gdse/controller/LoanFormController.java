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
import lk.ijse.gdse.db.DbConnection;
import lk.ijse.gdse.model.Customer;
import lk.ijse.gdse.model.CustomerLoan;
import lk.ijse.gdse.model.Loan;
import lk.ijse.gdse.model.tm.CartTm;
import lk.ijse.gdse.model.tm.InterestRateTm;
import lk.ijse.gdse.model.tm.LoanTm;

import lk.ijse.gdse.repository.CustomerLoanRepo;
import lk.ijse.gdse.repository.InquiryRepo;
import lk.ijse.gdse.repository.LoanRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanFormController {

    public JFXButton btnBack;
    public TextField txtLoanTerm;
    public TextField txtollateral;
    public TextField txtpurpose;
    public JFXComboBox cmbLoanType;
    public TableColumn colLoanTerm;
    public TableColumn colCollateral;
    public TableColumn colPurpose;
    public TextField txtCustomerId;
    public TextField txtNic;
    public TextField txtPercentage;
    public TableColumn colPercentage;
    public DatePicker txtDate;
    public JFXButton btnPrint;
    @FXML
    private TableColumn<Loan, String> colApplication;

    @FXML
    private TableColumn<Loan, String> colLoanAmount;

    @FXML
    private TableColumn<Loan, String> colLoanId;

    @FXML
    private TableColumn<Loan, String> colLoanType;

    @FXML
    private TableView<LoanTm> tblLoan;


    @FXML
    private TextField txtLoanAmount;

    @FXML
    private TextField txtLoanId;


    @FXML
    private JFXComboBox<String> cmbApplicationType;
    private final ObservableList<CartTm> obList = FXCollections.observableArrayList();


    private void initializeComboBox() {
        ObservableList<String> applicationTypes = FXCollections.observableArrayList(
                "Approved",
                "Pending",
                "Disbursed"
        );
        cmbApplicationType.setItems(applicationTypes);
    }

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
        cmbApplicationType.setValue(null);
        txtLoanAmount.clear();
        txtLoanId.clear();
        cmbLoanType.setValue(null);
        txtLoanTerm.clear();
        txtollateral.clear();
        txtpurpose.clear();
        txtCustomerId.clear();
        txtPercentage.clear();
        txtNic.clear();
        txtDate.setValue(null);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
        String loanId = txtLoanId.getText();

        if (!loanId.isEmpty()) {
            try {
                boolean isDeleted = LoanRepo.delete(loanId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Loan deleted successfully!").show();
                    initialize();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete loan!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Error deleting loan: ").show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter a Loan ID to delete!").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {

        String loanId = txtLoanId.getText();
        String application = cmbApplicationType.getValue();
        String loanAmount = txtLoanAmount.getText();
        String loanType = String.valueOf(cmbLoanType.getValue());
        String loanTerm = txtLoanTerm.getText();
        String collateral = txtollateral.getText();
        String purpose = txtpurpose.getText();
        String customerId = txtCustomerId.getText();
        String percentage = txtPercentage.getText();
        String nic = txtNic.getText();
        String date= String.valueOf(txtDate.getValue());


        Loan loan = new Loan();
        loan.setLoanId(loanId);
        loan.setApplication(application);
        loan.setLoanAmount(loanAmount);
        loan.setLoanType(loanType);
        loan.setLoanTerm(loanTerm);
        loan.setCollateral(collateral);
        loan.setPurpose(purpose);
        loan.setCustomerId(customerId);
        loan.setPercentage(Double.parseDouble(percentage));
        loan.setNic(nic);
        loan.setDate(date);

        try {
            DbConnection.getInstance().getConnection().setAutoCommit(false);
            if (isValied()) {
                boolean isLoanSaved = LoanRepo.save(loan);
                if (isLoanSaved) {
                    CustomerLoan customerLoan = new CustomerLoan(loanId, customerId);

                    List<CustomerLoan> customerLoanList = new ArrayList<>();
                    customerLoanList.add(customerLoan);

                    CustomerLoanRepo customerLoanRepo = new CustomerLoanRepo();
                    boolean isSaved = customerLoanRepo.saveCustomerLoan(customerLoanList);

                    if (isSaved) {
                        new Alert(Alert.AlertType.INFORMATION, "Customer loan details Transaction saved successfully.").show();
                        DbConnection.getInstance().getConnection().setAutoCommit(true);
                    }
                } else {
                    System.out.println("Failed to save customer loan details.");
                    DbConnection.getInstance().getConnection().rollback();
                }
                new Alert(Alert.AlertType.CONFIRMATION, "Loan saved successfully").show();
                initialize();
                clearFields();
            } else {
                new Alert(Alert.AlertType.WARNING, "Loan saving failed").show();
                DbConnection.getInstance().getConnection().rollback();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error saving loan: " ).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String loanId = txtLoanId.getText();
        String application = cmbApplicationType.getValue();
        String loanAmount = txtLoanAmount.getText();
        String loanType = String.valueOf(cmbLoanType.getValue());
        String loanTerm = txtLoanTerm.getText();
        String collateral = txtollateral.getText();
        String purpose = txtpurpose.getText();
        String customerId = txtCustomerId.getText();
        String nic = txtNic.getText();
        String date = String.valueOf(txtDate.getValue());

        try {
            String percentage = txtPercentage.getText();
                         Loan loan = new Loan(loanId, application, loanAmount, loanType, loanTerm, collateral, purpose, customerId, percentage, nic,date);
                boolean isUpdated = LoanRepo.update(loan);

                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Loan updated successfully").show();
                    initialize();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Loan update failed").show();
                }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid percentage format").show();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error updating loan: ").show();
        }
    }

    public void initialize() {
        setDate();
        initializeComboBoxLoanType();
        initializeComboBox();
        lastLoanId();
        setCellValueFactory();
        loadAllLoans();
    }

    public void setCellValueFactory() {
        colLoanId.setCellValueFactory(new PropertyValueFactory<>("loan_id"));
        colApplication.setCellValueFactory(new PropertyValueFactory<>("application"));
        colLoanType.setCellValueFactory(new PropertyValueFactory<>("loan_type"));
        colLoanAmount.setCellValueFactory(new PropertyValueFactory<>("loan_amount"));
        colLoanTerm.setCellValueFactory(new PropertyValueFactory<>("loan_term"));
        colCollateral.setCellValueFactory(new PropertyValueFactory<>("collateral"));
        colPurpose.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        colPercentage.setCellValueFactory(new PropertyValueFactory<>("percentage"));



    }

    private void loadAllLoans() {
        try {
            List<LoanTm> loanList = LoanRepo.getAll();
            tblLoan.getItems().clear();
            for (LoanTm loan : loanList) {
                LoanTm tm = new LoanTm(loan.getLoan_id(), loan.getApplication(), loan.getLoan_amount(), loan.getLoan_type(), loan.getLoan_term(), loan.getCollateral(), loan.getPurpose(), loan.getC_id(), loan.getPercentage(), loan.getNic(),loan.getDate());
                tblLoan.getItems().add(tm);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading loans: " ).show();
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.NIC, txtNic);
        String nic = txtNic.getText();


        LoanTm loan = LoanRepo.searchById(nic);
        if (loan != null) {

            txtLoanId.setText(loan.getLoan_id());
            txtCustomerId.setText(loan.getC_id());
            cmbApplicationType.setValue(loan.getApplication());
            txtLoanAmount.setText(loan.getLoan_amount());
            cmbLoanType.setValue(loan.getLoan_type());
            txtLoanTerm.setText(loan.getLoan_term());
            txtollateral.setText(loan.getCollateral());
            txtpurpose.setText(loan.getPurpose());
            txtPercentage.setText(String.valueOf(loan.getPercentage()));
            txtNic.setText(loan.getNic());
            txtDate.setValue(LocalDate.parse(loan.getDate()));


        } else {
            String customerId = LoanRepo.getCustId(nic);
            if (customerId != null) {
                txtCustomerId.setText(customerId);
                new Alert(Alert.AlertType.INFORMATION, "Your Customer ID is found").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Customer not found for NIC: " + nic).show();
            }
        }





}


   /* private void setLoanData(Loan loan) {
        if (loan != null) {
            txtLoanId.setText(loan.getLoanId());
            cmbApplicationType.setValue(loan.getApplication());
            txtLoanAmount.setText(loan.getLoanAmount());
            cmbLoanType.setValue(loan.getLoanType());
            txtLoanTerm.setText(loan.getLoanTerm());
            txtollateral.setText(loan.getCollateral());
            txtpurpose.setText(loan.getPurpose());
            txtCustomerId.setText(loan.getCustomerId());
            txtPercentage.setText(String.valueOf(loan.getPercentage()));
            txtNic.setText(loan.getNic());
        } else {
            // Handle case where loan is null (not found or other error)
            System.out.println("Loan data is null.");
        }
    }
*/
    public void txtApplicationOnAction(ActionEvent event) {
    }

    public void txtLoanAmountOnAction(ActionEvent event) {
    }


    public void btnBackOnAction(ActionEvent event) throws IOException {

        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard-form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) btnBack.getScene().getWindow(); // Use any control's scene
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }

    private void lastLoanId() {
        try {
            String lastId = LoanRepo.getLastLoanId();
            String nextId = generateNextId(lastId);
            txtLoanId.setText(nextId);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error generating next ID: ");
        }
    }

    private String generateNextId(String lastId) {
        String nextId = null;
        try {
            if (lastId != null && !lastId.isEmpty()) {
                int lastNumericId = Integer.parseInt(lastId.substring(1));
                int nextNumericId = lastNumericId + 1;
                nextId = String.format("L%03d", nextNumericId);
            } else {
                nextId = "L001";
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error generating next ID: Invalid ID format");
        }
        return nextId;
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        new Alert(alertType, message).show();
    }

    public void txtLoanTermOnAction(ActionEvent event) {
    }

    public void txtollateralOnAction(ActionEvent event) {
    }

    public void txtpurposeOnAction(ActionEvent event) {
    }

    public void cmbLoanTypeOnAction(ActionEvent event) {
        String type = (String) cmbLoanType.getValue();
        InterestRateTm loan = LoanRepo.searchByLoanType(type);
        if (loan != null) {

//            txtLoanId.setText(loan.getLoan_id());
//            txtCustomerId.setText(loan.getC_id());
//            cmbApplicationType.setValue(loan.getApplication());
//            txtLoanAmount.setText(loan.getLoan_amount());
//            cmbLoanType.setValue(loan.getLoan_type());
//            txtLoanTerm.setText(loan.getLoan_term());
//            txtollateral.setText(loan.getCollateral());
//            txtpurpose.setText(loan.getPurpose());
            txtPercentage.setText(String.valueOf(loan.getPercentage()));

        } else {
           // showAlert(Alert.AlertType.WARNING, "Loan Type not found for Loan: " + type);
        }


    }

    public void txtCustomerIdOnAction(ActionEvent event) throws SQLException {

    }
    public void txtNicOnKeyReleased(ActionEvent event) throws SQLException {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.NIC, txtNic);

    }

    public void txtTermOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.TERM, txtLoanTerm);
    }

    public void txtColatrellalOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.NAME, txtollateral);
    }

    public void txtIdOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.LOANID, txtLoanId);
    }

    public void txtAmountOnKeyReleased(KeyEvent keyEvent) {
         Regex.setTextColor(lk.ijse.gdse.Util.TextField.AMOUNT,txtLoanAmount);
    }

    public boolean isValied() {
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.TERM, txtLoanTerm)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.NAME, txtollateral)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.LOANID, txtLoanId)) return false;
          if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.AMOUNT,txtLoanAmount)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.NIC, txtNic)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.PERCENTAGE, txtPercentage)) return false;




        return true;
    }

    public void txtPercentageOnAction(ActionEvent event) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.PERCENTAGE, txtPercentage);
    }

    public void txtDateOnAction(ActionEvent event) {
    }
    private void setDate() {
        LocalDate now = LocalDate.now();
        txtDate.setValue(LocalDate.parse(String.valueOf(now)));
    }

    public void btnPrintOnAction(ActionEvent event) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/loan-report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }
}