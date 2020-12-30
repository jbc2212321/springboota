package com.encrypty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Jama.Matrix;

public class IndexEncrypty {

	/**
	 * 对索引D构造一个索引向量I
	 * @param D
	 * @param sk
	 * @return
	 */
	public double[] BuildIndexVector(double D,SecretKey sk){
		int S=sk.getS();
		double[] array=new double[S];

		List<Integer> L=sk.getL();
		int count=L.size();
		Random random=new Random();
		int r=random.nextInt(S/4)+1;//随机生成r，用以在L中选择r对1和0
		List<Integer> index=new ArrayList<Integer>();//存儲01对位置
		while(true){
			int i=random.nextInt(S/4)+1;//随机生成01对位置
			index.add(i);
			if(index.size()==r){
				break;
			}
		}
		Collections.sort(index);//将01对位置从小到大排序

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
			if(L.get(i)==0){//L[k]位置等于0则填充"1"
				array[i]=1;
				zero++;
				if(index.contains(zero)){
					array[count+i]=1*C.get(zero);
//					BigDecimal b=new BigDecimal(array[count+i]);  
//					array[count+i]=b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
				}else{
					array[count+i]=0;
				}
			}else{//L[k]位置等于1则填充"D"
				array[i]=D;
				one++;
				if(index.contains(one)){
					array[count+i]=D*C.get(one);
//					BigDecimal b=new BigDecimal(array[count+i]);  
//					array[count+i]=b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数					
				}else{
					array[count+i]=0;
				}
			}

		}	


		return array;
	}

	/**
	 * 拆分向量I
	 * @param I
	 * @param sk
	 * @return
	 */
	public splitedMatrix splitIndexVector(double[] array,SecretKey sk){
		return Gen.splitVector(array, sk, 1);//1表示R中数据为1时进行拆分，为0则不拆分
//		int S=sk.getS();
//		double[] array1=new double[S];
//		double[] array2=new double[S];
//		splitedVector splitedVecI = new splitedVector();
//		List<Integer> R = sk.getR();
//		int R_size=R.size();
//		for(int i=0;i<R_size;i++){
//			if(R.get(i)==0){//R[i]位置等于0则不拆分I
//				array1[i]=array[i];					
//				array2[i]=array[i];	
//			}
//			else {
//				Random random=new Random();
//				double c=(random.nextDouble()*array[i]*2)-2;//产生-array1[i]~array1[i]之间的随机浮点数
//				BigDecimal b1=new BigDecimal(c);  
//				c=b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
//
//				array1[i]=array[i]-c;
//				array2[i]=array[i]-array1[i];
//				BigDecimal b2=new BigDecimal(array1[i]);  
//				array1[i]=b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数	
//				BigDecimal b3=new BigDecimal(array2[i]);  
//				array2[i]=b3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数	
//			}
//		}		
//
//		Matrix I_a=new Matrix(array1, 1);//生成1*S的矩阵
//		Matrix I_b=new Matrix(array2, 1);//生成1*S的矩阵
//
//		splitedVecI.setVec_a(I_a);
//		splitedVecI.setVec_b(I_b);
//
//		return splitedVecI;
	}


	/**
	 * 向量I的加密处理
	 * @param i
	 * @param sk
	 * @return
	 */			
	public splitedMatrix EncIndexVector(splitedMatrix splited_I,SecretKey sk){
		splitedMatrix EncI=new splitedMatrix();	
		EncI.setVec_a(splited_I.getVec_a().times(sk.getM1()));
		EncI.setVec_b(splited_I.getVec_b().times(sk.getM2()));	
		return EncI;
	}

}
