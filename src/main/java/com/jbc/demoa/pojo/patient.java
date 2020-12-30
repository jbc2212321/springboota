package com.jbc.demoa.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class patient {
    private int patientID;
    private int paccountNo;
    private String patientName;
    private String phoneNo;
    private String birthday;
    private String sex;

    public patient(String patientName, String phoneNo) {
        this.patientName = patientName;
        this.phoneNo = phoneNo;
    }
}
