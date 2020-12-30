package com.njupt.rtree;

import com.encrypty.Encrypty;
import com.encrypty.IndexEncrypty;
import com.encrypty.QueryEncrypty;
import com.encrypty.splitedMatrix;
import com.njupt.constants.Constants;

public class Node_Encrypty {

	/**
	 * 矩阵查询数据加密
	 * 
	 * @param rectangle
	 * @param isBasic 是否为basic方案
	 * @return
	 */
	public Enc_Rectangle rectangle_Enc_Query(Rectangle rectangle, boolean isBasic) {
		if (rectangle == null) {
			return null;
		}
		Enc_Rectangle enc_Rectangle;
		//加密左下角点
		Enc_Point enc_low = point_Enc(rectangle.getLow(), false,isBasic);
		//加密右上角点
		Enc_Point enc_high = point_Enc(rectangle.getHigh(), false,isBasic);
		//生成加密矩形
		enc_Rectangle = new Enc_Rectangle(enc_low, enc_high);
		return enc_Rectangle;
	}

	/**
	 * 矩阵索引数据加密
	 * 
	 * @param rectangle
	 * @return
	 */
	public Enc_Rectangle rectangle_Enc_Index(Rectangle rectangle, boolean isLeaf) {
		if (rectangle == null) {
			return null;
		}
		Enc_Rectangle enc_Rectangle;
		if (isLeaf) {// 叶子节点只需对一个点进行加密
			Enc_Point enc_low = point_Enc(rectangle.getLow(), true,false);
			enc_Rectangle = new Enc_Rectangle(enc_low);
		} else {// 非叶子节点需对两个点进行加密
			Enc_Point enc_low = point_Enc(rectangle.getLow(), true,false);
			Enc_Point enc_high = point_Enc(rectangle.getHigh(), true,false);
			enc_Rectangle = new Enc_Rectangle(enc_low, enc_high);
		}
		return enc_Rectangle;
	}

	/**
	 * 点加密
	 * 
	 * @param point
	 * @param isIndex是否為索引，否則為查詢、
	 * @param isBasic是否为basic方法
	 * @return
	 */
	private Enc_Point point_Enc(Point point, boolean isIndex, boolean isBasic) {
		Enc_Point enc_Point;
		Encrypty encrypty = new Encrypty();
		splitedMatrix[] enc_data = new splitedMatrix[point.getDimension()];
		// 分别对xy进行加密
		for (int i = 0; i < point.getDimension(); i++) {
			double d = point.getDoubleCoordinate(i);
			if (isIndex == true) {
				enc_data[i] = encrypty.indexDataEncrypty(d);
			} else {
				if (isBasic) {
					enc_data[i] = encrypty.basic_QueryDataEncrypty(d);
				} else {
					enc_data[i] = encrypty.advance_QueryDataEncrypty(d);
				}
			}
		}
		enc_Point = new Enc_Point(enc_data);
		return enc_Point;
	}

}
