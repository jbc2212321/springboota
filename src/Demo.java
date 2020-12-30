import com.*;
import com.encrypty.*;
import com.table.bloodTable;
import com.table.fatherTable;

import java.io.IOException;
import java.util.Vector;

/**
 * @ClassName Demo
 * @Description CIPE??????
 * @Date 2020/10/12 21:21
 * @Version 1.0
 */
public class Demo {

    /**
     * 获取安全数据索引
     *
     * @param data 数据
     * @param sk   密钥
     * @return
     */
    public static splitedMatrix getSecureDataIndex(double data, SecretKey sk) {
        splitedMatrix secureDataIndex = null;

        // 生成明文索引
        IndexEncrypty indexEncrypty = new IndexEncrypty();
        double[] index = indexEncrypty.BuildIndexVector(data, sk);

        // 索引加密
        splitedMatrix splitedMatrix = indexEncrypty.splitIndexVector(index, sk);
        secureDataIndex = indexEncrypty.EncIndexVector(splitedMatrix, sk);

        return secureDataIndex;
    }

    /**
     * 获取安全查询索引
     *
     * @param data 查询边界值
     * @param sk
     * @return 密钥
     */
    public static splitedMatrix getSecureQueryIndex(double data, SecretKey sk) {
        splitedMatrix secureQueryIndex = null;

        // 生成明文索引
        QueryEncrypty queryEncrypty = new QueryEncrypty();
        double[] index = queryEncrypty.BuildQueryVector(data, sk);

        // 索引加密
        splitedMatrix splitedMatrix = queryEncrypty.splitQueryVector(index, sk);
        secureQueryIndex = queryEncrypty.EncQueryVector(splitedMatrix, sk);

        return secureQueryIndex;
    }


    public static void main(String[] args) throws IOException {
    //    secret.getSecureDataIndex(2.0);
      //  secret.getSecureDataIndex(2.0);
      //  System.out.println(secret.getSecureDataIndex(2.0));
       // System.out.println(secret.getSecureDataIndex(2.0));

      bloodTable bt=new bloodTable();
     //String s=AES.encrypt(2.0);
    //  System.out.println(AES.decrypt(s));
        Vector<fatherTable> ftV= csv.csvRead("C:\\Users\\32809\\Desktop\\test.csv","血液表");
      //  bloodTable bt=new bloodTable();
        for(int i=0;i<ftV.size();++i){
            bt= (bloodTable) secret.Secret(ftV.elementAt(i));
       //     System.out.println(bt.num);
            DBH.Add("血液表",bt.m_patientID,bt.m_date,bt.AESString,bt.dataIndex);
        }
      bt.m_type="血液表";
      bt.m_name="zzq";
        System.out.println(DBH.search(jsonHelper.TestJson()));
    //  System.out.println(DBH.search(jsonHelper.JsonToJson(jsonHelper.TestJson2())));
    //  System.out.println(jsonHelper.TestJson2());
    //    System.out.println(jsonHelper.BtalbeToJson(bt));
        //  com.DBH.search();

     //   System.out.println(ft.elementAt(0).m_name);
      //  System.out.println(jsonHelper.talbeToJson(ft.elementAt(1)));
        //获取密钥
   //     SecretKey sk = Gen.GenKey(64);
     //   System.out.println(sk);
   //     double data = -113.0;
    //    double queryLeft = 2.0;
      //  double queryRight = 4.0;
       // String str= AES.encrypt(data);
    //    System.out.println(str+" "+com.AES.decrypt(str));
        // 构造索引
      //  splitedMatrix dataIndex1 = getSecureDataIndex(data, sk);

   //     ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("test"));

       // com.DBH.Add("bloodTalbe",str,dataIndex1);
    //    DBH.search("bloodTable",sk);


     //   splitedMatrix queryIndexLeft = getSecureQueryIndex(queryLeft, sk);
       // splitedMatrix queryIndexRight = getSecureQueryIndex(queryRight, sk);

        // 查询 结果为0则满足范围查询条件
        // 开区间范围查询
     //   System.out.println(""+dataIndex1.toString());
   //     System.out.println(new Query().Search(queryIndexLeft, queryIndexRight, dataIndex1));


    }



}
