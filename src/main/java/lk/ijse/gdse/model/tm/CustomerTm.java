package lk.ijse.gdse.model.tm;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustomerTm {
    private String c_id;
    private String c_name;
    private String c_email;
    private String c_contact;
    private String c_address;
    private String c_age;
    private String date_of_birth;
    private String nic;
    private String registration_date;
    private String annual_income;
}
