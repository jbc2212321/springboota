package com.jbc.demoa.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class log {
    private int logNo;
    private String userName;
    private String type;
    private String action;
    private String time;
}
