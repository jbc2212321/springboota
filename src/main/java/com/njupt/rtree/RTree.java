package com.njupt.rtree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.AVL_Tree.Position;
import com.encrypty.Tools;
import com.njupt.constants.Constants;

/**
 * @ClassName RTree
 * @Description
 */
public class RTree {
	/**
	 * 根节点
	 */
	public RTNode root;

	public Enc_RTNode Enc_root;
	
	/**
	 * 结点容量
	 */
	private int nodeCapacity = -1;

	/**
	 * 结点填充因子
	 */
	private float fillFactor = -1;

	public RTree(int capacity, float fillFactor) {
		this.fillFactor = fillFactor;
		nodeCapacity = capacity;
		root = new RTDataNode(this, Constants.RTNode_NULL);
	}
	
	public RTree(){
		Enc_root=new Enc_RTDataNode(this, Constants.Enc_RTNode_NULL);
	}

	public void setRoot(RTNode root) {
		this.root = root;
	}

	public float getFillFactor() {
		return fillFactor;
	}

	/**
	 * @return 返回结点容量
	 */
	public int getNodeCapacity() {
		return nodeCapacity;
	}
	
	/**
	 * 范围搜索
	 * @param root
	 * @param rectangle需要搜索的范围
	 * @return
	 */
	public List<Rectangle> search(RTNode root,double x1,double y1,double x2,double y2) {
		if (root == null)
			throw new IllegalArgumentException("Node cannot be null.");
		Point p1 = new Point(new double[] { min(x1,x2), min(y1,y2) });
		Point p2 = new Point(new double[] { max(x1,x2), max(y1,y2) });
		Rectangle rectangle = new Rectangle(p1, p2);
		List<Rectangle> list = root.search(rectangle);
		
		return list;
	}

	/**
	 * 向Rtree中插入Rectangle
	 * <p>
	 * 1、先找到合适的叶节点 <br>
	 * 2、再向此叶节点中插入<br>
	 * 
	 * @param rectangle
	 */
	public boolean insert(Rectangle rectangle) {
		if (rectangle == null)
			throw new IllegalArgumentException("Rectangle cannot be null.");

		RTDataNode leaf = root.chooseLeaf(rectangle);

		return leaf.insert(rectangle);
	}

	/**
	 * 从给定的结点root开始遍历所有的结点
	 * 
	 * @param node
	 * @return 所有遍历的结点集合
	 */
	public static List<RTNode> traversePostOrder(RTNode root) {
		if (root == null)
			throw new IllegalArgumentException("Node cannot be null.");

		List<RTNode> list = new ArrayList<RTNode>();
		list.add(root);

		if (!root.isLeaf()) {
			for (int i = 0; i < root.usedSpace; i++) {
				List<RTNode> a = traversePostOrder(((RTDirNode) root).getChild(i));
				for (int j = 0; j < a.size(); j++) {
					list.add(a.get(j));
				}
			}
		}
		return list;
	}
	
	/**
	 * 对加密数，从给定的结点root开始遍历所有的结点
	 * 
	 * @param node
	 * @return 所有遍历的结点集合
	 */
	public List<Enc_RTNode> enc_TraversePostOrder(Enc_RTNode root){
		if (root == null)
			throw new IllegalArgumentException("Node cannot be null.");

		List<Enc_RTNode> list = new ArrayList<Enc_RTNode>();
		list.add(root);

		if (!root.isLeaf()) {
			for (int i = 0; i < root.usedSpace; i++) {
				List<Enc_RTNode> a = enc_TraversePostOrder(((Enc_RTDirNode) root).getChild(i));
				for (int j = 0; j < a.size(); j++) {
					list.add(a.get(j));
				}
			}
		}
		return list;
	}

	/**
	 * 对若干Rectangle进行加密
	 * */
	private Enc_Rectangle[] enc_Rectangles(Rectangle[] datas,boolean isLeaf){
		Node_Encrypty encrypty=new Node_Encrypty();
		Enc_Rectangle[] enc_datas=new Enc_Rectangle[datas.length];
		for(int i=0;i<datas.length;i++){
			enc_datas[i]=encrypty.rectangle_Enc_Index(datas[i],isLeaf);
		}
		return enc_datas;
	}
	
	/**
	 * rtree加密处理
	 * */
	public Enc_RTNode rtree_Encrypty(RTNode root,Enc_RTNode parent){
		Enc_RTNode enc_node;
		if(!root.isLeaf()){
			enc_node=new Enc_RTDirNode(this, parent, root.getLevel());
			Rectangle[] datas=root.getDatas();
			Enc_Rectangle[] enc_datas=enc_Rectangles(root.getDatas(),false);
			enc_node.setEncData(enc_datas);//设定密文数据
			enc_node.setDatas(datas);//设定明文数据
			for(int i=0;i<root.usedSpace;i++){//添加孩子节点
				((Enc_RTDirNode)enc_node).addChild(
						rtree_Encrypty(((RTDirNode) root).getChild(i),enc_node));
			}
		}else{
			enc_node=new Enc_RTDataNode(this, parent);
			Rectangle[] datas=root.getDatas();
			Enc_Rectangle[] enc_datas=enc_Rectangles(root.getDatas(),true);
			enc_node.setEncData(enc_datas);//设定密文数据
			enc_node.setDatas(datas);//设定明文数据
		}
		
		return enc_node;
	}

