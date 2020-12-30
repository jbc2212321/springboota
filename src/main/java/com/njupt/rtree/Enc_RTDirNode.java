package com.njupt.rtree;

import java.util.ArrayList;
import java.util.List;

import com.encrypty.splitedMatrix;

/**
 * 数据加密后的非叶节点 
 *
 */
public class Enc_RTDirNode extends Enc_RTNode{

	/** 
     * 孩子结点 
     */  
    private List<Enc_RTNode> children; 
	
	public Enc_RTDirNode(RTree rtree, Enc_RTNode parent, int level) {
		super(rtree, parent, level);
		children = new ArrayList<Enc_RTNode>();  
	}

	/** 
     * @param index 
     * @return 对应索引下的孩子结点 
     */  
    public Enc_RTNode getChild(int index)  
    {  
        return children.get(index);  
    }
    
    /**
     * 添加孩子节点
     * @param node
     */
    public void addChild(Enc_RTNode node){
    	children.add(node);
    }
	
	@Override
	protected List<Enc_Rectangle> search(Enc_Rectangle rectangle) {
		// TODO Auto-generated method stub
		List<Enc_Rectangle> list=new ArrayList<Enc_Rectangle>();
    	for(int i = 0; i < usedSpace; i ++)  
        {  
            if(rectangle.isIntersection(getEnc_Datas()[i]))  
            {  
            	Enc_RTNode node=children.get(i);
            	List<Enc_Rectangle> a=node.search(rectangle);
            	for(int j = 0; j < a.size(); j ++)  
                {  
                     list.add(a.get(j));  
                }  
            }  
        }  
    	return list;
	}

	@Override
	protected void setEncData(Enc_Rectangle[] datas) {
		// TODO Auto-generated method stub
		for(int i=0;i<datas.length;i++){
			if(datas[i]!=null){
				addEncData(datas[i]);
			}
		}
	}



}
