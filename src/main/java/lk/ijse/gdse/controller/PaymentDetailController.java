package lk.ijse.gdse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse.Util.Regex;
import lk.ijse.gdse.db.DbConnection;
import lk.ijse.gdse.model.Payment;
import lk.ijse.gdse.repository.PaymentRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentDetailController {
    public TableColumn<String, Payment> colInvoice;
    public TableColumn<String, Payment> colLoanId;
    public TableColumn<String, Payment> colLoanType;
    public TableColumn<Double, Payment> colAmount;
    public TableColumn<String, Payment> colPaymentMethod;
    public TableColumn<Double, Payment> colLateFee;
    public TableColumn<String, Payment> colDate;
    public TextField txtNic;
    public TableView<Payment> tblPaymentDetails;
    public AnchorPane rootNode;


    public void initialize() {
        colInvoice.setCellValueFactory(new PropertyValueFactory<>("pInvoiceNo"));
        colLoanId.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        colLoanType.setCellValueFactory(new PropertyValueFactory<>("loanType"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("paymentAmount")); // Assuming pAmount is a DoubleProperty or similar
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colLateFee.setCellValueFactory(new PropertyValueFactory<>("lateFee")); // Assuming lateFee is a DoubleProperty or similar
        colDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate")); // Assuming pDate is a ObjectProperty<LocalDate> or similar
    }


    public void txtNicOnAction(ActionEvent event) {
        String nic = txtNic.getText();
        try {if (isValied()){
            List<Payment> payments = PaymentRepo.getPaymentsByNIC(nic); // Assuming a method like this in PaymentRepo
            if (payments != null) {
                ObservableList<Payment> paymentList = FXCollections.observableArrayList(payments);
                tblPaymentDetails.setItems(paymentList);
            } } else {
                tblPaymentDetails.getItems().clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnPrintBillOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        String nic = txtNic.getText();
        try {
            List<Payment> payments = PaymentRepo.getPaymentsByNIC(nic);
            if (payments != null && !payments.isEmpty()) {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("p_txtNic", nic);

                JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/Blank_A4_1.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, DbConnection.getInstance().getConnection());
                JasperViewer.viewReport(jasperPrint, false);
            } else {
            }
        } catch (SQLException | JRException e) {
            e.printStackTrace();
        }
    }
    public void txtNicOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.gdse.Util.TextField.NIC, txtNic);
    }

    public boolean isValied() {
        if (!Regex.setTextColor(lk.ijse.gdse.Util.TextField.NIC, txtNic)) return false;



        return true;
    }


}