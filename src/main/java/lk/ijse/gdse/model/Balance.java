package lk.ijse.gdse.model;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Balance {
    private String bId;
    private String loanId;
    private String principalBalance;
    private String interestBalance;
    private String totalBalance;
    private String lastUpdatedDate;
}
