package com.jbc.demoa.util;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by �����޷�����˼ on 2018/5/23.
 * ʹ��AES�����㷨��String���ͽ��м��ܺͽ��ܣ��ṩsetKey(String key)��String encrypt(String content)
 * ��decrypt(String content)������������һ�������������üӽ�����Կ��Ĭ����Կ cswfqxs�����ڶ�����������
 * ���ܺ���ַ����������������Լ��ܺ���ַ������н��ܲ����ؽ��ܺ�������ַ�����
 */

public class AES
{
    private static String key = "cswfqxsarjgczzqa"; //������Կ Ĭ����ԿΪcswfqxs
    private static final IvParameterSpec IV_PARAMETER_SPEC = new IvParameterSpec("0000000000000000".getBytes());

    /**
     *  ���ܳ�ʮ�������ַ���
     *
     *  <p>
     *     ʹ��AES���ܣ�����Cipher���ܺ��byte����ת����16�����ַ���
     *  </p>
     *
     * @author Cr
     * @date 2020-03-22
     * */
    public static String encrypt(double data2){
        String data=""+data2;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"), IV_PARAMETER_SPEC);
            return bytesConvertHexString(cipher.doFinal(Arrays.copyOf(data.getBytes(), 16 * ((data.getBytes().length / 16) + 1))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * �����ܺ��ʮ�������ַ������н���
     *
     * @author Cr
     * @date 2020-03-22
     *
     * **/
    public static String decrypt(String data){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"), IV_PARAMETER_SPEC);
            return new String(cipher.doFinal(hexStringConvertBytes(data.toLowerCase())),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *  byte����ת����ʮ�������ַ���
     *
     *  <p>
     *      �ȶ�ÿ��byte��ֵ�����ʮ����,
     *      Ȼ���ڽ�ʮ����ת���ɶ�Ӧ��ʮ������.
     *      �������ת��, ʮ������ֻ��һλʱ�� ����ǰ��׷��0�����λ.
     *  </p>
     *
     * @author Cr
     * @date 2020-03-22
     * */
    private static String bytesConvertHexString(byte [] data){
        StringBuffer result = new StringBuffer();
        String hexString = "";
        for (byte b : data) {
            // �������ʮ���ƺ�ת����16����
            hexString = Integer.toHexString(b & 255);
            result.append(hexString.length() == 1 ? "0" + hexString : hexString);
        }
        return result.toString().toUpperCase();
    }

    /**
     * ʮ�������ַ���ת����byte����
     *
     *  <p>
     *      �ڼ���ʱ, ʮ��������ֵ��byte�ֽڵĶ�Ӧ��ϵ ��:  2��ʮ��������ֵ��Ӧ  1��byte�ֽ�  (2: 1)
     *      ����byte����ĳ���Ӧ����ʮ�������ַ�����һ��, ������ת��ʱ
     *      Ӧ������ʮ��������ֵת����һ��byte�ֽ�  (2��2��ʮ��������ֵ����ת��)
     *     ��Ҳ��Ϊʲô����*2��ԭ�� ����: 0, 2, 4, 6, 8, 10, 12 ���α���
     *  </p>
     *
     * @author Cr
     * @date 2020-04-22
     * */
    private static byte [] hexStringConvertBytes(String data){
        int length = data.length() / 2;
        byte [] result = new byte[length];
        for (int i = 0; i < length; i++) {
            int first = Integer.parseInt(data.substring(i * 2, i * 2 + 1), 16);
            int second = Integer.parseInt(data.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (first * 16 + second);
        }
        return result;
    }



//        //public static void setKey(String key)
//        {
//            keygen = key;
//        }

    /*    public static String encrypt(double num) //�����ַ��� ����contentΪ��Ҫ���ܵ�����
        {
            String content=""+num;
           // return content;
           // return content;

          //  System.out.println(System.getProperty("java.version"));
            try
            {
             //   KeyGenerator keyGen=KeyGenerator.getInstance("com.AES");
                KeyGenerator keyGen = new  KeyGenerator("com.AES");
                keyGen.init(128, new SecureRandom(keygen.getBytes()));
                SecretKey secretKey = keyGen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "com.AES"); // ����������
                Cipher cipher = Cipher.getInstance("com.AES");
                byte[] byteContent = content.getBytes("utf-8");
                cipher.init(Cipher.ENCRYPT_MODE, key); // ��ʼ��
                byte[] result = cipher.doFinal(byteContent);
                String code = parseByteToHexStr(result);
                return code;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static String decrypt(String content)  // �����ַ��� ����contentΪ��Ҫ���ܵ�����
        {
            //return content;
            byte[] code = parseHexStrToByte(content);
            try {
                KeyGenerator keyGen = KeyGenerator.getInstance("com.AES");
                keyGen.init(128, new SecureRandom(keygen.getBytes()));
                SecretKey secretKey = keyGen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "com.AES");
                Cipher cipher = Cipher.getInstance("com.AES");// ����������
                cipher.init(Cipher.DECRYPT_MODE, key);// ��ʼ��
                byte[] result = cipher.doFinal(code);
                try {
                    return new String(result, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();}
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static String parseByteToHexStr(byte buf[]) //��ת�ַ��� ��������
        {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < buf.length; i++)
            {
                String hex = Integer.toHexString(buf[i] & 0xFF);
                if (hex.length() == 1)
                {
                    hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
            }
            return sb.toString();
        }

        private static byte[] parseHexStrToByte(String hexStr)  //�ַ���ת�� ��������
        {
            if (hexStr.length() < 1)
            {
                return null;
            }
            byte[] result = new byte[hexStr.length() / 2];
            for (int i = 0; i < hexStr.length() / 2; i++)
            {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (high * 16 + low);
            }
            return result;
        }*/

}
