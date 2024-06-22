package lk.ijse.gdse.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceTm {
    private String b_id;
    private String loan_id;
    private String principal_balance;
    private String interest_balance;
    private String total_balance;
    private String last_updated_date;
}
