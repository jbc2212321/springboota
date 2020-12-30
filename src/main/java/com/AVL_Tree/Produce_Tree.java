package com.AVL_Tree;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.encrypty.Tools;

public class Produce_Tree {
	//编码文件名
	private static String encode_file_name="encode.txt";
	//x树
	private  AVL_Tree tree_X=new AVL_Tree();
	//y树
	private  Map<Double,AVL_Tree> trees_Y=new HashMap<Double,AVL_Tree>();
	//
	private  List<Position_Encode> position_Encodes=new ArrayList<Position_Encode>();
	
	

	public  List<Position_Encode> getPosition_Encodes() {
		return position_Encodes;
	}



	/**
	 * 生成树结构
	 */
	public void produce_tree(String file_path,String data_file_name) {
		// TODO Auto-generated method stub		
		
		//从文件中获取数据
		List<Position> vals=Tools.getDataInFile(file_path+data_file_name);
		
		//构造X树
		int count=vals.size();
		for(int i=0;i<count;i++){
			tree_X.insert(vals.get(i).getX());
		}
		//构造Y树
		for(int i=0;i<count;i++){
			double x=vals.get(i).getX();
			if(!trees_Y.containsKey(x)){
				//没有相同x点的y树，构造一颗新树并插入trees_Y
				AVL_Tree tree=new AVL_Tree();
				tree.insert(vals.get(i).getY());
				trees_Y.put(x, tree);
			}else{
				//在已有的y树种插入新节点
				trees_Y.get(x).insert(vals.get(i).getY());
			}
			
		}
		
		System.out.println("X树：");
		//遍历输出树种数据
		tree_X.orderTraversal();
		
		System.out.println("Y树：");	
		//获取数据在X树的编码信息
		Map<Double, List<Integer>> encodes_X=tree_X.getEncoding();
		
		//获取数据在Y树的编码信息
		Map<Double,Map<Double, List<Integer>>> encodes_Y = new HashMap<Double,Map<Double, List<Integer>>>();
		Iterator keys=trees_Y.keySet().iterator();
		while(keys.hasNext()){
			Double x=(Double)keys.next();
			encodes_Y.put(x, trees_Y.get(x).getEncoding());
		}
		
		
		for(int i=0;i<vals.size();i++){
			Position_Encode pos_encode=new Position_Encode();
			pos_encode.setPos(vals.get(i));
			pos_encode.setEncode_X(encodes_X.get(vals.get(i).getX()));
			Map<Double, List<Integer>> encodes=encodes_Y.get(vals.get(i).getX());//获取同为x的y树编码信息
			pos_encode.setEncode_Y(encodes.get(vals.get(i).getY()));
			position_Encodes.add(pos_encode);
		}
		//编码数据写入文件中
		Tools.saveDataToFile(file_path+encode_file_name, position_Encodes);

	}


}
