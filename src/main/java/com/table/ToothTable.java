package com.table;

import com.encrypty.splitedMatrix;

import java.util.Vector;

public class ToothTable extends FatherTable {
    //血液表实体对象
    public String m_type,m_name,m_date;
    public int m_patientID,m_tableNum;
    public Vector<Double> num;
    public Vector<splitedMatrix> dataIndex;
    public Vector<String> AESString;
    public Vector<String> incluStr;
    public String getM_type(){
        return m_type;
    }
}
