package com.AVL_Tree;
import java.util.List;


public class Position_Encode {

	private Position pos;
	
	private List<Integer> encode_X;
	private List<Integer> encode_Y;
	
	
	public Position getPos() {
		return pos;
	}
	public void setPos(Position pos) {
		this.pos = pos;
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
