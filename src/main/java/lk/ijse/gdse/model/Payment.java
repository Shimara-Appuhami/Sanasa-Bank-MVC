package lk.ijse.gdse.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private String pInvoiceNo;
    private String nic;
    private String loanId;
    private String rateId;
    private String paymentMethod;
    private String paymentAmount;
    private String paymentDate;
    private String loanType;
    private String lateFee;


    public Payment(String loanType, String rateId) {
    }
}
