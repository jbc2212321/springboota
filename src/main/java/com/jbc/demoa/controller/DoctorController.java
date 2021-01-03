package com.jbc.demoa.controller;

import com.DBH;
import com.DBHDoctor;
import com.Log;
import com.alibaba.fastjson.JSONObject;
import com.jbc.demoa.mapper.UserMapper;
import com.jbc.demoa.util.util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class DoctorController {
    @Autowired
    private UserMapper userMapper;

    //    医生
    //医生个人界面
    @CrossOrigin
    @RequestMapping(value = "/getDocDetail", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8")
    public Map<Object, Object> getDocDetail(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String doctorPhone = jsonObject.getString("DoctorPhone");
        return userMapper.getDocDetailInformationByPhone(doctorPhone);
    }

    //修改医生个人信息
    @CrossOrigin
    @RequestMapping(value = "/updateDocDetail", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8")
    public String updateDocDetail(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        Map<String, Object> map = new HashMap<>();
        String doctorPhone = jsonObject.getString("DoctorPhone");
        String sex = jsonObject.getString("sex");
        if (sex.length() == 2) {
            if (sex.equals("男性")) {
                map.put("sex", "m");
            } else if (sex.equals("女性")) {
                map.put("sex", "f");
            }
        } else {
            map.put("sex", jsonObject.getString("sex"));
        }
        map.put("phoneNo", doctorPhone);
        if (jsonObject.getString("birthday") != null) {
            map.put("birthday", jsonObject.getString("birthday").substring(0, 10));
        }
        if (jsonObject.getInteger("departmentNo") != null) {
            if (jsonObject.getInteger("departmentNo") != 0) {
                map.put("departmentNo", jsonObject.getInteger("departmentNo"));
            }

        }
        map.put("nationality", jsonObject.getString("nationality"));
        map.put("nation", jsonObject.getString("nation"));
        map.put("college", jsonObject.getString("college"));
        map.put("address", jsonObject.getString("address"));
        map.put("expertise", jsonObject.getString("expertise"));
        map.put("works", jsonObject.getString("works"));
        map.put("introduction", jsonObject.getString("introduction"));
        map.put("achievements", jsonObject.getString("achievements"));
        map.put("evaluation", jsonObject.getString("evaluation"));
        map.put("nativePlace", jsonObject.getString("nativePlace"));
        userMapper.updateDocDetail(map);
        return "修改成功!";
    }


    //通过医生手机号查询病人列表
    @CrossOrigin
    @RequestMapping(value = "/getDocsPatient", method = RequestMethod.POST, consumes = "application/json")
    List<Map<Object, Object>> getDocsPatient(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String doctorPhone = jsonObject.getString("DoctorPhone");
        int doctorID = userMapper.getDoctorIdByPhone(doctorPhone);
        List<Map<Object, Object>> mapList = userMapper.getDocsPatient(doctorID);
        for (Map<Object, Object> maps : mapList) {
            if (maps.get("sex") == null) {
                maps.put("sex", "");
            } else {
                if (maps.get("sex").equals("m")) {
                    maps.put("sex", "男性");
                } else if (maps.get("sex").equals("f")) {
                    maps.put("sex", "女性");
                } else {
                    maps.put("sex", "");
                }
            }
        }
        Log.readPatient(doctorID, "医生");
        return mapList;
    }


    //通过医生id查看其管理病人的所有体检单
    @CrossOrigin
    @RequestMapping(value = "/getDocsPatientsRecord", method = RequestMethod.POST, consumes = "application/json")
    List<Map<Object, Object>> getDocsPatientsRecord(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String doctorPhone = jsonObject.getString("doctorPhone");
        int doctorID = userMapper.getDoctorIdByPhone(doctorPhone);
        String patientPhone = jsonObject.getString("patientPhone");
        int patientId = userMapper.getPatientIdByPhone(patientPhone);
        Log.search(doctorID, "医生");
        return userMapper.getDocsPatientsRecord(patientId, doctorID);
    }


    /**
     * ..
     * 根据体检单号获取病人病例
     * ....
     */
    @CrossOrigin
    @RequestMapping(value = "/getDocsPatientsRecordDetail", method = RequestMethod.POST, consumes = "application/json")
    public JSONObject getDocsPatientsRecordDetail(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String office = jsonObject.getString("office");
        if (office.equals("血液科")) {
            office = "bloodTable";
        } else if (office.equals("口腔科")) {
            office = "toothTable";
        }
        return DBH.searchByNo(jsonObject.getInteger("tableID"), office);
    }

    /**
     * ....
     * 获取医生管理下病人的数据图表
     * ..
     *
     * @return 返回list，储存列表
     */
    @CrossOrigin
    @RequestMapping(value = "/getDocsPatientsChart", method = RequestMethod.POST, consumes = "application/json")
    public ArrayList<JSONObject> getDocsPatientsChart(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        int patientId = jsonObject.getInteger("patientNum");
        String doctorPhone = jsonObject.getString("doctorPhone");
        int doctorId = userMapper.getDoctorIdByPhone(doctorPhone);
        String type = jsonObject.getString("type");
        String date01 = jsonObject.getString("date01");
        String date02 = jsonObject.getString("date02");
        Log.drawGraph(doctorId, "医生");
        return DBHDoctor.searchByNoWithDate(patientId, doctorId, type, date01, date02);
    }

    // 通过医生id查看其预约的情况
    @CrossOrigin
    @RequestMapping(value = "/getDocsAppointment", method = RequestMethod.POST, consumes = "application/json")
    public List<Map<Object, Object>> getDocsAppointment(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String doctorPhone = jsonObject.getString("phone");
        int doctorId = userMapper.getDocIdByPhoneNo(doctorPhone);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前日期
        String date = formatter.format(new Date(System.currentTimeMillis()));
        List<Map<Object, Object>> mapList = userMapper.getDocsAppointment(doctorId, date);
        for (Map<Object, Object> map : mapList) {
            String beginTime = df.format(map.get("bTime"));
            String endTime = df.format(map.get("eTime"));
            String time = beginTime + "-" + endTime.split(" ")[1];
            map.put("time", time);
        }
        Log.AppoitmentRead(doctorId, "医生");
        return mapList;
    }

    //通过预约编号修改预约的状态
    @CrossOrigin
    @RequestMapping(value = "/setDocsAppointment", method = RequestMethod.POST, consumes = "application/json")
    public void setDocsAppointment(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        int appointmentNum = jsonObject.getInteger("num");
        int state = jsonObject.getInteger("state");
        userMapper.setDocsAppointment(appointmentNum, state);
    }

    //检查预约的时间是否冲突
    @CrossOrigin
    @RequestMapping(value = "/checkDocsAppointment", method = RequestMethod.POST, consumes = "application/json")
    public Boolean checkDocsAppointment(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        int appointmentNo = jsonObject.getInteger("appointmentNo");
        String doctorPhone = jsonObject.getString("doctorPhone");
        int doctorId = userMapper.getDocIdByPhoneNo(doctorPhone);
        String date01 = jsonObject.getString("date1");
        String date02 = jsonObject.getString("date2");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<Map<String, Object>> mapList = userMapper.checkDocsAppointment(doctorId, appointmentNo);
        for (Map<String, Object> map : mapList) {
            if (!util.dateCheck(date01, date02, formatter.format(map.get("bTime")), formatter.format(map.get("eTime")))) {
                return false;
            }
        }
        return true;
    }

    //通过电话查询医生的用户名
    @CrossOrigin
    @RequestMapping(value = "/getDoctorName", method = RequestMethod.POST, consumes = "application/json")
    public String getDoctorName(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        return userMapper.getDoctorName(phone);
    }


    //验证医生密码
    @CrossOrigin
    @RequestMapping(value = "/checkDoctorPassword", method = RequestMethod.POST, consumes = "application/json")
    public Boolean checkDoctorPassword(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        String password = jsonObject.getString("password");
        return password.equals(userMapper.checkDoctorPassword(phone));

    }

    //医生修改密码
    @CrossOrigin
    @RequestMapping(value = "/updateDoctorPassword", method = RequestMethod.POST, consumes = "application/json")
    public void updateDoctorPassword(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        String password = jsonObject.getString("password");
        userMapper.updateDoctorPassword(phone, password);
    }
}
