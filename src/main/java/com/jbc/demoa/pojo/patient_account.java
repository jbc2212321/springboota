package com.jbc.demoa.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class patient_account {
    private int paccountNo;
    private int patientID;
    private String userName;
    private String password;
    private String phoneNo;

    public patient_account(String userName, String password, String phoneNo) {
        this.userName = userName;
        this.password = password;
        this.phoneNo = phoneNo;
    }
}
