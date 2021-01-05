package com;

import com.encrypty.*;
import com.table.BloodTable;
import com.table.FatherTable;
import com.table.ToothTable;

import java.util.Vector;

public class Secret {

    SecretKey sk = Gen.GenKey(64);

    public static SecretKey getKey() {
        //读取密钥，如果没有密钥，则生成密钥
        FileHelper fi = new FileHelper("E:\\programming\\java\\demoa\\s//rc\\secretKey");
        SecretKey sk = fi.getObjFromFile();
        if (sk != null) return sk;
        sk = Gen.GenKey(64);
        fi.saveObjToFile(sk);
        return sk;
    }

    public static splitedMatrix getSecureDataIndex(double data) {
        //生成加密索引
        splitedMatrix secureDataIndex = null;
        SecretKey sk = getKey();
        IndexEncrypty indexEncrypty = new IndexEncrypty();
        double[] index = indexEncrypty.BuildIndexVector(data, sk);
        splitedMatrix splitedMatrix = indexEncrypty.splitIndexVector(index, sk);
        secureDataIndex = indexEncrypty.EncIndexVector(splitedMatrix, sk);

        return secureDataIndex;
    }

    public static splitedMatrix getSecureQueryIndex(double data) {
        //生成加密查询索引
        splitedMatrix secureQueryIndex = null;

        SecretKey sk = getKey();
        QueryEncrypty queryEncrypty = new QueryEncrypty();
        double[] index = queryEncrypty.BuildQueryVector(data, sk);
        splitedMatrix splitedMatrix = queryEncrypty.splitQueryVector(index, sk);
        secureQueryIndex = queryEncrypty.EncQueryVector(splitedMatrix, sk);

        return secureQueryIndex;
    }

    private static BloodTable SecretForBlood(BloodTable bt) {
        //为血液表对象进行加密
        bt.AESString = new Vector<>();
        bt.dataIndex = new Vector<>();
        for (int i = 0; i < bt.num.size(); ++i) {

            bt.AESString.add(AES.encrypt(bt.num.elementAt(i)));
            bt.dataIndex.add(getSecureDataIndex(bt.num.elementAt(i)));

        }
        return bt;
    }


    private static ToothTable SecretForTooth(ToothTable tt) {
        //为牙齿表对象进行加密
        tt.AESString = new Vector<>();
        tt.dataIndex = new Vector<>();
        for (int i = 0; i < tt.num.size(); ++i) {
            tt.AESString.add(AES.encrypt(tt.num.elementAt(i)));
            tt.dataIndex.add(getSecureDataIndex(tt.num.elementAt(i)));
        }
        return tt;
    }

    public static FatherTable Secret(FatherTable ft) {
        //调用对应函数为对应表对象做加密
        if (ft.getM_type().equals("toothTable")) ft = SecretForTooth((ToothTable) ft);
        if (ft.getM_type().equals("bloodTable")) ft = SecretForBlood((BloodTable) ft);
        return ft;
    }


}
