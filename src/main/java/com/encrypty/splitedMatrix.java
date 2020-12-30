package com.encrypty;

import Jama.Matrix;

import java.io.Serializable;

public class splitedMatrix implements Serializable {
	private Matrix Vec_a;
	private Matrix Vec_b;

	public Matrix getVec_a() {
		return Vec_a;
	}

	public void setVec_a(Matrix vec_a) {
		Vec_a = vec_a;
	}

	public Matrix getVec_b() {
		return Vec_b;
	}

	public void setVec_b(Matrix vec_b) {
		Vec_b = vec_b;
	}

	public splitedMatrix transpose() {
		Vec_a = Vec_a.transpose();
		Vec_b = Vec_b.transpose();
		return this;
	}

	public String toString() {

		StringBuffer sBuffer = new StringBuffer("[");
		sBuffer.append("(");
		for (int i = 0; i < Vec_a.getRowDimension(); i++) {
			for (int j = 0; j < Vec_a.getColumnDimension() - 1; j++) {
				// System.out.print(Vec_a.get(0, i) + ", ");
				sBuffer.append(Vec_a.get(i, j)).append(",");
			}
			sBuffer.append(Vec_a.get(i, Vec_a.getColumnDimension() - 1)).append("\n");
		}
		sBuffer.deleteCharAt(sBuffer.length()-1);
		sBuffer.append(")");
		sBuffer.append(",").append("(");
		for (int i = 0; i < Vec_a.getRowDimension(); i++) {
			for (int j = 0; j < Vec_b.getColumnDimension() - 1; j++) {
				// System.out.print(Vec_a.get(0, i) + ", ");
				sBuffer.append(Vec_b.get(i, j)).append(",");
			}
			sBuffer.append(Vec_b.get(i, Vec_b.getColumnDimension() - 1)).append("\n");
		}
		sBuffer.deleteCharAt(sBuffer.length()-1);
		sBuffer.append(")").append("]");
		return sBuffer.toString();
	}

}
