package com;

import com.encrypty.splitedMatrix;
import com.encrypty.*;
import com.alibaba.fastjson.JSONObject;
//import com.google.gson.JsonObject;
import com.table.BloodTable;
import com.table.ToothTable;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.ArrayList;
public class DBH {
    private static Connection Conn; // 数据库连接对象

    // 数据库连接地址
    private static String URL = "jdbc:mysql://localhost:3306/healthCare?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";

    // 数据库的用户名
    private static String UserName = "root";
    // 数据库的密码
    private static String Password = "111111";


    public static Connection getConnection() {
        //连接数据库
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        try {

            //通过DriverManager类的getConenction方法指定三个参数,连接数据库
            Conn = DriverManager.getConnection(URL, UserName, Password);
            //       System.out.println("连接数据库成功!!!");
            //返回连接对象
            return Conn;

        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<JSONObject> searchForBlood(JSONObject jo) throws SQLException, IOException, ClassNotFoundException {
        //加密查询血液表相关指标
        ArrayList<JSONObject> JAL=new ArrayList<>();

        Statement stt;
        int patientID=jo.getInteger("patientId");
        String dateFrom=jo.getString("startDate");
        String dateTo=jo.getString("endDate");
        String Sql = "select ";
        if(jo.get("rbc")!=null&&jo.getBoolean("rbc"))Sql+="rbc,rbc_index,";
        if(jo.get("wbc")!=null&&jo.getBoolean("wbc"))Sql+="wbc,wbc_index,";
        if(jo.get("plt")!=null&&jo.getBoolean("plt"))Sql+="plt,plt_index,";
        Sql+="date,bloodNo from blood_routine where date_format(date,'%Y-%m-%d') between \""+dateFrom+"\" and \""+dateTo+
                "\" and patientID="+patientID+" order by date;";
        stt = Conn.createStatement();
        ResultSet set=null;
        set = stt.executeQuery(Sql);
        splitedMatrix rbcUp=new splitedMatrix(),rbcDown=new splitedMatrix(),
                wbcUp=new splitedMatrix(),wbcDown=new splitedMatrix(),
                pltDown=new splitedMatrix(),pltUp=new splitedMatrix();


        //     secret.getSecureQueryIndex()
        if(jo.get("rbc")!=null&&jo.getBoolean("rbc")){
            rbcDown=Secret.getSecureQueryIndex(jo.getDouble("rbcDown"));
            rbcUp=Secret.getSecureQueryIndex(jo.getDouble("rbcUp"));

            // rbcDown=secret.getSecureQueryIndex(15);
            // rbcUp=secret.getSecureQueryIndex(500);
        }
        if(jo.get("wbc")!=null&&jo.getBoolean("wbc")){
            //    System.out.println(jo.get("红细胞下限").getAsDouble());
            wbcDown=Secret.getSecureQueryIndex(jo.getDouble("wbcDown"));
            wbcUp=Secret.getSecureQueryIndex(jo.getDouble("wbcUp"));

            //   wbcDown=secret.getSecureQueryIndex(15);
            //  wbcUp=secret.getSecureQueryIndex(500);
        }
        if(jo.get("plt")!=null&&jo.getBoolean("plt")){
            pltDown=Secret.getSecureQueryIndex(jo.getDouble("pltDown"));
            pltUp=Secret.getSecureQueryIndex(jo.getDouble("pltUp"));
        }


        // System.out.println(set.getString(1));
        while (set.next()) {

            BloodTable bt=new BloodTable();
            bt.incluStr=new Vector<>();
            bt.num=new Vector<>();
            int count=0;
            //      System.out.println(1111);
            if(jo.getBoolean("rbc")!=null){
                //     System.out.println(111);
                Blob bb= set.getBlob(count+2);
                InputStream is=bb.getBinaryStream();                //获取二进制流对象
                BufferedInputStream bis=new BufferedInputStream(is);    //带缓冲区的流对象

                byte[] buff=new byte[(int) bb.length()];
                bis.read(buff, 0, buff.length);           //一次性全部读到buff中
                ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(buff));
                splitedMatrix sMarix=(splitedMatrix)in.readObject();

                //  System.out.println(new Query().Search(rbcDown,rbcUp,sMarix));
                if(new Query().Search(rbcDown,rbcUp,sMarix)==0){
                    //  System.out.println(set.getString(count+1));
                    bt.num.add(Double.parseDouble(AES.decrypt(set.getString(count+1))));
                    //     bt.num.add(Double.parseDouble(set.getString(count+1)));
                    //    System.out.println(AES.decrypt(set.getString(count+1)));
                    count+=2;
                    bt.incluStr.add("rbc");
                }
                else continue;
            }

            if(jo.getBoolean("wbc")!=null){

                Blob bb= set.getBlob(count+2);
                InputStream is=bb.getBinaryStream();                //获取二进制流对象
                BufferedInputStream bis=new BufferedInputStream(is);    //带缓冲区的流对象

                byte[] buff=new byte[(int) bb.length()];
                bis.read(buff, 0, buff.length);           //一次性全部读到buff中
                ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(buff));
                splitedMatrix sMarix=(splitedMatrix)in.readObject();

                //      System.out.println(new Query().Search(rbcDown,rbcUp,sMarix));
                if(new Query().Search(wbcDown,wbcUp,sMarix)==0){
                    bt.num.add(Double.parseDouble(AES.decrypt(set.getString(count+1))));
                    //   bt.num.add(Double.parseDouble(set.getString(count+1)));
                    //    System.out.println(set.getString(count+1));
                    count+=2;
                    bt.incluStr.add("wbc");
                }
                else continue;
            }

            if(jo.getBoolean("plt")!=null&&jo.getBoolean("plt")){

                Blob bb= set.getBlob(count+2);
                InputStream is=bb.getBinaryStream();                //获取二进制流对象
                BufferedInputStream bis=new BufferedInputStream(is);    //带缓冲区的流对象

                byte[] buff=new byte[(int) bb.length()];
                bis.read(buff, 0, buff.length);           //一次性全部读到buff中
                ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(buff));
                splitedMatrix sMarix=(splitedMatrix)in.readObject();


                //    System.out.println(new Query().Search(pltDown,pltUp,sMarix));
                if(new Query().Search(pltDown,pltUp,sMarix)==0){
                    bt.num.add(Double.parseDouble(AES.decrypt(set.getNString(count+1))));
                    //   System.out.println(set.getString(count+1));
                    //    bt.num.add(Double.parseDouble(set.getString(count+1)));
                    count+=2;
                    bt.incluStr.add("plt");
                }
                else continue;
            }
            bt.m_tableNum=set.getInt(count+2);
            bt.m_type="bloodTable";
            bt.m_date=set.getDate(count+1).toString();
            JAL.add(JsonHelper.talbeToJson(bt));
        }
        
