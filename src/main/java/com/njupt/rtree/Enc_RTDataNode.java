package com.njupt.rtree;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *加密后的数据节点
 */
public class Enc_RTDataNode extends Enc_RTNode{

	
	
	public Enc_RTDataNode(RTree rtree, Enc_RTNode parent) {
		super(rtree, parent, 0);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setEncData(Enc_Rectangle[] datas){
		for(int i=0;i<datas.length;i++){
			if(datas[i]!=null){
				addEncData(datas[i]);
			}
		}
	}

	@Override
	protected List<Enc_Rectangle> search(Enc_Rectangle rectangle) {
		// TODO Auto-generated method stub
		List<Enc_Rectangle> list=new ArrayList<Enc_Rectangle>();
		for(int i=0;i<usedSpace;i++){
			if(rectangle.enclosure(getEnc_Datas()[i])){
				list.add(getEnc_Datas()[i]);
			}
		}
		return list;
	}

	
	
	

}
