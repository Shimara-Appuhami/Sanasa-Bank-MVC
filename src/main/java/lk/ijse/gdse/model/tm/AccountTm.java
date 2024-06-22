package lk.ijse.gdse.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTm {
    private String a_no;
    private String c_id;
    private String a_type;
    private String a_balance;
    private String open_date;
    private String status;
}
