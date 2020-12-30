package com.encrypty;

import com.njupt.constants.Constants;
/**
 * 加密处理类
 * @author SHENJUN
 *
 */
public class Encrypty {

	/**
	 * 索引数据加密
	 * @param d
	 * @return
	 */
	public splitedMatrix indexDataEncrypty(double d){
		if(Constants.SK==null){
			throw new IllegalArgumentException("SK cannot be null.");
		}
		// 构造I
		IndexEncrypty indexEnc = new IndexEncrypty();
		double[] I = indexEnc.BuildIndexVector(d, Constants.SK);

		// 拆分I
		splitedMatrix splited_I = indexEnc.splitIndexVector(I, Constants.SK);
		// 加密I
		splitedMatrix encI = indexEnc.EncIndexVector(splited_I, Constants.SK);
		
		return encI;
	}
	
	
	/**
	 * 查詢数据加密
	 * @param d
	 * @return
	 */
	public splitedMatrix advance_QueryDataEncrypty(double d){
		if(Constants.SK==null){
			throw new IllegalArgumentException("SK cannot be null.");
		}
		/// 构造查询向量Q
		QueryEncrypty queryEnc = new QueryEncrypty();
		double[] Q = queryEnc.BuildQueryVector(d, Constants.SK);
		//构造查询矩阵
		double[][] M_Q=queryEnc.BuildQueryMatrix(Q, Constants.SK);
		// 拆分查询矩阵Q
		splitedMatrix splited_Q = queryEnc.splitedQueryMatrix(M_Q, Constants.SK);
		// 加密
		splitedMatrix encQ = queryEnc.EncQueryMatrix(splited_Q, Constants.SK);
		
		//System.out.println(encQ);
		return encQ;
	}
	
	/**
	 * 查詢数据加密
	 * @param d
	 * @return
	 */
	public splitedMatrix basic_QueryDataEncrypty(double d){
		if(Constants.SK==null){
			throw new IllegalArgumentException("SK cannot be null.");
		}
		/// 构造查询向量Q
		QueryEncrypty queryEnc = new QueryEncrypty();
		double[] Q = queryEnc.BuildQueryVector(d, Constants.SK);
		// 拆分查询向量Q
		splitedMatrix splited_Q = queryEnc.splitQueryVector(Q, Constants.SK);
		splitedMatrix encQ = queryEnc.EncQueryVector(splited_Q, Constants.SK);
		
		return encQ;
	}
	
}
