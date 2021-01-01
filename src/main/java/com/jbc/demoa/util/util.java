package com.jbc.demoa.util;

import com.csvreader.CsvReader;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

//工具类
public class util {
    //检查cmp
    public static boolean checkCmp(String a1, String a2) {

        for (int i = 0; i < 5; ++i) {
            if (a1.charAt(i) < a2.charAt(i)) return false;
            if (a1.charAt(i) > a2.charAt(i)) return true;

        }
        return true;
    }

    //判断日期是否冲突
    public static boolean dateCheck(String a1, String a2, String b1, String b2) {
        if ((a1.split(" ")[0]).equals(b1.split(" ")[0])) {
            a1 = a1.split(" ")[1];
            a2 = a2.split(" ")[1];
            b1 = b1.split(" ")[1];
            b2 = b2.split(" ")[1];
            if (a1.length() != 5) a1 = "0" + a1;
            if (a2.length() != 5) a2 = "0" + a2;
            if (((!(checkCmp(a1, b1)) && !(checkCmp(a2, b1)))) || a2.equals(b1)) return true;
            return ((checkCmp(a1, b2)) && (checkCmp(a2, b2))) || a1.equals(b2);
        }
        return true;
    }

    public static boolean checkForTooth(String filename) {
        //从csv文件中分别调用其他表的函数读取表

        try {
            File fileIN = new File(filename);
            BufferedReader reader = new BufferedReader(new FileReader(fileIN));
            CsvReader csvReader = new CsvReader(filename, ',', StandardCharsets.UTF_8);
            //  System.out.println("111"+csvReader.getHeaderCount());
            ArrayList<String[]> List = new ArrayList<String[]>();
            while (csvReader.readRecord()) {
                List.add(csvReader.getValues());
            }
            if (List.get(0)[0].equals("patientID") && List.get(0)[1].equals("date") && List.get(0)[2].equals("pain")
                    && List.get(0)[3].equals("mobility") && List.get(0)[4].equals("tartar")) return true;
            //   if(csvReader.getHeader(0))
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkForBlood(String filename) {
        //从csv文件中分别调用其他表的函数读取表

        try {
            File fileIN = new File(filename);
            BufferedReader reader = new BufferedReader(new FileReader(fileIN));
            CsvReader csvReader = new CsvReader(filename, ',', StandardCharsets.UTF_8);
            //  System.out.println("111"+csvReader.getHeaderCount());
            ArrayList<String[]> List = new ArrayList<String[]>();
            while (csvReader.readRecord()) {
                List.add(csvReader.getValues());
            }
            if (List.get(0)[0].equals("patientID") && List.get(0)[1].equals("date") && List.get(0)[2].equals("rbc")
                    && List.get(0)[3].equals("wbc") && List.get(0)[4].equals("plt")) return true;
            //   if(csvReader.getHeader(0))
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}

