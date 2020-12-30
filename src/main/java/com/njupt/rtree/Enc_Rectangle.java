package com.njupt.rtree;

import com.encrypty.Query;

public class Enc_Rectangle {

	private Enc_Point low;  
    private Enc_Point high;  
	
    public Enc_Rectangle(Enc_Point low){
    	this.low=low;
    	this.high=null;
    }
    
    public Enc_Rectangle(Enc_Point low, Enc_Point high){
    	this.low=low;
    	this.high=high;
    }
    
    /** 
     * 返回Rectangle左下角的Point 
     * @return Point 
     */  
    public Enc_Point getLow()   
    {  
        return low;  
    }  
  
    /** 
     * 返回Rectangle右上角的Point 
     * @return Point 
     */  
    public Enc_Point getHigh()   
    {  
        return high;  
    }  
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	if(high==null){
    		return "Point:"+low;
    	}else{
    		return "Rectangle Low:" + low + " High:" + high;
    	}
    }
    
    /** 
     * @return 返回Rectangle的维度 
     */  
    private int getDimension()   
    {  
        return low.getDimension();  
    }  
    
    /** 
     * @param rectangle 
     * @return 判断两个Rectangle是否相交 
     */  
    public boolean isIntersection(Enc_Rectangle rectangle)  
    {  
        if(rectangle == null)  
            throw new IllegalArgumentException("Rectangle cannot be null.");  
        
        for(int i = 0; i < getDimension(); i ++)  
        {  
        	if((Query.Compare( rectangle.high.getCoordinate(i),low.getCoordinate(i))>0)||
        			(Query.Compare(rectangle.low.getCoordinate(i),high.getCoordinate(i))<0)){
        		return false;
        	}
        }  
        return true;  
    } 
    
    /** 
     * 当rectangle为点时，判断rectangle是否被包围 
     * @param rectangle 
     * @return 
     */  
    public boolean enclosure(Enc_Rectangle rectangle)   
    {  
        if(rectangle == null)  
            throw new IllegalArgumentException("Rectangle cannot be null.");  
        if(rectangle.high!=null){
        	throw new IllegalArgumentException("Rectangle not a point.");
        }
        for(int i = 0; i < getDimension(); i ++)  
        {  
        	if(Query.Compare(rectangle.low.getCoordinate(i),low.getCoordinate(i))>0||
        			Query.Compare(rectangle.low.getCoordinate(i),high.getCoordinate(i))<0){
        		return false;  
        	} 
        }  
        return true;  
    } 
    
}
