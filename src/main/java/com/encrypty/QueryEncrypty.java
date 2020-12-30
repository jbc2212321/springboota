package com.encrypty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.njupt.constants.Constants;

import Jama.Matrix;


public class QueryEncrypty {

	/**
	 * �Բ�ѯq����һ����ѯ����Q
	 * @return
	 */
	public double[] BuildQueryVector(double q,SecretKey sk){
		int S=sk.getS();
		double[] array=new double[S];
		
		List<Integer> L=sk.getL();
		int count=L.size();
		Random random=new Random();
		int r=random.nextInt(S/4)+1;//�������r��������L��ѡ��r��1��0
		List<Integer> index=new ArrayList<Integer>();//�惦01��λ��
		while(true){
			int i=random.nextInt(S/4)+1;//�������01�Ե�λ��(ȷ��ѡ��ڼ���0��1)
			index.add(i);
			if(index.size()==r){
				break;
			}
		}
		Collections.sort(index);//��ѡ���01�Ե�λ�ô�С��������
		
		Map<Integer,Double> C=new HashMap<Integer,Double>();//�洢�����C 
		for(int i=0;i<r;i++){
			double ci=random.nextDouble()*S;//����0-S֮������������
			BigDecimal b=new BigDecimal(ci);  
			ci=b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//������λС��
			C.put(index.get(i), ci);
		}
		
		int one=0;
		int zero=0;
		for(int i=0;i<count;i++){
			if(L.get(i)==0){//L[k]λ�õ���0�����"q"
				array[count+i]=q;
				zero++;
				if(index.contains(zero)){
					array[i]=q*C.get(zero);
//					BigDecimal b=new BigDecimal(array[i]); 
//					array[i]=b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//������λС��
				}else{
					array[i]=0;
				}
			}else{//L[k]λ�õ���1�����"-1"
				array[count+i]=-1;
				one++;
				if(index.contains(one)){
					array[i]=-1*C.get(one);
//					BigDecimal b=new BigDecimal(array[i]); 
//					array[i]=b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//������λС��
				}else{
					array[i]=0;
				}
			}
			
		}
		
		//Matrix Q=new Matrix(array,1);
		return array;
	}
	
	/**
	 * �Բ�ѯ����q����һ����ѯ����Q
	 * @param vector_Q
	 * @return
	 */
	public double[][] BuildQueryMatrix(double[] vector_Q,SecretKey sk){
		int S=sk.getS();
		int col=Constants.MATRIX_Q_COL;
		double[][] matrix_Q=new double[S][col];
		Random random=new Random();
		
		double r=random.nextDouble()*5;//����0~5������������Գ�������Q
		r=Tools.Rounding(r, 2);
		//Q����r
		for(int i=0;i<S;i++){
			vector_Q[i]=vector_Q[i]*r;			
		}
		//���ɾ���Q
		double a=random.nextDouble()*5;////����0~5�������������Ϊ�������б���
		for(int i=0;i<S;i++){
			matrix_Q[i][0]=vector_Q[i];//vector_Q���ݷ���Խ���λ��
			double sum=0;
			for(int j=1;j<col-1;j++){
				double num=(random.nextDouble()*(a*matrix_Q[i][0]+1))-1;//����0~a*matrix_Q[i][0]֮��������					
				num=Tools.Rounding(num, 2);//������λС��
					
				int sysbol=random.nextInt(2);//��������ţ�0Ϊ��1Ϊ��
				if(sysbol==0){
					num=-1*num;
				}
				matrix_Q[i][j]=num;
				sum+=num;
			}
			matrix_Q[i][col-1]=(matrix_Q[i][0]*a)-sum;//ʹÿ�����ݺ�Ϊ��ʵ���ݵ�a��
		}
		
		return matrix_Q;
	}
	
	/**
	 * ��ѯ����Q�ļ��ܴ���
	 * @param Q
	 * @param sk
	 * @return
	 */
	public splitedMatrix EncQueryVector(splitedMatrix splited_Q,SecretKey sk){
		splitedMatrix EncQ=new splitedMatrix();
		EncQ.setVec_a(sk.getM1_Inverse().times(splited_Q.getVec_a().transpose()));
		EncQ.setVec_b(sk.getM2_Inverse().times(splited_Q.getVec_b().transpose()));
//		EncQ.setVec_a(sk.getM1().inverse().times(splited_Q.getVec_a().transpose()));
//		EncQ.setVec_b(sk.getM2().inverse().times(splited_Q.getVec_b().transpose()));
		return EncQ;
	}
	
	public splitedMatrix EncQueryMatrix(splitedMatrix splited_Q,SecretKey sk){
		splitedMatrix EncQ=new splitedMatrix();
		EncQ.setVec_a(sk.getM1_Inverse().times(splited_Q.getVec_a()));
		EncQ.setVec_b(sk.getM2_Inverse().times(splited_Q.getVec_b()));
		return EncQ;
	}
	
	/**
	 * ��־���Q��R������Ϊ0ʱ���в�֣�Ϊ1�򲻲��
	 * @param q
	 * @param sk
	 * @return
	 */
	public splitedMatrix splitedQueryMatrix(double[][] array,SecretKey sk){
		int S=sk.getS();
		int col=Constants.MATRIX_Q_COL;
		double[][] array1=new double[S][col];
		double[][] array2=new double[S][col];
		splitedMatrix splitedMatQ = new splitedMatrix();
		List<Integer> R = sk.getR();
		int R_size=R.size();
		//�������
		for(int i=0;i<S;i++){
			for(int j=0;j<col;j++){				
				if(R.get(i)==0){//R[i]λ�õ���0�򲻲��
					array1[i][j]=array[i][j];					
					array2[i][j]=array[i][j];	
				}else{
					Random random=new Random();
					double c=(random.nextDouble()*(array[i][j]+array[i][j]+1))-array[i][j]-1;//����-array[i]~array[i]֮������������
					BigDecimal b1=new BigDecimal(c);  
					c=b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//������λС��
					
					array1[i][j]=array[i][j]-c;
					array2[i][j]=array[i][j]-array1[i][j];
//					BigDecimal b2=new BigDecimal(array1[i][j]);  
//					array1[i][j]=b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//������λС��	
//					BigDecimal b3=new BigDecimal(array2[i][j]);  
//					array2[i][j]=b3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//������λС��	
				}
			}			
		}
		Matrix I_a=new Matrix(array1);//���ɾ���
		Matrix I_b=new Matrix(array2);//���ɾ���

		splitedMatQ.setVec_a(I_a);
		splitedMatQ.setVec_b(I_b);
		
		return splitedMatQ;
	}
	
	/**
	 * �������Q
	 * @param I
	 * @param sk
	 * @return
	 */
	public splitedMatrix splitQueryVector(double[] array,SecretKey sk){
		return Gen.splitVector(array, sk, 0);////0��ʾR������Ϊ0ʱ���в�֣�Ϊ1�򲻲��
	}
	
}
