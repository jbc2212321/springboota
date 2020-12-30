package com.encrypty;

import java.util.List;

import Jama.Matrix;

public class Enc_Encode {
	
	private Matrix Enc_X;//X点密文
	private Matrix Enc_Y;//Y点密文
	 
	private List<Integer> encode_X;//X編碼
	private List<Integer> encode_Y;//Y編碼
	public Matrix getEnc_X() {
		return Enc_X;
	}
	public void setEnc_X(Matrix enc_X) {
		Enc_X = enc_X;
	}
	public Matrix getEnc_Y() {
		return Enc_Y;
	}
	public void setEnc_Y(Matrix enc_Y) {
		Enc_Y = enc_Y;
	}
	public List<Integer> getEncode_X() {
		return encode_X;
	}
	public void setEncode_X(List<Integer> encode_X) {
		this.encode_X = encode_X;
	}
	public List<Integer> getEncode_Y() {
		return encode_Y;
	}
	public void setEncode_Y(List<Integer> encode_Y) {
		this.encode_Y = encode_Y;
	}
	
	private String enc_Matrix_toString(Matrix matrix){
		String str="";
		for(int i=0;i<matrix.getColumnDimension();i++){
			if(i!=0){
				str+=",";
			}
			str+=matrix.get(0, i);			
		}
		return str;
	}
	
	public String enc_X_toString(){
		return enc_Matrix_toString(Enc_X);
	}
	
	public String enc_Y_toString(){
		return enc_Matrix_toString(Enc_Y);
	}
	
	private String encode_toString(List<Integer> encodes){
		String encode="";
		for (int i = 0; i < encodes.size(); i++) {
			encode+=encodes.get(i).toString();		
	    }
		return encode;
	}
	
	public String encode_X_toString(){
		return encode_toString(encode_X);
	}
	
	public String encode_Y_toStrig(){
		return encode_toString(encode_Y);
	}

}
