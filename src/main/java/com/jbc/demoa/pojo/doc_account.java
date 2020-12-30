package com.jbc.demoa.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class doc_account {
    private int daccountNo;
    private int doctorID;
    private String userName;
    private String password;
    private String phoneNo;

    public doc_account(String userName, String password, String phoneNo) {
        this.userName = userName;
        this.password = password;
        this.phoneNo = phoneNo;
    }

    public doc_account(int daccountNo, String userName, String phoneNo) {
        this.daccountNo = daccountNo;
        this.userName = userName;
        this.phoneNo = phoneNo;
    }
}
