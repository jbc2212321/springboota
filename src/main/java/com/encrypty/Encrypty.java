package com.encrypty;

import com.njupt.constants.Constants;
/**
 * ���ܴ�����
 * @author SHENJUN
 *
 */
public class Encrypty {

	/**
	 * �������ݼ���
	 * @param d
	 * @return
	 */
	public splitedMatrix indexDataEncrypty(double d){
		if(Constants.SK==null){
			throw new IllegalArgumentException("SK cannot be null.");
		}
		// ����I
		IndexEncrypty indexEnc = new IndexEncrypty();
		double[] I = indexEnc.BuildIndexVector(d, Constants.SK);

		// ���I
		splitedMatrix splited_I = indexEnc.splitIndexVector(I, Constants.SK);
		// ����I
		splitedMatrix encI = indexEnc.EncIndexVector(splited_I, Constants.SK);
		
		return encI;
	}
	
	
	/**
	 * ��ԃ���ݼ���
	 * @param d
	 * @return
	 */
	public splitedMatrix advance_QueryDataEncrypty(double d){
		if(Constants.SK==null){
			throw new IllegalArgumentException("SK cannot be null.");
		}
		/// �����ѯ����Q
		QueryEncrypty queryEnc = new QueryEncrypty();
		double[] Q = queryEnc.BuildQueryVector(d, Constants.SK);
		//�����ѯ����
		double[][] M_Q=queryEnc.BuildQueryMatrix(Q, Constants.SK);
		// ��ֲ�ѯ����Q
		splitedMatrix splited_Q = queryEnc.splitedQueryMatrix(M_Q, Constants.SK);
		// ����
		splitedMatrix encQ = queryEnc.EncQueryMatrix(splited_Q, Constants.SK);
		
		//System.out.println(encQ);
		return encQ;
	}
	
	/**
	 * ��ԃ���ݼ���
	 * @param d
	 * @return
	 */
	public splitedMatrix basic_QueryDataEncrypty(double d){
		if(Constants.SK==null){
			throw new IllegalArgumentException("SK cannot be null.");
		}
		/// �����ѯ����Q
		QueryEncrypty queryEnc = new QueryEncrypty();
		double[] Q = queryEnc.BuildQueryVector(d, Constants.SK);
		// ��ֲ�ѯ����Q
		splitedMatrix splited_Q = queryEnc.splitQueryVector(Q, Constants.SK);
		splitedMatrix encQ = queryEnc.EncQueryVector(splited_Q, Constants.SK);
		
		return encQ;
	}
	
}
