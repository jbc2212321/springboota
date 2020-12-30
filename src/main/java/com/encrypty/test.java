package com.encrypty;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.njupt.constants.Constants;
import com.njupt.rtree.Enc_RTNode;
import com.njupt.rtree.Enc_Rectangle;
import com.njupt.rtree.RTNode;
import com.njupt.rtree.RTree;
import com.njupt.rtree.Rectangle;

import Jama.Matrix;

public class test {
	// ����������ļ������
	private static List<Enc_Encode> enc_Encodes = new ArrayList<Enc_Encode>();

	public static void main(String[] args) {
		int[] d = {128};//����ά�ȣ�ż����

		//XY������������
		double[][] XY = { { 30, 40 }, { 40, 35 }, { 35, 45 }, { 45, 70 },
				{ 55, 60 }, { 30, 75 }, { 38, 88 }, { 45, 90 }, { 50, 58 },
				{ 40, 70 } };
		//step��������ķ�Χ��С
		double[] step = { 5, 10, 15, 20, 25, 30, 35, 40, 45, 50 };
		//ѭ������d�����Բ�ͬά���е�����
		for (int x = 0; x < 1; x++) {
			/**����׼�� ��ʼ**/
			//��������ά�����ɿ������
			Constants.SK = Gen.GenKey(d[x]);// ������Ϊż��
			//����RTree���Ľṹ
			RTree rtree = RTree.produce_Rtree(Constants.file_path,
					Constants.data_file_name);
			/**����׼�� ����**/
			
			/**���ݼ��� ��ʼ**/
			// ���ܲ���
			double ave_time = 0;
			long b = System.currentTimeMillis();
			//�����������ṹ����������
			Enc_RTNode enc_root = rtree.produce_EncRtree(rtree.root);
			ave_time= (System.currentTimeMillis() - b) / 1f;
			System.out.println("ƽ������ִ��ʱ�䣺" + ave_time + "ms");
			/**���ݼ��� ����**/
			
			/**basic��ѯ ��ʼ**/
//			System.out.println("----------++++++++++++--------------");
//			for (int s = 0; s < step.length; s++) {
//				Constants.ave_basic_enctime = 0; // basic ����trapdoorʱ��
//				Constants.ave_basic_sertime = 0; // basic ִ�в�ѯʱ��
//				System.out.println("basic����ѯ��СΪ" + (s + 1) + "ʱ:");
//				double[][] times = new double[2][XY.length];
//				for (int i = 0; i < XY.length; i++) {
//
//					double arr[] = Gen.GenArr(XY[i][0], XY[i][1], step[s]);
//					// basic���������ѯ
//					rtree.basic_enc_search(enc_root, arr[0], arr[1], arr[2],
//							arr[3]);
//					times[0][i] = Constants.ave_basic_enctime;
//					times[1][i] = Constants.ave_basic_sertime;
//					// System.out.println();
//
//				}
//				System.out.print("trapdoorʮ������ʱ��:");
//				for (int i = 0; i < XY.length; i++) {
//					System.out.print(times[0][i] + "\t");
//				}
//				System.out.println();
//				System.out.print("basicʮ�β�ѯִ��ʱ��:");
//				for (int i = 0; i < XY.length; i++) {
//					System.out.print(times[1][i] + "\t");
//				}
//				System.out.println();
//
//				// Constants.ave_basic_enctime =
//				// Constants.ave_basic_enctime/XY.length;
//				// Constants.ave_basic_sertime =
//				// Constants.ave_basic_sertime/XY.length;
//				//
//				// System.out.println("basic����trapdoorִ��ʱ�䣺"+
//				// Constants.ave_basic_enctime + "ms");
//				// System.out.println("basic��ѯִ��ʱ�䣺"+
//				// Constants.ave_basic_sertime + "ms");
//				System.out.println("----------++++++++++++--------------");
//			}
			/**basic��ѯ ����**/
			
			
			/**advance��ѯ ��ʼ**/
			// advance��ѯ����
			System.out.println("----------------***********---------------");
			int[] col_num = { 2,16,32,48,64};//��ѯ����Q������
			for (int j = 0; j < 5; j++) {
				Constants.MATRIX_Q_COL = col_num[j];
				System.out.println("advance��s=" + col_num[j] + "ʱ:");
				double[][] times = new double[2][10];
				for (int s = 0; s < step.length; s++) {

					Constants.ave_advance_enctime = 0; // advance����trapdoorʱ��
					Constants.ave_advance_sertime = 0; // advanceִ�в�ѯʱ��
					System.out.println("��ѯ��СΪ" + (s + 1) + "ʱ:");
					for (int i = 0; i < XY.length; i++) {
						//������������
						double arr[] = Gen.GenArr(XY[i][0], XY[i][1], step[s]);
						//������������
						rtree.advance_enc_search(enc_root, arr[0], arr[1],
								arr[2], arr[3]);
						
						 times[0][i]=Constants.ave_advance_enctime;
						 times[1][i]=Constants.ave_advance_sertime;

					}

					System.out.print("trapdoorʮ������ʱ��:");
					for (int i = 0; i < 10; i++) {
						System.out.print(times[0][i] + "\t");
					}
					System.out.println();
//					System.out.print("advanceʮ�β�ѯִ��ʱ��:");
//					for (int i = 0; i < 10; i++) {
//						System.out.print(times[1][i] + "\t");
//					}
//					System.out.println();
//					Constants.ave_advance_enctime = Constants.ave_advance_enctime
//							/ XY.length;
//					Constants.ave_advance_sertime = Constants.ave_advance_sertime
//							/ XY.length;
//
//					System.out.print("advance,s=" + col_num[j]
//							+ "ʱ������trapdoorƽ��ʱ�䣺"
//							+ Constants.ave_advance_enctime + "ms");
//					System.out.println("\t ƽ����ѯִ��ʱ�䣺"
//							+ Constants.ave_advance_sertime + "ms");

				}
				System.out.println("----------*************--------------");
			}
			/**advance��ѯ ����**/
			System.out.println();
			System.out.println("--------------------------------------------");
			System.out.println();

		}

	}

}
