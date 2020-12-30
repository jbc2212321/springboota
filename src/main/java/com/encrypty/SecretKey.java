package com.encrypty;

import java.io.Serializable;
import java.util.List;

import Jama.Matrix;

public class SecretKey implements Serializable {
	
	private Matrix M1,M2;      //加密矩阵
	private Matrix M1_Inverse,M2_Inverse;      //加密逆矩阵
	public Matrix getM1_Inverse() {
		return M1_Inverse;
	}
	public void setM1_Inverse(Matrix m1_Inverse) {
		M1_Inverse = m1_Inverse;
	}
	public Matrix getM2_Inverse() {
		return M2_Inverse;
	}
	public void setM2_Inverse(Matrix m2_Inverse) {
		M2_Inverse = m2_Inverse;
	}
	private List<Integer> L;   //向量填充依据
	private List<Integer> R;   //向量拆分依据
	private int S;             //向量size
	
	public Matrix getM1() {
		return M1;
	}
	public void setM1(Matrix m1) {
		M1 = m1;
	}
	public Matrix getM2() {
		return M2;
	}
	public void setM2(Matrix m2) {
		M2 = m2;
	}
	public List<Integer> getL() {
		return L;
	}
	public void setL(List<Integer> l) {
		L = l;
	}
	public int getS() {
		return S;
	}
	public void setS(int s) {
		S = s;
	}
	public List<Integer> getR() {
		return R;
	}
	public void setR(List<Integer> r) {
		R = r;
	}
	private String MatrixToString(Matrix Vec_a){
	//	String s=" ";
		StringBuffer sBuffer = new StringBuffer("[");
	//	sBuffer.append("(");
		for (int i = 0; i < Vec_a.getRowDimension(); i++) {
			for (int j = 0; j < Vec_a.getColumnDimension() - 1; j++) {
				// System.out.print(Vec_a.get(0, i) + ", ");
				sBuffer.append(Vec_a.get(i, j)).append(",");
			}
			sBuffer.append(Vec_a.get(i, Vec_a.getColumnDimension() - 1)).append("\n");
		}
		sBuffer.deleteCharAt(sBuffer.length()-1);
	//	sBuffer.append(")");

		sBuffer.deleteCharAt(sBuffer.length()-1);
		sBuffer.append("]");
		return  sBuffer.toString();
	}
	public String toString(){
		return MatrixToString(M1)+" "+
				MatrixToString(M2)+" "+
				 MatrixToString(M1_Inverse)+" "+ MatrixToString(M2_Inverse)
				+" "+L.toString()+" "+R.toString()+" "+S;
	}
	

}
