package com;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.table.*;

public class JsonHelper {


    public  static JSONObject TtalbeToJson(ToothTable tt)
    {
        //将查到的牙齿表对象转化为json格式返回给前端
        JSONObject jo=new JSONObject();
        jo.put("office","口腔科");
        jo.put("tableId",tt.m_tableNum);
        jo.put("date",tt.m_date);

        for(int i=0;i<tt.incluStr.size();++i)
            jo.put(tt.incluStr.elementAt(i),tt.num.elementAt(i));
        return jo;
    }

    public  static JSONObject BtalbeToJson(BloodTable bt)
    {

        //将查到的血液表对象转化为json格式返回给前端
        JSONObject jo=new JSONObject();
        jo.put("office","血液科");
        jo.put("tableId",bt.m_tableNum);
        jo.put("date",bt.m_date);

        for(int i=0;i<bt.incluStr.size();++i)
            jo.put(bt.incluStr.elementAt(i),bt.num.elementAt(i));
        return jo;
    }
    public  static JSONObject JsonToJson(JSONObject jo){
        //对传入的json进行解析，生成用于查询的json
        JSONObject joNew=new JSONObject();
        joNew.put("tableName",jo.getString("tableName"));
        joNew.put("patientId",jo.getString("patientId"));
        joNew.put("startDate",jo.getString("startDate").split("T")[0]);
        joNew.put("endDate",jo.getString("endDate").split("T")[0]);

        JSONArray joA=jo.getJSONArray("value");

        for(int i=0;i<joA.size();++i){
            joNew.put(joA.getJSONObject(i).getString("values"),true);//ԭɸѡ��Ŀ
            joNew.put(joA.getJSONObject(i).getString("values")+"Down",joA.getJSONObject(i).getDoubleValue("down"));
            joNew.put(joA.getJSONObject(i).getString("values")+"Up",joA.getJSONObject(i).getDoubleValue("up"));
        }

        return joNew;
    }





    public  static JSONObject talbeToJson(FatherTable ft)
    {
        //调用对应函数将表转化为json
        JSONObject jo=new JSONObject();
        if(ft.getM_type().equals("bloodTable"))return BtalbeToJson((BloodTable) ft);
        if(ft.getM_type().equals("toothTable"))return TtalbeToJson((ToothTable) ft);
        return jo;
    }

}