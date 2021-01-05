package com.jbc.demoa.mapper;

import com.jbc.demoa.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 78240
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
@Repository
public interface UserMapper {
    List<admin_account> getAllAdminList();  //获取所有管理员列表

    String getAdminName(@Param("phone") String phone);  //通过手机号查询管理员用户名

    String getDoctorName(@Param("phone") String phone);  //通过手机号查询医生用户名

    String getPatientName(@Param("phone") String phone);  //通过手机号查询病人用户名

    void deleteLogByNum(@Param("num") int num);  //通过日志id删除日志

    @Select("select accountID from admin_account where phoneNo=#{phone}")
    int getAdminId(@Param("phone") String phone);  //获取管理员id


    List<Map<Object, Object>> getTodoListReset();//获取重置密码的信息


    List<Map<Object, Object>> getTodoListDelete();//获取删除医师的信息


    void insertTodoListDelete(@Param("patientId") int p, @Param("doctorId") int d, @Param("date") String date);  //向待办事项中添加病人删除医师

    void insertTodoListRest(@Param("type") String type, @Param("userId") int userId, @Param("date") String date);  //向待办事项中添加重置密码

    admin_account getAdminByName(String userName);

    void addAdmin(admin_account admin);

    List<doc_account> getAllDocList();     //获取所有医生列表

    void addDocAccount(doc_account docAccount);   //增加医生账户

    int getDocIdByPhoneNo(String phone);   //通过手机号查询医生账户的NO

    int getDoctorIdByPhone(@Param("phone") String phone);  //通过手机号查询医生id

    void updateDoctorIdByPhone(@Param("doctorID") int doctorID, @Param("phone") String phone);  //向医生账号中插入医生id

    void addDocBase(doctor doctor); //增加医生个人信息

    doctor getDocByPhone(String phone);  //通过电话查询医生个人信息

    void addPatientAccount(patient_account patientAccount);  //新增病人账户

    void addPatient(patient patient);  //新增病人信息;

    int getPatientAccountNoByPhone(String phone); //通过电话查询病人账户编号

    int getPatientIdByPhone(String phone); //通过电话查询病人id

    void updatePatientAccountPatientIdByPhone(@Param("phone") String phone, @Param("patientId") int patientId);  //在病人账户中加入病人编号

    void updatePatientPatientAccountByPhone(@Param("phone") String phone, @Param("patientAccountNo") int patientAccountNo); //在病人中添加病人账户编号

    //接下来三条是通过电话和密码查询验证管理员、病人、医生账户信息是否存在，1表示存在，0表示不存在
    int checkAdminAccount(@Param("phone") String phone, @Param("password") String password);   //返回管理员账户编号

    int checkDoctorAccount(@Param("phone") String phone, @Param("password") String password);   //返回医生账户编号

    int checkPatientAccount(@Param("phone") String phone, @Param("password") String password);   //返回病人账户编号

    //接下来3条是验证手机号有没有被注册，1表示存在，0表示不存在
    int checkAdminPhone(@Param("phone") String phone);  //验证管理员手机号

    int checkDoctorAccountPhone(@Param("phone") String phone); //验证医生手机号

    int checkPatientAccountPhone(@Param("phone") String phone); //验证病人手机号

    //    接下来2条是获取所有病人账号和医生账号的信息
    List<Map<Object, Object>> getAllPatientAccount();   //获取所有病人账号

    List<Map<Object, Object>> getAllDocAccount();    //获取所有医生账号

    List<Map<Object, Object>> getAllDoc();    //获取所有医生,用于病人选择医生
//

    Map<Object, Object> getDocDetailInformationByPhone(@Param("phone") String phone);  //通关医生手机号查询医生详细信息

    Map<Object, Object> getPatientDetailInformationByPhone(@Param("phone") String phone); //通过病人手机号查询病人详细信息

    //    接下来多条是删除病人或者医生，目前只有4条
    void deleteDocByPhone(@Param("phone") String phone);  //通过医生手机号删除医生

    void deleteDocAccountByPhone(@Param("phone") String phone);  //通过医生手机号删除医生账户

    void deletePatientByPhone(@Param("phone") String phone);  //通过病人手机号删除病人

    void deletePatientAccountByPhone(@Param("phone") String phone);  //通过病人手机号删除病人账户
//    上述是删除病人或者医生


    //    医生与病人关系表
    //添加关系
    void insertRelationship(@Param("PatientID") int patientID, @Param("DoctorID") int doctorID, @Param("date") String date);

    void insertRestriction(@Param("PatientID") int patientID, @Param("DoctorID") int doctorID);//添加权限，默认全1

