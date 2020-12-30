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
	 * 对查询q构造一个查询向量Q
	 * @return
	 */
	public double[] BuildQueryVector(double q,SecretKey sk){
		int S=sk.getS();
		double[] array=new double[S];
		
		List<Integer> L=sk.getL();
		int count=L.size();
		Random random=new Random();
		int r=random.nextInt(S/4)+1;//随机生成r，用以在L中选择r对1和0
		List<Integer> index=new ArrayList<Integer>();//存儲01对位置
		while(true){
			int i=random.nextInt(S/4)+1;//随机生成01对的位置(确定选择第几对0，1)
			index.add(i);
			if(index.size()==r){
				break;
			}
		}
		Collections.sort(index);//将选择的01对的位置从小到大排序
		
		Map<Integer,Double> C=new HashMap<Integer,Double>();//存储随机数C 
		for(int i=0;i<r;i++){
			double ci=random.nextDouble()*S;//产生0-S之间的随机浮点数
			BigDecimal b=new BigDecimal(ci);  
			ci=b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
			C.put(index.get(i), ci);
		}
		
		int one=0;
		int zero=0;
		for(int i=0;i<count;i++){
			if(L.get(i)==0){//L[k]位置等于0则填充"q"
				array[count+i]=q;
				zero++;
				if(index.contains(zero)){
					array[i]=q*C.get(zero);
//					BigDecimal b=new BigDecimal(array[i]); 
//					array[i]=b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
				}else{
					array[i]=0;
				}
			}else{//L[k]位置等于1则填充"-1"
				array[count+i]=-1;
				one++;
				if(index.contains(one)){
					array[i]=-1*C.get(one);
//					BigDecimal b=new BigDecimal(array[i]); 
//					array[i]=b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
				}else{
					array[i]=0;
				}
			}
			
		}
		
		//Matrix Q=new Matrix(array,1);
		return array;
	}
	
	/**
	 * 对查询向量q构造一个查询矩阵Q
	 * @param vector_Q
	 * @return
	 */
	public double[][] BuildQueryMatrix(double[] vector_Q,SecretKey sk){
		int S=sk.getS();
		int col=Constants.MATRIX_Q_COL;
		double[][] matrix_Q=new double[S][col];
		Random random=new Random();
		
		double r=random.nextDouble()*5;//产生0~5的随机数，用以乘以向量Q
		r=Tools.Rounding(r, 2);
		//Q乘以r
		for(int i=0;i<S;i++){
			vector_Q[i]=vector_Q[i]*r;			
		}
		//生成矩阵Q
		double a=random.nextDouble()*5;////产生0~5的随机数，和作为矩阵行中倍数
		for(int i=0;i<S;i++){
			matrix_Q[i][0]=vector_Q[i];//vector_Q数据放入对角线位置
			double sum=0;
			for(int j=1;j<col-1;j++){
				double num=(random.nextDouble()*(a*matrix_Q[i][0]+1))-1;//产生0~a*matrix_Q[i][0]之间的随机数					
				num=Tools.Rounding(num, 2);//保留两位小数
					
				int sysbol=random.nextInt(2);//随机数符号，0为负1为正
				if(sysbol==0){
					num=-1*num;
				}
				matrix_Q[i][j]=num;
				sum+=num;
			}
			matrix_Q[i][col-1]=(matrix_Q[i][0]*a)-sum;//使每行数据和为真实数据的a倍
		}
		
		return matrix_Q;
	}
	
	/**
	 * 查询向量Q的加密处理
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
	 * 拆分矩阵Q，R中数据为0时进行拆分，为1则不拆分
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
		//拆分数据
		for(int i=0;i<S;i++){
			for(int j=0;j<col;j++){				
				if(R.get(i)==0){//R[i]位置等于0则不拆分
					array1[i][j]=array[i][j];					
					array2[i][j]=array[i][j];	
				}else{
					Random random=new Random();
					double c=(random.nextDouble()*(array[i][j]+array[i][j]+1))-array[i][j]-1;//产生-array[i]~array[i]之间的随机浮点数
					BigDecimal b1=new BigDecimal(c);  
					c=b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
					
					array1[i][j]=array[i][j]-c;
					array2[i][j]=array[i][j]-array1[i][j];
//					BigDecimal b2=new BigDecimal(array1[i][j]);  
//					array1[i][j]=b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数	
//					BigDecimal b3=new BigDecimal(array2[i][j]);  
//					array2[i][j]=b3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数	
				}
			}			
		}
		Matrix I_a=new Matrix(array1);//生成矩阵
		Matrix I_b=new Matrix(array2);//生成矩阵

		splitedMatQ.setVec_a(I_a);
		splitedMatQ.setVec_b(I_b);
		
		return splitedMatQ;
	}
	
	/**
	 * 拆分向量Q
	 * @param I
	 * @param sk
	 * @return
	 */
	public splitedMatrix splitQueryVector(double[] array,SecretKey sk){
		return Gen.splitVector(array, sk, 0);////0表示R中数据为0时进行拆分，为1则不拆分
	}
	
}
