package com;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Log {
    private static Connection Conn;

    private static String getTime() {
        java.util.Date a = new java.util.Date();
        java.sql.Timestamp d = new java.sql.Timestamp(a.getTime());
        return d.toString();
    }

    public static void logIn(int id, String type) {
        DBH.AddLog(id, type, getTime(), "登录");
    }

    public static void changeUserImformation(int id, String type) {
        DBH.AddLog(id, type, getTime(), "修改个人信息");
    }

    public static void changeDoctorPrivilege(int id, String type) {
        DBH.AddLog(id, type, getTime(), "修改医生权限");
    }

    public static void deleteUser(int id, String type) {
        DBH.AddLog(id, type, getTime(), "删除医生或病患");
    }

    public static void addUser(int id, String type) {
        DBH.AddLog(id, type, getTime(), "添加用户");
    }

    public static void logOut(int id, String type) {
        DBH.AddLog(id, type, getTime(), "登出");
    }

    public static void search(int id, String type) {
        DBH.AddLog(id, type, getTime(), "查询");
    }

    public static void changePassword(int id, String type) {
        DBH.AddLog(id, type, getTime(), "修改密码");
    }

    public static void upload(int id, String type) {
        DBH.AddLog(id, type, getTime(), "上传");
    }

    public static void readUserManagement(int id, String type) {
        DBH.AddLog(id, type, getTime(), "查看用户管理界面");
    }

    public static void readLog(int id, String type) {
        DBH.AddLog(id, type, getTime(), "查看日志");
    }

    public static void deleteLog(int id, String type) {
        DBH.AddLog(id, type, getTime(), "删除日志");
    }

    public static void drawGraph(int id, String type) {
        DBH.AddLog(id, type, getTime(), "绘制图表");
    }

    public static void readPatient(int id, String type) {
        DBH.AddLog(id, type, getTime(), "查看病人列表");
    }

    public static void makeAppoitment(int id, String type) {
        DBH.AddLog(id, type, getTime(), "预约医生");
    }

    public static void AppoitmentPermit(int id, String type) {
        DBH.AddLog(id, type, getTime(), "同意预约");
    }

    public static void AppoitmentRead(int id, String type) {
        DBH.AddLog(id, type, getTime(), "查询预约");
    }

    public static void AppoitmentCancel(int id, String type) {
        DBH.AddLog(id, type, getTime(), "取消预约");
    }

    public static ArrayList<JSONObject> searchLogWithDate(String date) throws SQLException, IOException, ClassNotFoundException {
        //通过日期进行查询日志
        Conn=DBH.getConnection();
        Statement stt;
      //  System.out.println("查询日期");
        ArrayList<JSONObject> joA = new ArrayList<>();
      //  System.out.println("date:"+date);
        String Sql = "SELECT logNo as num,userNo as id ,type as account,action,time FROM log WHERE date_format(time,'%Y-%m-%d')=\"" + date + "\";";
        stt = Conn.createStatement();
    //    System.out.println("sql"+Sql);
        ResultSet set = null;
        set = stt.executeQuery(Sql);
      //  System.out.println(set.toString());
        while (set.next()) {
            JSONObject JO = new JSONObject();

            JO.put("num", set.getInt(1));
            JO.put("id", set.getInt(2));
            JO.put("account", set.getString(3));
            JO.put("action", set.getString(4));
            JO.put("time", set.getTimestamp(5).toString().split("\\.")[0]);
         //   System.out.println("中途jo："+JO);
            joA.add(JO);
        }
        return joA;
    }

    public static ArrayList<JSONObject> searchLog() throws SQLException, IOException, ClassNotFoundException {
        //查询日志
        Conn=DBH.getConnection();
        Statement stt;
        ArrayList<JSONObject> joA = new ArrayList<>();
        String Sql = "SELECT logNo as num,userNo as id, type as account,action,time FROM log;";
        stt = Conn.createStatement();
        ResultSet set = null;
        set = stt.executeQuery(Sql);

        while (set.next()) {
            JSONObject JO = new JSONObject();

            JO.put("num", set.getInt(1));
            JO.put("id", set.getInt(2));
            JO.put("account", set.getString(3));
            JO.put("action", set.getString(4));
            JO.put("time", set.getTimestamp(5).toString().split("\\.")[0]);

            joA.add(JO);
        }
        return joA;
    }
}
