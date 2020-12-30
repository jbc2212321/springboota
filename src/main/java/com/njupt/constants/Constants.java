package com.njupt.constants;

import com.encrypty.SecretKey;
import com.njupt.rtree.Enc_RTNode;
import com.njupt.rtree.RTNode;

public class Constants {

	public static final int MAX_NUMBER_OF_ENTRIES_IN_NODE = 20;//结点中的最大条目数  
    public static final int MIN_NUMBER_OF_ENTRIES_IN_NODE = 8;//结点中的最小条目数  
      

    public static final int NIL = -1;  
    public static final RTNode RTNode_NULL = null;  
    public static final Enc_RTNode Enc_RTNode_NULL = null;  
    
    public static double ave_basic_enctime;
    public static double ave_basic_sertime;
    /**
     * advance生成trapdoor时间
     */
    public static double ave_advance_enctime;
    /**
     * 
     */
    public static double ave_advance_sertime;
    
    /**
     * 查询矩阵Q的列数
     */
    public static int MATRIX_Q_COL;
    
    //文件路径
    public static String file_path = "F:\\研一\\实验\\";
  	//数据文件名
    public static String data_file_name = "data1万t.txt";
  	//编码文件名
    public static String str_file_name="str.txt";
    
    public static SecretKey SK;
}
