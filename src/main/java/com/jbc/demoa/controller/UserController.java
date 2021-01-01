package com.jbc.demoa.controller;

import com.CSV;
import com.Log;
import com.alibaba.fastjson.JSONObject;
import com.jbc.demoa.mapper.UserMapper;
import com.jbc.demoa.pojo.*;
import com.jbc.demoa.util.util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @CrossOrigin
    @GetMapping("/getAdmin")  //测试，获取管理员列表
    public List<admin_account> getAllAdminList() {
        List<admin_account> admin_Accounts = userMapper.getAllAdminList();
        for (admin_account adminAccount : admin_Accounts) {
            System.out.println(admin_Accounts);

        }
        return admin_Accounts;
    }

    @CrossOrigin
    @GetMapping("/getAllDocAndPatient")  //获取所有医生和病人账号列表
    public List<Map<Object, Object>> getAllDocAndPatient() {
        List<Map<Object, Object>> mapList = new ArrayList<>();
        mapList.addAll(userMapper.getAllDocAccount());
        mapList.addAll(userMapper.getAllPatientAccount());
        return mapList;
    }

    @CrossOrigin  //删除医生或者病人
    @RequestMapping(value = "/deleteDocOrPatient", method = RequestMethod.POST, consumes = "application/json")
    public Boolean deleteDocOrPatient(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        String tag = jsonObject.getString("tag");
        if (tag.equals("病患")) {
            userMapper.deletePatientAccountByPhone(phone);
            userMapper.deletePatientByPhone(phone);
            return true;
        } else if (tag.equals("医师")) {
            userMapper.deleteDocAccountByPhone(phone);
            userMapper.deleteDocByPhone(phone);
            return true;
        }
        // Log.deleteLog(,"");

        return false;
    }

    @CrossOrigin
    //获取医生或病人详细信息
    @RequestMapping(value = "/getInformationDocOrPatient", method = RequestMethod.POST, consumes = "application/json")
    public Map<Object, Object> getInformationDocOrPatient(@RequestBody String jsonParamStr) {
        //noinspection AlibabaCollectionInitShouldAssignCapacity
        @SuppressWarnings("AlibabaCollectionInitShouldAssignCapacity") Map<Object, Object> map = new HashMap<Object, Object>();
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        String tag = jsonObject.getString("tag");
        if (tag.equals("病患")) {
            map = userMapper.getPatientDetailInformationByPhone(phone);
        } else if (tag.equals("医师")) {
            map = userMapper.getDocDetailInformationByPhone(phone);
        }
        return map;
    }

    //添加用户
    @CrossOrigin
    @RequestMapping(value = "/addUser", method = RequestMethod.POST, consumes = "application/json")
    public String addUser(@RequestBody String jsonParamStr) {
        boolean isRegister = false;
        int checkPhone = -1;
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String userName = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String phone = jsonObject.getString("phone");
        int value = Integer.parseInt(jsonObject.getString("value"));
        switch (value) {
            //管理员
            case 1:
                if (userMapper.checkAdminPhone(phone) == 1) {
                    return "手机号已被注册!";
                }
                userMapper.addAdmin(new admin_account(userName, password, phone));
                Log.addUser(userMapper.getAdminId(phone), "管理员");
                isRegister = true;
                //   Log.addUser(,"管理员");
                break;
            //医生
            case 2:
                if (userMapper.checkDoctorAccountPhone(phone) == 1) {
                    return "手机号已被注册!";
                }
                //在doc_account中添加了姓名，密码，手机号
                userMapper.addDocAccount(new doc_account(userName, password, phone));
                int DocId = userMapper.getDocIdByPhoneNo(phone);
                //向其中加入账号id，用户名，电话
                userMapper.addDocBase(new doctor(DocId, userName, phone));
                int docId = userMapper.getDoctorIdByPhone(phone);
                userMapper.updateDoctorIdByPhone(docId, phone);
                isRegister = true;
                Log.addUser(DocId, "医生");
                break;
            case 3: //病人
                if (userMapper.checkPatientAccountPhone(phone) == 1) {
                    return "手机号已被注册!";
                }
                userMapper.addPatientAccount(new patient_account(userName, password, phone));
                userMapper.addPatient(new patient(userName, phone));
                int patientId = userMapper.getPatientIdByPhone(phone);
                int patientAccountNo = userMapper.getPatientAccountNoByPhone(phone);
                userMapper.updatePatientAccountPatientIdByPhone(phone, patientId);
                userMapper.updatePatientPatientAccountByPhone(phone, patientAccountNo); //这个的问题
                isRegister = true;
                Log.addUser(patientId, "病人");
                break;
            default:
                break;
        }

        return String.valueOf(isRegister);
    }


    //检查用户能否登录
    @CrossOrigin
    @RequestMapping(value = "/checkUser", method = RequestMethod.POST, consumes = "application/json")
    public Boolean checkUser(@RequestBody String jsonParamStr) {
        boolean isLogin = false;
        int check = -1;
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        String password = jsonObject.getString("password");
        int value = Integer.parseInt(jsonObject.getString("value"));
        switch (value) {
            //管理员
            case 1:
                check = userMapper.checkAdminAccount(phone, password);
                if (check == 1) {
                    isLogin = true;
                    Log.logIn(userMapper.getAdminId(phone), "管理员");
                }
                break;
            //医生
            case 3:
                check = userMapper.checkDoctorAccount(phone, password);
                if (check == 1) {
                    isLogin = true;
                    Log.logIn(userMapper.getDoctorIdByPhone(phone), "医生");
                }
                break;
            //病人
            case 2:
                check = userMapper.checkPatientAccount(phone, password);
                if (check == 1) {
                    isLogin = true;
                    Log.logIn(userMapper.getPatientIdByPhone(phone), "病人");
                }
                break;
            default:
                break;
        }
        return isLogin;
    }

    //用户登出
    @CrossOrigin
    @RequestMapping(value = "/userLoginOut", method = RequestMethod.POST, consumes = "application/json")
    public void userLoginOut(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        int state = jsonObject.getInteger("state");
        switch (state) {
            case 1:
                Log.logOut(userMapper.getAdminId(phone), "管理员");
                break;
            case 2:
                Log.logOut(userMapper.getPatientIdByPhone(phone), "病人");
                break;
            case 3:
                Log.logOut(userMapper.getDoctorIdByPhone(phone), "医生");
        }

    }

    //上传文件
    @CrossOrigin
    @RequestMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }
        //获取上传文件名,包含后缀
        String originalFilename = file.getOriginalFilename();

        assert originalFilename != null;
        //获取后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //保存的文件名
        String dFileName = UUID.randomUUID() + substring;
        //保存路径
        //springboot 默认情况下只能加载 resource文件夹下静态资源文件
        String path = "C:\\Users\\78240\\Desktop\\download\\";
        //生成保存文件
        File uploadFile = new File(path + dFileName);
        //将上传文件保存到路径
        try {
            file.transferTo(uploadFile);
            if (originalFilename.charAt(0) == 'B') {

                if (!util.checkForBlood(path + dFileName)) {
                    if (uploadFile.exists()) {
                        uploadFile.delete();
                    }
                    return "表名与csv内容冲突";
                }
                CSV.csvRead(path + dFileName, "bloodTable");
            } else if (originalFilename.charAt(0) == 'T') {
                if (!util.checkForTooth(path + dFileName)) {
                    if (uploadFile.exists()) {
                        uploadFile.delete();
                    }
                    return "表名与csv内容冲突";
                }
                CSV.csvRead(path + dFileName, "toothTable");
            } else {
                if (uploadFile.exists()) {
                    uploadFile.delete();
                }
                return "命名不正确";
            }
            if (uploadFile.exists()) {
                uploadFile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传成功";
    }


    //获取用户名
    @CrossOrigin
    @RequestMapping(value = "/getAdminName", method = RequestMethod.POST, consumes = "application/json")
    public String getAdminName(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        return userMapper.getAdminName(phone);
    }

    //获取所有日志
    @CrossOrigin
    @RequestMapping(value = "/getAllLog", method = RequestMethod.GET)
    public ArrayList<JSONObject> getAllLog() throws SQLException, IOException, ClassNotFoundException {
        return Log.searchLog();
    }

    //通过日期筛选日志
    @CrossOrigin
    @RequestMapping(value = "/getAllLogByDate", method = RequestMethod.POST, consumes = "application/json")
    public ArrayList<JSONObject> getAllLogByDate(@RequestBody String jsonParamStr) throws SQLException, IOException, ClassNotFoundException {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        Date date = jsonObject.getDate("date");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return Log.searchLogWithDate(df.format(date));

    }

    //通过编号删除日志
    @CrossOrigin
    @RequestMapping(value = "/deleteLogByNum", method = RequestMethod.POST, consumes = "application/json")
    public void deleteLogByNum(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        int num = jsonObject.getInteger("num");
        userMapper.deleteLogByNum(num);
    }


    //获取重置密码的TodoList
    @CrossOrigin
    @RequestMapping(value = "/getTodoListReset", method = RequestMethod.GET)
    public List<Map<Object, Object>> getTodoListReset() {
        return userMapper.getTodoListReset();
    }

    //获取删除医师的TodoList
    @CrossOrigin
    @RequestMapping(value = "/getTodoListDelete", method = RequestMethod.GET)
    public List<Map<Object, Object>> getTodoListDelete() {
        return userMapper.getTodoListDelete();
    }


    //用户请求重置密码
    @CrossOrigin
    @RequestMapping(value = "/sendResetPassword", method = RequestMethod.POST, consumes = "application/json")
    public String sendResetPassword(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        String type = jsonObject.getString("type");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(new Date(System.currentTimeMillis()));
        switch (type) {
            case "病人":
                if (userMapper.checkPatientAccountPhone(phone) != 1) {
                    return "false";
                } else {
                    int userId = userMapper.getPatientIdByPhone(phone);
                    userMapper.insertTodoListRest(type, userId, date);
                }
                break;
            case "医师":
                if (userMapper.checkDoctorAccountPhone(phone) != 1) {
                    return "false";
                } else {
                    int userId = userMapper.getDoctorIdByPhone(phone);
                    userMapper.insertTodoListRest(type, userId, date);
                }
                break;
            case "管理员":
                if (userMapper.checkAdminPhone(phone) != 1) {
                    return "false";
                } else {
                    int userId = userMapper.getAdminId(phone);
                    userMapper.insertTodoListRest(type, userId, date);
                }
                break;
            default:
                return "false";
        }
        return "true";
    }

    //验证管理员密码
    @CrossOrigin
    @RequestMapping(value = "/checkAdminPassword", method = RequestMethod.POST, consumes = "application/json")
    public Boolean checkAdminPassword(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        String password = jsonObject.getString("password");
        return password.equals(userMapper.checkAdminPassword(phone));
    }

    //管理员修改密码
    @CrossOrigin
    @RequestMapping(value = "/updateAdminPassword", method = RequestMethod.POST, consumes = "application/json")
    public void updateAdminPassword(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        String password = jsonObject.getString("password");
        userMapper.updateAdminPassword(phone, password);
    }

    //管理员操作todoList
    @CrossOrigin
    @RequestMapping(value = "/updateTodoList", method = RequestMethod.POST, consumes = "application/json")
    public void updateTodoList(@RequestBody String jsonParamStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        int num = jsonObject.getInteger("num");
        int state = jsonObject.getInteger("state");
        String content = jsonObject.getString("content");
        if (state == 1) {
            if (content.equals("重置密码")) {
                String phone = jsonObject.getString("phone");
                String type = jsonObject.getString("type");
                if (type.equals("管理员")) {
                    userMapper.updateAdminPassword(phone, "111111");
                } else if (type.equals("病人")) {
                    userMapper.updatePatientPassword(phone, "111111");
                } else {
                    userMapper.updateDoctorPassword(phone, "111111");
                }
            } else if (content.equals("删除医生")) {
                int patientId = jsonObject.getInteger("patientId");
                int doctorId = jsonObject.getInteger("doctorId");
                userMapper.deleteRelationshipByPatientId(patientId, doctorId);
                userMapper.deleteRestrictionByPatientId(patientId, doctorId);
                Log.changeDoctorPrivilege(patientId, "病人");
            }
        }
        userMapper.updateTodoList(num, state);
    }
}
