package com;
import com.encrypty.SecretKey;

import java.io.*;

public class FileHelper {
    private String fileName;

    public FileHelper(){

    }

    public FileHelper(String fileName){
        //获取文件名
        this.fileName=fileName;
    }


    public void saveObjToFile(SecretKey sk){
        //将对象保存在文件中
        try {

            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(fileName));

            oos.writeObject(sk);

            oos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public SecretKey getObjFromFile(){
        //将文件中的对象读取出来
        try {
            ObjectInputStream ois=new ObjectInputStream(new FileInputStream(fileName));

            SecretKey sk=(SecretKey)ois.readObject();

            return sk;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return null;
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

}
