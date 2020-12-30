package com.AVL_Tree;

public class TreeNode {

	private double val;
	private int height;
	private TreeNode lson;
	private TreeNode rson;

	public TreeNode(double v,int h){
		this.val=v;
		this.height=h;
		this.lson=null;
		this.rson=null;
	}
	
	public double getVal() {
		return val;
	}

	public void setVal(double val) {
		this.val = val;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public TreeNode getLson() {
		return lson;
	}

	public void setLson(TreeNode lson) {
		this.lson = lson;
	}

	public TreeNode getRson() {
		return rson;
	}

	public void setRson(TreeNode rson) {
		this.rson = rson;
	}

}
