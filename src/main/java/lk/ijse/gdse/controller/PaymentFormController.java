package lk.ijse.gdse.controller;

import com.ctc.wstx.shaded.msv_core.grammar.BinaryExp;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import lk.ijse.gdse.Util.Regex;
import lk.ijse.gdse.model.Payment;
import lk.ijse.gdse.model.tm.PaymentTm;
import lk.ijse.gdse.repository.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PaymentFormController {
    public JFXComboBox<String> cmbPaymentMethod;

    public TextField txtNic;
    public TableColumn colNic;
    public JFXComboBox cmbLoanType;
    public TableColumn colLoanType;
    public DatePicker txtDate;
    public TextField cmbRateId;
    public TextField cmbLoanId;
    public JFXButton btnPrint;
    public AnchorPane root;
    public JFXButton btnClear;
    @FXML
    private JFXButton btnBack;

    @FXML
    private TableColumn<Payment, String> colAmount;

    @FXML
    private TableColumn<Payment, String> colDate;

    @FXML
    private TableColumn<Payment, String> colInvoiceNo;

    @FXML
    private TableColumn<Payment, String> colLateFee;

    @FXML
    private TableColumn<Payment, String> colLoanId;

    @FXML
    private TableColumn<Payment, String> colPaymentMethod;

    @FXML
    private TableColumn<Payment, String> colRateId;


    @FXML
    private TextField txtAmount;
    @FXML
    private TextField txtInvoiceNo;
    @FXML
    private TextField txtLateFee;

    @FXML
    private TextField txtLoanId;

    @FXML
    private TextField txtPaymentMethod;

    @FXML
    private TextField txtRateId;


    @FXML
    private TableView<PaymentTm> tblPayment;



    private void initializeComboBox() {
        ObservableList<String> paymentMethod = FXCollections.observableArrayList(
                "Cash",
                "Card",
                "Online"
        );
        cmbPaymentMethod.setItems(paymentMethod);
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
        txtInvoiceNo.clear();
        txtNic.clear();
        cmbLoanId.clear();
        cmbRateId.clear();
        cmbPaymentMethod.setValue(null);
        txtAmount.clear();
        txtDate.setValue(null);
        cmbLoanType.setValue(null);
        txtLateFee.clear();


    }

    public void initialize() throws SQLException {
        initializeComboBoxLoanType();
        //handleCustomerIdEntered();
        //initializeComboBoxStatus();
        initializeComboBox();
        lastPaymentId();
        setDate();
        setCellValueFactory();
        loadPayments();
    }

    @FXML
    void setCellValueFactory() {

        colInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("p_invoice_no"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colLoanId.setCellValueFactory(new PropertyValueFactory<>("loan_id"));
        colRateId.setCellValueFactory(new PropertyValueFactory<>("rate_id"));
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("p_method"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("p_amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("p_date"));
        colLoanType.setCellValueFactory(new PropertyValueFactory<>("loan_type"));
        colLateFee.setCellValueFactory(new PropertyValueFactory<>("late_fee"));

    }

    private void loadPayments() {
        ObservableList<PaymentTm> obList = FXCollections.observableArrayList();

        try {
            List<Payment> paymentList = PaymentRepo.getAll();
            for (Payment payment : paymentList) {
                PaymentTm tm = new PaymentTm(
                        payment.getPInvoiceNo(),
                        payment.getNic(),
                        payment.getLoanId(),
                        payment.getRateId(),
                        payment.getPaymentMethod(),
                        payment.getPaymentAmount(),
                        payment.getPaymentDate(),
                        payment.getLoanType(),
                        payment.getLateFee()
                );

                obList.add(tm);
            }

            tblPayment.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        try {
            String invoiceNo = txtInvoiceNo.getText();
            String nic = txtNic.getText();
            String loanId = String.valueOf(cmbLoanId.getText());
            String rateId = String.valueOf(cmbRateId.getText());
            String paymentMethod = String.valueOf(cmbPaymentMethod.getValue());
            String paymentAmount = txtAmount.getText();
            String paymentDate = String.valueOf(txtDate.getValue());
            String loanType = String.valueOf(cmbLoanType.getValue());
            String lateFee = txtLateFee.getText();

            Payment payment = new Payment(invoiceNo, nic, loanId, rateId, paymentMethod, paymentAmount, paymentDate, loanType, lateFee);
            if (isValied()) {
                boolean isSaved = PaymentRepo.savePayment(payment);

                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Payment saved successfully").show();
                    initialize();
                    clearFields();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save payment").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error saving payment: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            String invoiceNo = txtInvoiceNo.getText();
            String nic = txtNic.getText();
            String loanId = String.valueOf(cmbLoanId.getText());
            String rateId = String.valueOf(cmbRateId.getText());
            String paymentMethod = cmbPaymentMethod.getValue();
            String paymentAmount = txtAmount.getText();
            String paymentDate = String.valueOf(txtDate.getValue());
            String loanType = String.valueOf(cmbLoanType.getValue());
            String lateFee = txtLateFee.getText();

            Payment payment = new Payment(invoiceNo, nic, loanId, rateId, paymentMethod, paymentAmount, paymentDate, loanType, lateFee);
            if (isValied()) {
                boolean isUpdated = PaymentRepo.update(payment);

                if (isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Payment updated successfully").show();
                    initialize();
                    clearFields();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update payment").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error updating payment: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        try {
            String invoiceNo = txtInvoiceNo.getText();
            boolean isDeleted = PaymentRepo.delete(invoiceNo);

            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Payment deleted successfully").show();
                initialize();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete payment").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error deleting payment: " + e.getMessage()).show();
        }
    }

    public void txtAmountOnAction(ActionEvent event) {
    }

    public void txtDateOnAction(ActionEvent event) {
    }


    public void txtLateFeeOnAction(ActionEvent event) {
    }


    public void btnBackOnAction(ActionEvent event) throws IOException {

        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard-form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) throws SQLException {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.NIC, txtNic);
        String nic = txtNic.getText();

        Payment payment = PaymentRepo.searchById(nic);
        if (payment != null) {
            txtInvoiceNo.setText(payment.getPInvoiceNo());
            txtNic.setText(payment.getNic());
            cmbLoanId.setText(payment.getLoanId());
            cmbRateId.setText(payment.getRateId());
            cmbPaymentMethod.setValue(payment.getPaymentMethod());
            txtAmount.setText(payment.getPaymentAmount());
            txtDate.setValue(LocalDate.parse(payment.getPaymentDate()));
            cmbLoanType.setValue(payment.getLoanType());

            calculateLateFee(payment);

        } else {
            cmbLoanType.setValue(null);
            cmbRateId.setText("null");
            cmbLoanId.setText("null");
            new Alert(Alert.AlertType.ERROR, "Loan details not found for NIC: " + nic).show();
        }
    }

    private void calculateLateFee(Payment payment) {
        LocalDate paymentDate = txtDate.getValue();
        String nic = txtNic.getText();

        try {
            String loanId = PaymentRepo.getLoanIdByNIC(nic);
            if (loanId != null) {
                LocalDate loanDate = PaymentRepo.getLoanDate(loanId);

                if (paymentDate != null && loanDate != null) {
                    int monthsSinceLoan = calculateMonthsSinceLoan(loanDate, paymentDate);
                    LocalDate dueDate = loanDate.plusMonths(monthsSinceLoan);

                    if (paymentDate.isAfter(dueDate.plusDays(7))) {
                        txtLateFee.setText("200.00");
                        System.out.println("Late fee calculated: 200.00");
                    } else {
                        txtLateFee.setText("0.00");
                    }
                } else {
                    txtLateFee.setText("Error: Invalid dates");
                }
            } else {
                txtLateFee.setText("Error: Loan ID not found for NIC");
            }
        } catch (SQLException e) {
            txtLateFee.setText("Error fetching loan details");
            e.printStackTrace();
        }
    }

    private int calculateMonthsSinceLoan(LocalDate loanDate, LocalDate paymentDate) {
        return Period.between(loanDate.withDayOfMonth(1), paymentDate.withDayOfMonth(1)).getMonths();
    }







    /*  private void setPaymentData(Payment payment) {
          if (payment != null) {
              txtInvoiceNo.setText(payment.getPInvoiceNo());
              txtNic.setText(payment.getNic());
              cmbLoanId.setValue(payment.getLoanId());
              cmbRateId.setValue(payment.getRateId());
              cmbPaymentMethod.setValue(payment.getPaymentMethod());
              txtAmount.setText(payment.getPaymentAmount());
              txtDate.setValue(LocalDate.parse(payment.getPaymentDate()));
              cmbLoanType.setValue(payment.getLoanType());
              txtLateFee.setText(payment.getLateFee());

          } else {

              System.out.println("Payment data is null.");
          }
      }
  */

    public void txtPaymentMethodOnAction(ActionEvent event) {
    }

    private void lastPaymentId() {
        try {
            String lastId = PaymentRepo.getLastPaymentId();
            String nextId = generateNextId(lastId);
            txtInvoiceNo.setText(nextId);
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
                nextId = String.format("P%03d", nextNumericId);
            } else {
                nextId = "P001";
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



    public void cmbLoanTypeOnAction(ActionEvent event) {
    }

    public void txtLateFeeOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.AMOUNT,txtLateFee);
    }

    public void txtAmountOnKeyReleased(KeyEvent keyEvent) {
          Regex.setTextColor(lk.ijse.gdse.Util.TextField.AMOUNT,txtAmount);
    }

    public void txtIdOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.PAYMENTID,txtInvoiceNo);
    }
    public void txtNicOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.NIC,txtNic);
    }
    public boolean isValied(){
         if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.AMOUNT,txtLateFee)) return false;
     if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.NIC,txtNic)) return false;
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.PAYMENTID,txtInvoiceNo)) return false;
        if(!Regex.setTextColor(lk.ijse.gdse.Util.TextField.AMOUNT,txtAmount))return false;


        return true;
    }

    public void cmbLoanIdOnAction(ActionEvent event) {
    }

    public void cmbRateIdOnAction(ActionEvent event) {
    }

    public void btnPrintOnAction(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(getClass().getResource("/view/paymentDetail.fxml"));
        root.getChildren().clear();
        root.getChildren().add(rootNode);
    }
}