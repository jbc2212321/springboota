package com.encrypty;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public class Query {

	public static int UP=1;//�������ڸ÷�Χ
	public static int IN=0;//�����ڸ÷�Χ����
	public static int DOWN=-1;//����С�ڸ÷�Χ
	
	List<Matrix> CQ=new ArrayList<Matrix>();
	
	/**
	 * �Ƚ�����������
	 * @param enc1
	 * @param enc2
	 * @return С��0��ʾenc1����enc2������0��ʾenc1С��enc2
	 */
	public static double Compare(splitedMatrix enc1,splitedMatrix enc2){
		
		double r1=Gen.splitedVector_times(enc1,enc2);
		return r1;
	}
	
	/**
	 * ��ѯ����
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
		if(r1*r2<0){//���������I��Q1��Q2֮��
			return IN;
		}
		if(r1<0&&r2<0){//����С��0���ʾ����i����q1��q2����i�ڷ�Χ֮��
			return UP;
		}else{//���ߴ���0��ʾ����iС��q1��q2
			return DOWN;
		}
	}
	
}
