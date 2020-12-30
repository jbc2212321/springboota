package com.njupt.rtree;

import java.util.List;

import com.encrypty.splitedMatrix;
import com.njupt.constants.Constants;

public abstract class Enc_RTNode {

	/** 
     * ������ڵ��� 
     */  
    protected RTree rtree;        
      
    /** 
     * ������ڵĲ� 
     */  
    private int level; 
    
    /** 
     * ���ڵ� 
     */  
    protected Enc_RTNode parent;
	
	/**
	 * ���ܺ������
	 * */
	private Enc_Rectangle enc_datas[];
	/** 
     * ������õĿռ� 
     */  
    protected int usedSpace; 
	
	public int getUsedSpace() {
		return usedSpace;
	}

	/** 
     *��������
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
     * @return �Ƿ���ڵ� 
     */  
    public boolean isRoot()  
    {  
        return (parent == Constants.Enc_RTNode_NULL);  
    }  
    
    /** 
     * @return �Ƿ��Ҷ�ӽ�� 
     */  
    public boolean isIndex()  
    {  
        return (getLevel() != 0);  
    }  
      
	/** 
     * @return �Ƿ�Ҷ�ӽ�� 
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
     * ����TΪһ��R���ĸ���㣬����������������S���ǵļ�¼��Ŀ��<p> 
     * S1:[��������] ���T�Ƿ�Ҷ�ӽ�㣬���T����Ӧ�ľ�����S���غϣ���ô�������T�д洢����Ŀ������������Щ��Ŀ��ʹ��Search����������ÿһ����Ŀ��ָ��������ĸ�����ϣ���T���ĺ��ӽ�㣩��<br> 
     * S2:[����Ҷ�ӽ��] ���T��Ҷ�ӽ�㣬���T����Ӧ�ľ�����S���غϣ���ôֱ�Ӽ��S��ָ������м�¼��Ŀ�����ط��������ļ�¼��<br> 
     * @param rectangle
     * @return
     */
    protected abstract List<Enc_Rectangle> search(Enc_Rectangle rectangle);

    protected abstract void setEncData(Enc_Rectangle[] datas);
}
