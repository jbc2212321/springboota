package com.njupt.constants;

import com.encrypty.SecretKey;
import com.njupt.rtree.Enc_RTNode;
import com.njupt.rtree.RTNode;

public class Constants {

	public static final int MAX_NUMBER_OF_ENTRIES_IN_NODE = 20;//����е������Ŀ��  
    public static final int MIN_NUMBER_OF_ENTRIES_IN_NODE = 8;//����е���С��Ŀ��  
      

    public static final int NIL = -1;  
    public static final RTNode RTNode_NULL = null;  
    public static final Enc_RTNode Enc_RTNode_NULL = null;  
    
    public static double ave_basic_enctime;
    public static double ave_basic_sertime;
    /**
     * advance����trapdoorʱ��
     */
    public static double ave_advance_enctime;
    /**
     * 
     */
    public static double ave_advance_sertime;
    
    /**
     * ��ѯ����Q������
     */
    public static int MATRIX_Q_COL;
    
    //�ļ�·��
    public static String file_path = "F:\\��һ\\ʵ��\\";
  	//�����ļ���
    public static String data_file_name = "data1��t.txt";
  	//�����ļ���
    public static String str_file_name="str.txt";
    
    public static SecretKey SK;
}