    List<Map<Object, Object>> getRelationshipByPatientId(@Param("PatientID") int patientID);  //通过病人编号查询其选择的病人

    Map<Object, Object> getDocListByDocId(@Param("DoctorID") int doctorID);  //通过医生id返回医生列表

    //修改关系
    void updateRelationshipByPatientId(@Param("PatientID") int patientID, @Param("DoctorID") int doctorID,
                                       @Param("blood") int blood, @Param("tooth") int tooth);

    //删除关系
    void deleteRelationshipByPatientId(@Param("PatientID") int patientID, @Param("DoctorID") int doctorID);

    //通过编号删除病人选择医生的权限
    void deleteRestrictionByPatientId(@Param("PatientID") int patientID, @Param("DoctorID") int doctorID);


    //通过医生手机号修改医生的个人资料
    void updateDocDetail(Map<String, Object> map);

    //通过病人手机号修改病人资料
    void updatePatientDetail(Map<String, Object> map);

    //通过医生编号查看其管理的病人列表
    List<Map<Object, Object>> getDocsPatient(@Param("DoctorID") int doctorID);

    //通过医生id查看其管理病人的所有体检单
    List<Map<Object, Object>> getDocsPatientsRecord(@Param("PatientID") int patientID, @Param("DoctorID") int doctorID);

    //通过病人id查看其设置的权限
    List<Map<Object, Object>> getPatientRelationship(@Param("PatientID") int patientID, @Param("DoctorID") int doctorID);

    //通过医生id查看其预约的情况
    List<Map<Object, Object>> getDocsAppointment(@Param("DoctorID") int doctorID, @Param("date") String date);

    //通过预约编号修改预约的状态
    void setDocsAppointment(@Param("num") int num, @Param("state") int state);

    //添加病人对医生的预约
    void insertPatientAppointment(@Param("patientID") int patientID, @Param("doctorID") int doctorID, @Param("start") String start, @Param("end") String end);

    //通过病人id查看其预约的情况
    List<Map<Object, Object>> getPatientsAppointment(@Param("patientID") int patientID, @Param("date") String date);

    //一个人病人只能选择一个医生，查看有没有重复的
    int getPatientsAppointmentSingle(@Param("patientID") int patientID, @Param("doctorID") int doctorID, @Param("date") String date);

    //病人通过编号取消预约
    void deletePatientsAppointment(@Param("num") int num);

    //医生预约时候检查是否冲突,获取该医生对应所有病人的时间
    List<Map<String, Object>> checkDocsAppointment(@Param("doctorID") int doctorID, @Param("appointmentNo") int appointmentNo);

    //病人删除医师（发送请求，本地关系中state置换成0）
    @Update("update doctor_patient_relationship set state=0 where patientID=#{patientID} and doctorID=#{doctorID}")
    void deleteDoctorModifyState(@Param("patientID") int patientID, @Param("doctorID") int doctorID);

    //病人验证密码
    @Select("SELECT password FROM patient_account WHERE phoneNo=#{phone}")
    String checkPatientPassword(@Param("phone") String phoneNo);


    //管理员验证密码
    @Select("SELECT password FROM admin_account WHERE phoneNo=#{phone}")
    String checkAdminPassword(@Param("phone") String phoneNo);


    //医生验证密码
    @Select("SELECT password FROM doc_account WHERE phoneNo=#{phone}")
    String checkDoctorPassword(@Param("phone") String phoneNo);

    //管理员修改密码
    @Update("UPDATE admin_account SET password=#{password}WHERE phoneNo=#{phone}")
    void updateAdminPassword(@Param("phone") String phoneNo, @Param("password") String password);

    //病人修改密码
    @Update("UPDATE patient_account SET password=#{password}WHERE phoneNo=#{phone}")
    void updatePatientPassword(@Param("phone") String phoneNo, @Param("password") String password);

    //医生修改密码
    @Update("UPDATE doc_account SET password=#{password}WHERE phoneNo=#{phone}")
    void updateDoctorPassword(@Param("phone") String phoneNo, @Param("password") String password);

    //管理员处理todoList
    @Update("UPDATE todolist SET state=#{state} WHERE todolist_no=#{num}")
    void updateTodoList(@Param("num")int num,@Param("state")int state);


    //管理员拒绝删除医师
    @Update("update doctor_patient_relationship set state=2 where patientID=#{patientID} and doctorID=#{doctorID} ")
    void updateTodoListState2(@Param("patientID") int patientID, @Param("doctorID")int doctorID);

}