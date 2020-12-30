package com.jbc.demoa.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class doctor {
    private int doctorID;
    private int departmentNo;
    private int daccountNo;
    private String doctorName;
    private String phoneNo;
    private String address;
    private String birthday;
    private String sex;
    private String introduction;
    private String expertise;
    private String achievements;
    private String nationality;
    private String nation;
    private String college;
    private String nativePlace;
    private String works;
    private String evaluation;


    public doctor(int daccountNo, String doctorName, String phoneNo) {
        this.daccountNo = daccountNo;
        this.doctorName = doctorName;
        this.phoneNo = phoneNo;
    }
}
