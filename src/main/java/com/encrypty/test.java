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
	// 存放数据密文及其编码
	private static List<Enc_Encode> enc_Encodes = new ArrayList<Enc_Encode>();

	public static void main(String[] args) {
		int[] d = {128};//矩阵维度（偶数）

		//XY生成搜索区域
		double[][] XY = { { 30, 40 }, { 40, 35 }, { 35, 45 }, { 45, 70 },
				{ 55, 60 }, { 30, 75 }, { 38, 88 }, { 45, 90 }, { 50, 58 },
				{ 40, 70 } };
		//step搜索区域的范围大小
		double[] step = { 5, 10, 15, 20, 25, 30, 35, 40, 45, 50 };
		//循环数组d，测试不同维度中的性能
		for (int x = 0; x < 1; x++) {
			/**数据准备 开始**/
			//根据数据维度生成可逆矩阵
			Constants.SK = Gen.GenKey(d[x]);// 参数需为偶数
			//生成RTree明文结构
			RTree rtree = RTree.produce_Rtree(Constants.file_path,
					Constants.data_file_name);
			/**数据准备 结束**/
			
			/**数据加密 开始**/
			// 加密测试
			double ave_time = 0;
			long b = System.currentTimeMillis();
			//根据明文树结构生成密文树
			Enc_RTNode enc_root = rtree.produce_EncRtree(rtree.root);
			ave_time= (System.currentTimeMillis() - b) / 1f;
			System.out.println("平均加密执行时间：" + ave_time + "ms");
			/**数据加密 结束**/
			
			/**basic查询 开始**/
//			System.out.println("----------++++++++++++--------------");
//			for (int s = 0; s < step.length; s++) {
//				Constants.ave_basic_enctime = 0; // basic 生成trapdoor时间
//				Constants.ave_basic_sertime = 0; // basic 执行查询时间
//				System.out.println("basic，查询大小为" + (s + 1) + "时:");
//				double[][] times = new double[2][XY.length];
//				for (int i = 0; i < XY.length; i++) {
//
//					double arr[] = Gen.GenArr(XY[i][0], XY[i][1], step[s]);
//					// basic密文区域查询
//					rtree.basic_enc_search(enc_root, arr[0], arr[1], arr[2],
//							arr[3]);
//					times[0][i] = Constants.ave_basic_enctime;
//					times[1][i] = Constants.ave_basic_sertime;
//					// System.out.println();
//
//				}
//				System.out.print("trapdoor十次生成时间:");
//				for (int i = 0; i < XY.length; i++) {
//					System.out.print(times[0][i] + "\t");
//				}
//				System.out.println();
//				System.out.print("basic十次查询执行时间:");
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
//				// System.out.println("basic生成trapdoor执行时间："+
//				// Constants.ave_basic_enctime + "ms");
//				// System.out.println("basic查询执行时间："+
//				// Constants.ave_basic_sertime + "ms");
//				System.out.println("----------++++++++++++--------------");
//			}
			/**basic查询 结束**/
			
			
			/**advance查询 开始**/
			// advance查询测试
			System.out.println("----------------***********---------------");
			int[] col_num = { 2,16,32,48,64};//查询矩阵Q的列数
			for (int j = 0; j < 5; j++) {
				Constants.MATRIX_Q_COL = col_num[j];
				System.out.println("advance，s=" + col_num[j] + "时:");
				double[][] times = new double[2][10];
				for (int s = 0; s < step.length; s++) {

					Constants.ave_advance_enctime = 0; // advance生成trapdoor时间
					Constants.ave_advance_sertime = 0; // advance执行查询时间
					System.out.println("查询大小为" + (s + 1) + "时:");
					for (int i = 0; i < XY.length; i++) {
						//生成搜索区域
						double arr[] = Gen.GenArr(XY[i][0], XY[i][1], step[s]);
						//密文树的搜索
						rtree.advance_enc_search(enc_root, arr[0], arr[1],
								arr[2], arr[3]);
						
						 times[0][i]=Constants.ave_advance_enctime;
						 times[1][i]=Constants.ave_advance_sertime;

					}

					System.out.print("trapdoor十次生成时间:");
					for (int i = 0; i < 10; i++) {
						System.out.print(times[0][i] + "\t");
					}
					System.out.println();
//					System.out.print("advance十次查询执行时间:");
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
//							+ "时，生成trapdoor平均时间："
//							+ Constants.ave_advance_enctime + "ms");
//					System.out.println("\t 平均查询执行时间："
//							+ Constants.ave_advance_sertime + "ms");

				}
				System.out.println("----------*************--------------");
			}
			/**advance查询 结束**/
			System.out.println();
			System.out.println("--------------------------------------------");
			System.out.println();

		}

	}

}
