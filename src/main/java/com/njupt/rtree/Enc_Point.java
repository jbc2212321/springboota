package com.njupt.rtree;

import com.encrypty.splitedMatrix;

public class Enc_Point {

	private splitedMatrix[] data;
	
	public Enc_Point(splitedMatrix[] data){
		if(data == null)  
        {  
            throw new IllegalArgumentException("Coordinates cannot be null.");  
        }  
        if(data.length < 2)  
        {  
            throw new IllegalArgumentException("Point dimension should be greater than 1.");  
        } 
        
		this.data = new splitedMatrix[data.length];  
        System.arraycopy(data, 0, this.data, 0, data.length);  
	}
	
	/** 
     * @return 返回Point的维度 
     */  
    public int getDimension()   
    {  
        return data.length;  
    }
    
    /** 
     * @param index 
     * @return 返回Point坐标第i位的加密数据值 
     */  
    public splitedMatrix getCoordinate(int index)   
    {  
        return data[index];  
    } 
	
	@Override
	public String toString() {

        StringBuffer sBuffer = new StringBuffer("{");           
        for(int i = 0 ; i < data.length - 1 ; i ++)  
        {  
            sBuffer.append(data[i]).append(",");  
        }           
        sBuffer.append(data[data.length - 1]).append("}");           
        return sBuffer.toString();
	}
}
