package com.encrypty;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public class Query {

	public static int UP=1;//索引大于该范围
	public static int IN=0;//索引在该范围以内
	public static int DOWN=-1;//索引小于该范围
	
	List<Matrix> CQ=new ArrayList<Matrix>();
	
	/**
	 * 比较两加密数据
	 * @param enc1
	 * @param enc2
	 * @return 小于0表示enc1大于enc2，大于0表示enc1小于enc2
	 */
	public static double Compare(splitedMatrix enc1,splitedMatrix enc2){
		
		double r1=Gen.splitedVector_times(enc1,enc2);
		return r1;
	}
	
	/**
	 * 查询操作
	 * @param Q1
	 * @param Q2
	 * @param I
	 * @return
	 */
	public int Search(splitedMatrix Q1,splitedMatrix Q2,splitedMatrix I){
		
		double r1=Gen.splitedVector_times(I, Q1);
		double r2=Gen.splitedVector_times(I, Q2);
		
//		System.out.println();
	//	System.out.println("r1:"+r1);
//		System.out.println("r2:"+r2);
		if(r1*r2<0){//两者异号则I在Q1和Q2之间
			return IN;
		}
		if(r1<0&&r2<0){//两者小于0则表示索引i大于q1和q2，及i在范围之上
			return UP;
		}else{//两者大于0表示索引i小于q1和q2
			return DOWN;
		}
	}
	
}
