package com;
import com.csvreader.CsvReader;
import  com.table.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.io.*;
public class CSV {

    private static Vector<FatherTable>ReadForBlood(CsvReader reader) throws IOException {
        //从csv文件中读取血液表
        Vector<FatherTable> ftVec=new Vector<>(3);
        String inString = "";
        String tmpString = "";
        ArrayList<String []> List = new ArrayList<String[]>();

        reader.readHeaders();
        while(reader.readRecord()) {
            List.add(reader.getValues());
        }
        reader.close();
        for (int row = 0;row < List.size(); row++) {
            int Length=List.get(row).length;
            BloodTable bt=new BloodTable();
            if(Length > 0){

                bt.m_type="bloodTable";
                bt.m_patientID=Integer.parseInt((List.get(row)[0]));
                bt.m_date=List.get(row)[1];


                bt.num=new Vector<>();
                bt.num.add(Double.parseDouble(List.get(row)[2]));
                bt.num.add(Double.parseDouble(List.get(row)[3]));
                bt.num.add(Double.parseDouble(List.get(row)[4]));

                ftVec.add(bt);

            }
        }
        BloodTable bt=new BloodTable();

        for(int i=0;i<ftVec.size();++i){
            bt= (BloodTable) Secret.Secret(ftVec.elementAt(i));
            DBH.Add("bloodTable",bt.m_patientID,bt.m_date,bt.AESString,bt.dataIndex);
        }
        return  ftVec;
    }


    private static Vector<FatherTable>ReadForTooth(CsvReader reader) throws IOException {
        //从csv文件中读取牙齿表
        Vector<FatherTable> ftVec=new Vector<>(3);
        ArrayList<String []> List = new ArrayList<String[]>();

        reader.readHeaders();
        while(reader.readRecord()) {
            List.add(reader.getValues());
        }
        reader.close();
        for (int row = 0;row < List.size(); row++) {
            int Length=List.get(row).length;
            ToothTable tt=new ToothTable();
            if(Length > 0){
                //for(int i=0;i<Length;i++){
                tt.m_type="toothTable";
                tt.m_patientID=Integer.parseInt((List.get(row)[0]));
                tt.m_date=List.get(row)[1];


                tt.num=new Vector<>();
                tt.num.add(Double.parseDouble(List.get(row)[2]));
                tt.num.add(Double.parseDouble(List.get(row)[3]));
                tt.num.add(Double.parseDouble(List.get(row)[4]));
                ftVec.add(tt);

            }
        }


        return  ftVec;
    }


    public static Vector<FatherTable> csvRead(String filename, String type){
        //从csv文件中分别调用其他表的函数读取表
        Vector<FatherTable> ftVec=new Vector<>(3);
        try {
            File fileIN= new File(filename);
            BufferedReader reader=new BufferedReader(new FileReader(fileIN));
            CsvReader csvReader = new CsvReader(reader);


            if (type == "bloodTable") {
                ftVec = ReadForBlood(csvReader);
                BloodTable bt=new BloodTable();
                for(int i=0;i<ftVec.size();++i){
                    bt= (BloodTable) Secret.Secret(ftVec.elementAt(i));
                    DBH.Add("bloodTable",bt.m_patientID,bt.m_date,bt.AESString,bt.dataIndex);
                }
            }
            if (type == "toothTable") {
                ftVec = ReadForTooth(csvReader);
                ToothTable tt=new ToothTable();
                for(int i=0;i<ftVec.size();++i){
                    tt= (ToothTable) Secret.Secret(ftVec.elementAt(i));
                    DBH.Add("toothTable",tt.m_patientID,tt.m_date,tt.AESString,tt.dataIndex);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  ftVec;
    }
}
