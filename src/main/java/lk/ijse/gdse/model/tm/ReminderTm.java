package lk.ijse.gdse.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReminderTm {
    private String r_id;
    private String nic;
    private String loan_type;
    private String r_message;
    private String r_type;
    private String r_date;
    private String r_status;

}