	/**
	 * 根据已有明文树生成加密数
	 * @param root明文树的根
	 * @return
	 */
	public Enc_RTNode produce_EncRtree(RTNode root){
		Enc_RTNode enc_root=rtree_Encrypty(root,null);
		return enc_root;
	}
	
	/**
	 * 生成树结构（明文形式）
	 * @param file_path 文件路径
	 * @param data_file_name 数据文件名称
	 * @return
	 */
	public static RTree produce_Rtree(String file_path, String data_file_name) {
		RTree tree = new RTree(3, 0.4f);
		// 从文件中获取数据
		List<Position> vals = Tools.getDataInFile(file_path + data_file_name);
		for(int i=0;i<vals.size();i++){
			Point p=new Point(new double[]{vals.get(i).getX(),vals.get(i).getY()});
			final Rectangle rectangle = new Rectangle(p, p);
			tree.insert(rectangle);
		}
		return tree;
	}
	
	private double max(double a,double b){
		return a>b?a:b;
	}
	
	private double min(double a,double b){
		return a<b?a:b;
	}
	
	/**
	 * 密文树的搜索，x1,x2,y1,y2为搜索区域（advance）
	 * @param x1
	 * @param x2
	 * @param y1
	 * @param y2
	 * @return
	 */
	public List<Enc_Rectangle> advance_enc_search(Enc_RTNode enc_root,double x1,double y1,double x2,double y2){
		if (enc_root == null)
			throw new IllegalArgumentException("Node cannot be null.");
		Node_Encrypty encrypty=new Node_Encrypty();
		//生成左下角点
		Point p1 = new Point(new double[] { min(x1,x2), min(y1,y2) });
		//生成右上角点
		Point p2 = new Point(new double[] { max(x1,x2), max(y1,y2) });
		//根据两点生成外包矩阵
		Rectangle rectangle = new Rectangle(p1, p2);
		long b = System.nanoTime();
		//对明文查询矩阵进行加密
		Enc_Rectangle enc_Rectangle=encrypty.rectangle_Enc_Query(rectangle,false);
		//记录trapdoor生成时间
		Constants.ave_advance_enctime=(System.nanoTime() - b) / 1000.0f;
		b = System.currentTimeMillis();
		//执行查询操作
		List<Enc_Rectangle> list = enc_root.search(enc_Rectangle);
		//记录查询执行时间
		Constants.ave_advance_sertime=(System.currentTimeMillis() - b) / 1f;
		//返回查询结果
		return list;
	}
	
	/**
	 * 密文树的搜索，x1,y1,x2,y2为搜索区域（basic）
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public List<Enc_Rectangle> basic_enc_search(Enc_RTNode enc_root,double x1,double y1,double x2,double y2){
		if (enc_root == null)
			throw new IllegalArgumentException("Node cannot be null.");
		Node_Encrypty encrypty=new Node_Encrypty();
		//生成左下角点
		Point p1 = new Point(new double[] { min(x1,x2), min(y1,y2) });
		//生成右上角点
		Point p2 = new Point(new double[] { max(x1,x2), max(y1,y2) });
		//根据两点生成外包矩阵
		Rectangle rectangle = new Rectangle(p1, p2);		
		long b = System.currentTimeMillis();
		//对明文查询矩阵进行加密
		Enc_Rectangle enc_Rectangle=encrypty.rectangle_Enc_Query(rectangle,true);
		//记录trapdoor生成时间
		Constants.ave_basic_enctime=(System.currentTimeMillis() - b) / 1f;
		
		b = System.currentTimeMillis();
		//执行查询操作
		List<Enc_Rectangle> list = enc_root.search(enc_Rectangle);
		//记录查询执行时间
		Constants.ave_basic_sertime=(System.currentTimeMillis() - b) / 1f;

		return list;
	}

	public static void main(String[] args) throws Exception {
		RTree tree = new RTree(3, 0.4f);

		double[] f = {

				1, 0, 9, 8, 7, 6, 2, 3, 1, 3, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 0, 0, 6, 6 };

		// 插入结点
		for (int i = 0; i < f.length;) {
			Point p1 = new Point(new double[] { f[i++], f[i++] });
			final Rectangle rectangle = new Rectangle(p1, p1);
			tree.insert(rectangle);

			Rectangle[] rectangles = tree.root.getDatas();
			System.out.println(tree.root.getLevel());
			for (int j = 0; j < rectangles.length; j++)
				System.out.println(rectangles[j]);
		}
		System.out.println("---------------------------------");
		System.out.println("Insert finished.");

		List<RTNode> rtrees = traversePostOrder(tree.root);
		for (int i = 0; i < rtrees.size(); i++) {
			RTNode node = rtrees.get(i);
			Rectangle[] rectangles = node.getDatas();
			System.out.println("level:" + node.getLevel());

			for (int j = 0; j < rectangles.length; j++)
				System.out.println(rectangles[j]);
		}

		System.out.println("---------------------------------");
		

	}

}