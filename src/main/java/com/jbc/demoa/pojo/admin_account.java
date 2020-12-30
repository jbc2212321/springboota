package com.jbc.demoa.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class admin_account {
    private int accountID;
    private String userName;
    private String password;
    private String phoneNo;

    public admin_account(String userName, String password, String phoneNo) {
        this.userName = userName;
        this.password = password;
        this.phoneNo = phoneNo;
    }
}
