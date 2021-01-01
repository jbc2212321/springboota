package com.jbc.demoa.controller;

import com.DBH;
import com.JsonHelper;
import com.Log;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jbc.demoa.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

@RestController
public class PatientController {
    @Autowired
    private UserMapper userMapper;

    //    之后是病人界面
    @CrossOrigin
    @GetMapping("/getAllDoc")  //获取所有医生账号列表
    public List<Map<Object, Object>> getAllDoc() {
        return new ArrayList<>(userMapper.getAllDoc());
    }


    //  添加医师和开放所有权限
    @CrossOrigin
    @RequestMapping(value = "/addDoctorAndPermission", method = RequestMethod.POST, consumes = "application/json")
    public String addDoctorAndPermission(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String patientPhone = jsonObject.getString("PatientPhone");
        JSONArray Doctor = jsonObject.getJSONArray("Doctor");
        for (int i = 0; i < Doctor.size(); i++) {
            String doctorPhone = (String) Doctor.getJSONObject(i).get("tel");
            int doctorId = userMapper.getDoctorIdByPhone(doctorPhone);
            int patientId = userMapper.getPatientIdByPhone(patientPhone);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            userMapper.insertRelationship(patientId, doctorId, df.format(new Date()));
            userMapper.insertRestriction(patientId, doctorId);
        }


        return "true";
    }

    //获取病人选择的医生的列表
    @CrossOrigin
    @RequestMapping(value = "/getRelationship", method = RequestMethod.POST, consumes = "application/json")
    public List<Object> getRelationship(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String patientPhone = jsonObject.getString("PatientPhone");
        int patientId = userMapper.getPatientIdByPhone(patientPhone);
        List<Map<Object, Object>> mapList = userMapper.getRelationshipByPatientId(patientId);
        List<Object> docList = new ArrayList<>();
        for (Map<Object, Object> objectObjectMap : mapList) {
            int docoterId = (int) objectObjectMap.get("doctorID");
            Map<Object, Object> map = userMapper.getDocListByDocId(docoterId);
            map.put("state", objectObjectMap.get("state"));
            docList.add(map);

        }

        return docList;

    }

    //修改病人选择的医生的权限
    @CrossOrigin
    @RequestMapping(value = "/updateRelationship", method = RequestMethod.POST, consumes = "application/json")
    public String updateRelationship(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String patientPhone = jsonObject.getString("PatientPhone");
        int patientId = userMapper.getPatientIdByPhone(patientPhone);
        String doctorPhone = jsonObject.getString("DoctorPhone");
        int doctorId = userMapper.getDoctorIdByPhone(doctorPhone);
        JSONArray checkList = jsonObject.getJSONArray("checkList");
        switch (checkList.size()) {
            case 0:
                userMapper.updateRelationshipByPatientId(patientId, doctorId, 0, 0);
                break;
            case 1:
                if (checkList.get(0).equals("口腔科")) {
                    userMapper.updateRelationshipByPatientId(patientId, doctorId, 0, 1);
                }
                if (checkList.get(0).equals("血液科")) {
                    userMapper.updateRelationshipByPatientId(patientId, doctorId, 1, 0);
                }
                break;
            case 2:
                userMapper.updateRelationshipByPatientId(patientId, doctorId, 1, 1);
                break;
            default:
                break;
        }
        Log.changeDoctorPrivilege(patientId, "病人");
        return "true";
    }

    //删除病人所选择的医生
    @CrossOrigin
    @RequestMapping(value = "/deleteRelationship", method = RequestMethod.POST, consumes = "application/json")
    public String deleteRelationship(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String patientPhone = jsonObject.getString("PatientPhone");
        int patientId = userMapper.getPatientIdByPhone(patientPhone);
        String doctorPhone = jsonObject.getString("DoctorPhone");
        int doctorId = userMapper.getDoctorIdByPhone(doctorPhone);
        userMapper.deleteRelationshipByPatientId(patientId, doctorId);
        userMapper.deleteRestrictionByPatientId(patientId, doctorId);
        Log.changeDoctorPrivilege(patientId, "病人");
        return "true";
    }


