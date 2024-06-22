package lk.ijse.gdse.model;


import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CustomerLoan {
    private String loanId;
    private String customerId;


}
