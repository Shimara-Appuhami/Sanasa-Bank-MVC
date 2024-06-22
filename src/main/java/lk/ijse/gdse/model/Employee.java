package lk.ijse.gdse.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private String employeeId;
    private String employeeName;
    private String employeeContact;
    private String employeeAddress;
    private String employeeSalary;
    private String position;
}
