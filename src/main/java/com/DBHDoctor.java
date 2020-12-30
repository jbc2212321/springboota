package com;

import com.DBH;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class DBHDoctor {
    private static Connection Conn;
    private static ArrayList<JSONObject> searchForToothByNoWithDate(int patientID, String startDate, String endDate, int doctorNum) throws SQLException, IOException, ClassNotFoundException {
        //通过病人id进行查询牙齿信息
        ArrayList<JSONObject> joA=new ArrayList<JSONObject>();
        Statement stt;

        String Sql = "SELECT tooth_grant FROM restriction WHERE patientID="+patientID+" AND doctorID="+doctorNum+";";
        stt = Conn.createStatement();
        ResultSet set=null;
        set = stt.executeQuery(Sql);
        while (set.next()) {
            if (set.getInt(1) == 0) {
                return null;
            }
        }
        Sql="select ";
        Sql+="pain,mobility,tartar,date from tooth_routine where patientID="+
                patientID+" and date_format(date,'%Y-%m-%d') between \""+startDate+"\" and \""+endDate+"\" order by date;";
      //  System.out.println(Sql);
        set = stt.executeQuery(Sql);


        // System.out.println(set.getString(1));
        while (set.next()) {
            JSONObject JO=  new JSONObject();
            JO.put("pain",Double.parseDouble(AES.decrypt(set.getString(1))));
            JO.put("mobility",Double.parseDouble(AES.decrypt(set.getString(2))));
            JO.put("tartar",Double.parseDouble(AES.decrypt(set.getString(3))));
            JO.put("date",set.getDate(4).toString());
            joA.add(JO);
        }
        System.out.println(joA);
        return joA;
    }


    private static ArrayList<JSONObject> searchForBloodByNoWithDate(int patientID,String startDate,String endDate,int doctorNum) throws SQLException, IOException, ClassNotFoundException {
        //通过病人id进行查询血液信息
        Statement stt;
        ArrayList<JSONObject> joA=new ArrayList<JSONObject>();

        String Sql = "SELECT blood_grant FROM restriction WHERE patientID="+patientID+" AND doctorID="+doctorNum+";";
        stt = Conn.createStatement();
        ResultSet set=null;
        set = stt.executeQuery(Sql);
        while (set.next()) {
            if (set.getInt(1) == 0) {
                return null;
            }
        }
        Sql="select rbc,wbc,plt,date from blood_routine where patientID="+patientID+
                " and date_format(date,'%Y-%m-%d') between \""+startDate+"\" and \""+endDate+"\" order by date;";
    //    System.out.println(Sql);
        stt = Conn.createStatement();
        set = stt.executeQuery(Sql);


        while (set.next()) {
            JSONObject JO=  new JSONObject();
            JO.put("rbc",Double.parseDouble(AES.decrypt(set.getString(1))));
            JO.put("wbc",Double.parseDouble(AES.decrypt(set.getString(2))));
            JO.put("plt",Double.parseDouble(AES.decrypt(set.getString(3))));
            JO.put("date",set.getDate(4).toString());
            joA.add(JO);
        }
        //System.out.println(joA);
        return joA;
    }
    public static ArrayList<JSONObject> searchByNoWithDate(int patientID, int doctorNum, String type, String startDate, String endDate){
        //调用函数实现按照病人id查询相关指标


        Connection conn=null;
        ResultSet set=null;


        try {
            // 获取连接
            Conn = DBH.getConnection();

            if(type.equals("血液科")){
                return searchForBloodByNoWithDate(patientID,startDate.split("T")[0],endDate.split("T")[0],doctorNum);}
            if(type.equals("口腔科")) {
                return searchForToothByNoWithDate(patientID,startDate.split("T")[0],endDate.split("T")[0],doctorNum);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                set.close();
                conn.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }

        }
        return null;
    }

}