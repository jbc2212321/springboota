package com.njupt.rtree;

import java.util.List;

import com.encrypty.splitedMatrix;
import com.njupt.constants.Constants;

public abstract class Enc_RTNode {

	/** 
     * 结点所在的树 
     */  
    protected RTree rtree;        
      
    /** 
     * 结点所在的层 
     */  
    private int level; 
    
    /** 
     * 父节点 
     */  
    protected Enc_RTNode parent;
	
	/**
	 * 加密后的数据
	 * */
	private Enc_Rectangle enc_datas[];
	/** 
     * 结点已用的空间 
     */  
    protected int usedSpace; 
	
	public int getUsedSpace() {
		return usedSpace;
	}

	/** 
     *明文数据
     */  
    private Rectangle[] datas;  
	
	public Rectangle[] getDatas() {
		return datas;
	}

	protected void addEncData(Enc_Rectangle data){
		getEnc_Datas()[usedSpace++]=data;
	}

	public void setDatas(Rectangle[] datas) {
		this.datas = datas;
	}


	public Enc_RTNode(RTree rtree, Enc_RTNode parent, int level){
		this.rtree=rtree;
		this.parent=parent;
		this.level=level;
		setEnc_Datas(new Enc_Rectangle[rtree.getNodeCapacity()]);
		usedSpace = 0;  
	}
	
	
	public Enc_RTNode getParent() {
		return parent;
	}

	private void setEnc_Datas(Enc_Rectangle[] datas) {
		this.enc_datas = datas;
	}

	/** 
     * @return 是否根节点 
     */  
    public boolean isRoot()  
    {  
        return (parent == Constants.Enc_RTNode_NULL);  
    }  
    
    /** 
     * @return 是否非叶子结点 
     */  
    public boolean isIndex()  
    {  
        return (getLevel() != 0);  
    }  
      
	/** 
     * @return 是否叶子结点 
     */  
    public boolean isLeaf()  
    {  
        return (getLevel() == 0);  
    }  
    
    public int getLevel() {
  		// TODO Auto-generated method stub
  		return level;
  	}
    
    public Enc_Rectangle[] getEnc_Datas() {
		return enc_datas;
	}
    
    /**
     * 假设T为一棵R树的根结点，查找所有搜索矩形S覆盖的记录条目。<p> 
     * S1:[查找子树] 如果T是非叶子结点，如果T所对应的矩形与S有重合，那么检查所有T中存储的条目，对于所有这些条目，使用Search操作作用在每一个条目所指向的子树的根结点上（即T结点的孩子结点）。<br> 
     * S2:[查找叶子结点] 如果T是叶子结点，如果T所对应的矩形与S有重合，那么直接检查S所指向的所有记录条目。返回符合条件的记录。<br> 
     * @param rectangle
     * @return
     */
    protected abstract List<Enc_Rectangle> search(Enc_Rectangle rectangle);

    protected abstract void setEncData(Enc_Rectangle[] datas);
}