    //查询数据
    @CrossOrigin
    @RequestMapping(value = "/selectPatientCase", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8")
    public List<JSONObject> selectPatientCase(@RequestBody String jsonParamStr) {

        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        int patientId = userMapper.getPatientIdByPhone(jsonObject.getString("phone"));
        Log.search(patientId, "病人");
        jsonObject.put("patientId", patientId);
        Log.search(patientId, "病人");
        return DBH.search(JsonHelper.JsonToJson(jsonObject));
    }

    //病人个人数据
    @CrossOrigin
    @RequestMapping(value = "/updatePatientDetail", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8")
    public String updatePatientDetail(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        Map<String, Object> map = new HashMap<>();
        String patientPhone = jsonObject.getString("phoneNo");
        String sex = jsonObject.getString("sex");
        if (sex.length() == 2) {
            if (sex.equals("男性")) {
                map.put("sex", "f");
            } else if (sex.equals("女性")) {
                map.put("sex", "m");
            }
        } else {
            map.put("sex", jsonObject.getString("sex"));
        }
        map.put("birthday", jsonObject.getString("birthday").substring(0, 10));
        map.put("phoneNo", patientPhone);
        userMapper.updatePatientDetail(map);
//        Log.changeUserImformation(userMapper.getPatientIdByPhone(patientPhone),"病人");
        return "修改成功";
    }

    //获得病人详细信息
    @CrossOrigin
    @RequestMapping(value = "/getPatientDetail", method = RequestMethod.POST, consumes = "application/json")
    public Map<Object, Object> getPatientDetail(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String patientPhone = jsonObject.getString("phone");
        return userMapper.getPatientDetailInformationByPhone(patientPhone);
    }

    //获取病人对该医生的权限
    @CrossOrigin
    @RequestMapping(value = "/getPatientRelationship", method = RequestMethod.POST, consumes = "application/json")
    public List<Map<Object, Object>> getPatientRelationship(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String patientPhone = jsonObject.getString("PatientPhone");
        String doctorPhone = jsonObject.getString("DoctorPhone");
        int patientID = userMapper.getPatientIdByPhone(patientPhone);
        int doctorID = userMapper.getDoctorIdByPhone(doctorPhone);
        return userMapper.getPatientRelationship(patientID, doctorID);
    }


    //添加病人对医生的预约
    @CrossOrigin
    @RequestMapping(value = "/insertPatientAppointment", method = RequestMethod.POST, consumes = "application/json")
    public void insertPatientAppointment(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String patientPhone = jsonObject.getString("patientPhone");
        String doctorPhone = jsonObject.getString("doctorPhone");
        int patientID = userMapper.getPatientAccountNoByPhone(patientPhone);
        int doctorID = userMapper.getDocIdByPhoneNo(doctorPhone);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(jsonObject.getDate("date"));
        String start = date + " " + jsonObject.getString("startTime");
        String end = date + " " + jsonObject.getString("endTime");
        Log.makeAppoitment(patientID, "病人");
        userMapper.insertPatientAppointment(patientID, doctorID, start, end);
    }


    //获得病人所有的预约
    @CrossOrigin
    @RequestMapping(value = "/getPatientsAppointment", method = RequestMethod.POST, consumes = "application/json")
    public List<Map<Object, Object>> getPatientsAppointment(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String patientPhone = jsonObject.getString("patientPhone");
        int patientID = userMapper.getPatientAccountNoByPhone(patientPhone);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前日期
        String date = formatter.format(new Date(System.currentTimeMillis()));
        List<Map<Object, Object>> mapList = userMapper.getPatientsAppointment(patientID, date);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (Map<Object, Object> map : mapList) {
            String beginTime = df.format(map.get("bTime"));
            String endTime = df.format(map.get("eTime"));
            String time = beginTime + "-" + endTime.split(" ")[1];
            map.put("time", time);
        }
        Log.AppoitmentRead(patientID, "病人");
        return mapList;
    }


    //检验病人预约是否重复
    @CrossOrigin
    @RequestMapping(value = "/getPatientsAppointmentSingle", method = RequestMethod.POST, consumes = "application/json")
    public int getPatientsAppointmentSingle(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String patientPhone = jsonObject.getString("patientPhone");
        String doctorPhone = jsonObject.getString("doctorPhone");
        int patientID = userMapper.getPatientIdByPhone(patientPhone);
        int doctorID = userMapper.getDocIdByPhoneNo(doctorPhone);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(new Date(System.currentTimeMillis()));
        return userMapper.getPatientsAppointmentSingle(patientID, doctorID, date);
    }


    //病人通过编号取消预约
    @CrossOrigin
    @RequestMapping(value = "/deletePatientsAppointment", method = RequestMethod.POST, consumes = "application/json")
    public void deletePatientsAppointment(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        int num = jsonObject.getInteger("num");
        userMapper.deletePatientsAppointment(num);
    }

    //通过电话查询病人的用户名
    @CrossOrigin
    @RequestMapping(value = "/getPatientName", method = RequestMethod.POST, consumes = "application/json")
    public String getPatientName(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        return userMapper.getPatientName(phone);
    }

    //向管理员请求删除医师
    @CrossOrigin
    @RequestMapping(value = "/sendDeleteDoctor", method = RequestMethod.POST, consumes = "application/json")
    public void sendDeleteDoctor(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String patientPhone = jsonObject.getString("patientPhone");
        String doctorPhone = jsonObject.getString("doctorPhone");
        int patientID = userMapper.getPatientIdByPhone(patientPhone);
        int doctorID = userMapper.getDoctorIdByPhone(doctorPhone);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(new Date(System.currentTimeMillis()));
        userMapper.insertTodoListDelete(patientID, doctorID, date);
        userMapper.deleteDoctorModifyState(patientID, doctorID);

    }

    //验证病人密码
    @CrossOrigin
    @RequestMapping(value = "/checkPatientPassword", method = RequestMethod.POST, consumes = "application/json")
    public Boolean checkPatientPassword(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        String password = jsonObject.getString("password");
        return password.equals(userMapper.checkPatientPassword(phone));

    }

    //病人修改密码
    @CrossOrigin
    @RequestMapping(value = "/updatePatientPassword", method = RequestMethod.POST, consumes = "application/json")
    public void updatePatientPassword(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        String password = jsonObject.getString("password");
        userMapper.updatePatientPassword(phone, password);
    }
}