        return JAL;
    }

    private static ArrayList<JSONObject> searchForTooth(JSONObject jo) throws SQLException, IOException, ClassNotFoundException {

        //加密查询牙齿表相关指标

        ArrayList<JSONObject> JAL=new ArrayList<>();
        Statement stt;
        int patientID=jo.getInteger("patientId");
        String dateFrom=jo.getString("startDate");
        String dateTo=jo.getString("endDate");
        String Sql = "select ";
        if(jo.get("pain")!=null&&jo.getBoolean("pain"))Sql+="pain,pain_index,";
        if(jo.get("mobility")!=null&&jo.getBoolean("mobility"))Sql+="mobility,mobility_index,";
        if(jo.get("tartar")!=null&&jo.getBoolean("tartar"))Sql+="tartar,tartar_index,";
        Sql+="date,toothNo from tooth_routine where date_format(date,'%Y-%m-%d') between \""+dateFrom+"\" and \""+dateTo+
                "\" and patientID="+patientID+" order by date;";
        stt = Conn.createStatement();
        ResultSet set=null;
        set = stt.executeQuery(Sql);

        splitedMatrix painUp=new splitedMatrix(),painDown=new splitedMatrix(),
                mobilityUp=new splitedMatrix(),mobilityDown=new splitedMatrix(),
                tartarDown=new splitedMatrix(),tartarUp=new splitedMatrix();
        if(jo.get("pain")!=null&&jo.getBoolean("pain")){
            painDown=Secret.getSecureQueryIndex(jo.getDouble("painDown"));
            painUp=Secret.getSecureQueryIndex(jo.getDouble("painUp"));
        }
        if(jo.get("mobility")!=null&&jo.getBoolean("mobility")){
            mobilityDown=Secret.getSecureQueryIndex(jo.getDouble("mobilityDown"));
            mobilityUp=Secret.getSecureQueryIndex(jo.getDouble("mobilityUp"));
        }
        if(jo.get("tartar")!=null&&jo.getBoolean("tartar")){
            tartarDown=Secret.getSecureQueryIndex(jo.getDouble("tartarDown"));
            tartarUp=Secret.getSecureQueryIndex(jo.getDouble("tartarUp"));
        }
        while (set.next()) {

            ToothTable tt=new ToothTable();
            tt.incluStr=new Vector<>();
            tt.num=new Vector<>();
            int count=0;
            if(jo.getBoolean("pain")!=null&&jo.getBoolean("pain")){
                Blob bb= set.getBlob(count+2);
                InputStream is=bb.getBinaryStream();                //获取二进制流对象
                BufferedInputStream bis=new BufferedInputStream(is);    //带缓冲区的流对象

                byte[] buff=new byte[(int) bb.length()];
                bis.read(buff, 0, buff.length);           //一次性全部读到buff中
                ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(buff));
                splitedMatrix sMarix=(splitedMatrix)in.readObject();
                if(new Query().Search(painDown,painUp,sMarix)==0){
                    tt.num.add(Double.parseDouble(AES.decrypt(set.getString(count+1))));
                    count+=2;
                    tt.incluStr.add("pain");
                }
                else continue;
            }

            if(jo.getBoolean("mobility")!=null&&jo.getBoolean("mobility")){

                Blob bb= set.getBlob(count+2);
                InputStream is=bb.getBinaryStream();                //获取二进制流对象
                BufferedInputStream bis=new BufferedInputStream(is);    //带缓冲区的流对象

                byte[] buff=new byte[(int) bb.length()];
                bis.read(buff, 0, buff.length);           //一次性全部读到buff中
                ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(buff));
                splitedMatrix sMarix=(splitedMatrix)in.readObject();
                if(new Query().Search(mobilityDown,mobilityUp,sMarix)==0){
                    tt.num.add(Double.parseDouble(AES.decrypt(set.getString(count+1))));
                    count+=2;
                    tt.incluStr.add("mobility");
                }
                else continue;
            }

            if(jo.getBoolean("tartar")!=null&&jo.getBoolean("tartar")){

                Blob bb= set.getBlob(count+2);
                InputStream is=bb.getBinaryStream();                //获取二进制流对象
                BufferedInputStream bis=new BufferedInputStream(is);    //带缓冲区的流对象

                byte[] buff=new byte[(int) bb.length()];
                bis.read(buff, 0, buff.length);           //一次性全部读到buff中
                ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(buff));
                splitedMatrix sMarix=(splitedMatrix)in.readObject();


                if(new Query().Search(tartarDown,tartarUp,sMarix)==0){
                    tt.num.add(Double.parseDouble(AES.decrypt(set.getNString(count+1))));

                    count+=2;
                    tt.incluStr.add("tartar");
                }
                else continue;
            }
            tt.m_tableNum=set.getInt(count+2);
            tt.m_type="toothTable";
            tt.m_date=set.getDate(count+1).toString();
            JAL.add(JsonHelper.talbeToJson(tt));

        }
        return JAL;
    }

    public static ArrayList<JSONObject> search(JSONObject jo){
        //调用子函数进行查询
        Connection conn=null;
        ResultSet set=null;
        String tablename=jo.getString("tableName");

        try {
            // 获取连接
            Conn = getConnection();

            if(tablename.equals("blood")){

                return searchForBlood(jo);}
            if(tablename.equals("tooth"))return searchForTooth(jo);
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
    private static JSONObject searchForToothByNo(int tableNum) throws SQLException, IOException, ClassNotFoundException {
        //通过病人id进行查询牙齿信息
        Statement stt;

        String Sql = "select ";

        Sql+="pain,mobility,tartar from tooth_routine where toothNo="+tableNum+";";
        stt = Conn.createStatement();
        ResultSet set=null;
        set = stt.executeQuery(Sql);
        JSONObject JO=  new JSONObject();

        // System.out.println(set.getString(1));
        while (set.next()) {
            JO.put("pain",Double.parseDouble(AES.decrypt(set.getString(1))));
            JO.put("mobility",Double.parseDouble(AES.decrypt(set.getString(2))));
            JO.put("tartar",Double.parseDouble(AES.decrypt(set.getString(3))));
        }
        return JO;
    }


    private static JSONObject searchForBloodByNo(int tableNum) throws SQLException, IOException, ClassNotFoundException {
        //通过病人id进行查询血液信息
        Statement stt;

        String Sql = "select ";

        Sql+="rbc,wbc,plt from blood_routine where bloodNo="+tableNum+";";
        stt = Conn.createStatement();
        ResultSet set=null;
        set = stt.executeQuery(Sql);

        JSONObject JO=  new JSONObject();
        while (set.next()) {
            JO.put("rbc",Double.parseDouble(AES.decrypt(set.getString(1))));
            JO.put("wbc",Double.parseDouble(AES.decrypt(set.getString(2))));
            JO.put("plt",Double.parseDouble(AES.decrypt(set.getString(3))));
        }
        return JO;
    }

    public static JSONObject searchByNo(int tableNo,String type){
        //调用函数实现按照病人id查询相关指标
        Connection conn=null;
        ResultSet set=null;


        try {
            // 获取连接
            Conn = getConnection();

            if(type.equals("bloodTable")){

                return searchForBloodByNo(tableNo);}
            if(type.equals("toothTable"))return searchForToothByNo(tableNo);
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

    public static void Add(String tablename, int patientID,  String date, Vector<String> num,
                           Vector<splitedMatrix> dataIndex){
        //将数据插入数据库
        Connection conn = DBH.getConnection();
        try {
            if(conn==null)
                return;

            String sql = "insert into ";

            if(tablename=="bloodTable"){
                sql+= "blood_routine(patientID,date,rbc,rbc_index,wbc,wbc_index,plt,plt_index)"
                        +" VALUES(?,?,?,?,?,?,?,?);";
            }
            else if(tablename=="toothTable"){
                sql+= "tooth_routine(patientID,date,pain,pain_index,mobility,mobility_index,tartar,tartar_index)"
                        +" VALUES(?,?,?,?,?,?,?,?);";
            }
       //     System.out.println(sql);
            PreparedStatement stt = conn.prepareStatement(sql);
            //执行sql语句

            stt.setInt(1,patientID);
           // System.out.println(date);
            date=date.replace('/','-');
           // System.out.println(date);
            stt.setDate(2, Date.valueOf(date));
            for(int i=0;i<num.size();++i){
                stt.setString(3+i*2,num.elementAt(i));
                stt.setObject(3+i*2+1,dataIndex.elementAt(i));
            }
           // System.out.println(sql);
            stt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {

                conn.close();

            } catch (Exception e2) {}

        }


    }
    public static void AddLog(int id,String type,String time,String actionType){
        Connection conn = DBH.getConnection();
        try {
            if (conn == null)
                return;
            String sql="insert into log(userNo,type,action,time)  VALUES(?,?,?,?);";
            PreparedStatement stt = conn.prepareStatement(sql);
            //执行sql语句
        //    System.out.println(time);
            stt.setInt(1,id);
            stt.setString(2,type);
            stt.setString(3,actionType);
            stt.setTimestamp(4, Timestamp.valueOf(time));

            stt.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                conn.close();
            } catch (Exception e2) {}

        }
    }
}
