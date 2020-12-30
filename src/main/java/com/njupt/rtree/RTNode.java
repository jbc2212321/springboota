package com.njupt.rtree;  
  
import java.util.List;  
  
import com.njupt.constants.Constants;  

  
/** 
 * @ClassName RTNode  
 * @Description  
 */  
public abstract class RTNode  
{  
    /** 
     * ������ڵ��� 
     */  
    protected RTree rtree;        
      
    /** 
     * ������ڵĲ� 
     */  
    private int level;          
      
    /** 
     * �൱����Ŀ 
     */  
    private Rectangle[] datas;  
      
//  /**  
//   * ��������  
//   */  
//  protected int capacity;  
      
    /** 
     * ���ڵ� 
     */  
    protected RTNode parent;  
      
    /** 
     * ������õĿռ� 
     */  
    protected int usedSpace;  
      
    /** 
     * ��¼���������·������ 
     */  
    protected int insertIndex;  
      
    public RTNode(RTree rtree, RTNode parent, int level)   
    {  
        this.rtree = rtree;  
        this.parent = parent;  
        this.setLevel(level);  
//      this.capacity = capacity;  
        setDatas(new Rectangle[rtree.getNodeCapacity() + 1]);//�����һ�����ڽ�����  
        usedSpace = 0;  
    }  
      
    /** 
     * @return ���ظ��ڵ� 
     */  
    public RTNode getParent()  
    {  
        return parent;  
    }  
      
//  /**  
//   * @return �������  
//   */  
//  public int getNodeCapacity()  
//  {  
//      return capacity;  
//  }  
      
    /** 
     * ���������Rectangle���������Ŀ 
     * @param rectangle 
     */  
    protected void addData(Rectangle rectangle)   
    {  
        if(usedSpace == rtree.getNodeCapacity())  
        {  
            throw new IllegalArgumentException("Node is full.");  
        }  
        getDatas()[usedSpace ++] = rectangle;  
    }    
      
    /** 
     * ���ѽ���ƽ���㷨<p> 
     * 1��Ϊ������ѡ���һ����Ŀ--�����㷨pickSeeds()��Ϊ������ѡ���һ��Ԫ�أ��ֱ��ѡ�е�������Ŀ���䵽�����鵱�С�<br> 
     * 2������Ƿ��Ѿ�������ϣ����һ�����е���Ŀ̫�٣�Ϊ�������磬��ʣ���������Ŀȫ�����䵽������У��㷨��ֹ<br> 
     * 3������pickNext��ѡ����һ�����з������Ŀ--�����ÿ����Ŀ����ÿ����֮�������������ѡ�����������������������Ŀ����, 
     *  ���������������ѡ�������С���飬�����Ҳ�����ѡ����Ŀ�����ٵ���<br> 
     * @param rectangle ���·��ѵ����Rectangle 
     * @return �������е���Ŀ������ 
     */  
    protected int[][] quadraticSplit(Rectangle rectangle)   
    {  
        if(rectangle == null)  
        {  
            throw new IllegalArgumentException("Rectangle cannot be null.");  
        }  
          
        getDatas()[usedSpace] = rectangle;   //����ӽ�ȥ  
//      if(this instanceof RTDirNode)  
//      {  
//          (RTDirNode)(this).children.add()  
//      }  
        int total = usedSpace + 1;      //�������  
          
        //��Ƿ��ʵ���Ŀ  
        int[] mask = new int[total];  
        for(int i = 0; i < total; i ++)  
        {  
            mask[i] = 1;  
        }  
          
        //ÿ����ֻ����total/2����Ŀ  
        int c = total/2 + 1;  
        //ÿ�������С��Ŀ����  
        int minNodeSize = Math.round(rtree.getNodeCapacity() * rtree.getFillFactor()); 
        //����������  
        if(minNodeSize < 2)  
            minNodeSize = 2;  
          
        //��¼û�б�������Ŀ�ĸ���  
        int rem = total;  
          
        int[] group1 = new int[c];//��¼�������Ŀ������  
        int[] group2 = new int[c];//��¼�������Ŀ������  
        //���ٱ�����ÿ�������Ŀ������  
        int i1 = 0, i2 = 0;  
          
        int[] seed = pickSeeds();  
        group1[i1 ++] = seed[0];  
        group2[i2 ++] = seed[1];  
        rem -=2;  
        mask[group1[0]] = -1;  
        mask[group2[0]] = -1;  
          
        while(rem > 0)  
        {  
            //��ʣ���������Ŀȫ�����䵽group1���У��㷨��ֹ  
            if(minNodeSize - i1 == rem)  
            {  
                for(int i = 0; i < total; i ++)//�ܹ�rem��  
                {  
                    if(mask[i] != -1)//��û�б�����  
                    {  
                        group1[i1 ++] = i;  
                        mask[i] = -1;  
                        rem --;  
                    }  
                }  
            //��ʣ���������Ŀȫ�����䵽group2���У��㷨��ֹ  
            }else if(minNodeSize - i2 == rem)  
            {  
                for(int i = 0; i < total; i ++)//�ܹ�rem��  
                {  
                    if(mask[i] != -1)//��û�б�����  
                    {  
                        group2[i2 ++] = i;  
                        mask[i] = -1;  
                        rem --;  
                    }  
                }  
            }else  
            {  
                //��group1��������Ŀ����С�������  
                Rectangle mbr1 = (Rectangle) getDatas()[group1[0]].clone();  
                for(int i = 1; i < i1; i ++)  
                {  
                    mbr1 = mbr1.getUnionRectangle(getDatas()[group1[i]]);  
                }  
                //��group2��������Ŀ���������  
                Rectangle mbr2 = (Rectangle) getDatas()[group2[0]].clone();  
                for(int i = 1; i < i2; i ++)  
                {  
                    mbr2 = mbr2.getUnionRectangle(getDatas()[group2[i]]);  
                }  
                  
                //�ҳ���һ�����з������Ŀ  
                double dif = Double.NEGATIVE_INFINITY;  
                double areaDiff1 = 0, areaDiff2 = 0;  
                double diff1=0,diff2=0;
                int sel = -1;                 
                for(int i = 0; i < total; i ++)  
                {  
                    if(mask[i] != -1)//��û�б��������Ŀ  
                    {  
                        //�����ÿ����Ŀ����ÿ����֮�������������ѡ�����������������������Ŀ����  
                        Rectangle a = mbr1.getUnionRectangle(getDatas()[i]);  
                        areaDiff1 = a.getArea() - mbr1.getArea();  
                          
                        Rectangle b = mbr2.getUnionRectangle(getDatas()[i]);  
                        areaDiff2 = b.getArea() - mbr2.getArea();  
                          
                        if(Math.abs(areaDiff1 - areaDiff2) > dif)  
                        {  
                            dif = Math.abs(areaDiff1 - areaDiff2);  
                            sel = i;  
                            diff1=areaDiff1;
                            diff2=areaDiff2;
                        }  
                    }  
                }  
                  
                if(diff1 < diff2)//�ȱȽ��������  
                {  
                    group1[i1 ++] = sel;  
                }else if(diff1 > diff2)  
                {  
                    group2[i2 ++] = sel;  
                }else if(mbr1.getArea() < mbr2.getArea())//�ٱȽ��������  
                {  
                    group1[i1 ++] = sel;  
                }else if(mbr1.getArea() > mbr2.getArea())  
                {  
                    group2[i2 ++] = sel;  
                }else if(i1 < i2)//���Ƚ���Ŀ����  
                {  
                    group1[i1 ++] = sel;  
                }else if(i1 > i2)  
                {  
                    group2[i2 ++] = sel;  
                }else {  
                    group1[i1 ++] = sel;  
                }  
                mask[sel] = -1;  
                rem --;  
                  
            }  
        }//end while  
          
          
        int[][] ret = new int[2][];  
        ret[0] = new int[i1];  
        ret[1] = new int[i2];  
          
        for(int i = 0; i < i1; i ++)  
        {  
            ret[0][i] = group1[i];  
        }  
        for(int i = 0; i < i2; i ++)  
        {  
            ret[1][i] = group2[i];  
        }  
        return ret;  
    }  
      
      
    /** 
     * 1����ÿһ����ĿE1��E2�������Χ���ǵ�Rectangle J������d = area(J) - area(E1) - area(E2);<br> 
     * 2��Choose the pair with the largest d 
     * @return ����������Ŀ�������һ�������������ռ����Ŀ���� 
     */  
    protected int[] pickSeeds()   
    {  
        double inefficiency = Double.NEGATIVE_INFINITY;  
        int i1 = 0, i2 = 0;  
          
        //  
        for(int i = 0; i < usedSpace; i ++)  
        {  
            for(int j = i + 1; j <= usedSpace; j ++)//ע��˴���jֵ  
            {  
                Rectangle rectangle = getDatas()[i].getUnionRectangle(getDatas()[j]);  
                double d = rectangle.getArea() - getDatas()[i].getArea() - getDatas()[j].getArea();  
                  
                if(d > inefficiency)  
                {  
                    inefficiency = d;  
                    i1 = i;  
                    i2 = j;  
                }  
            }  
        }  
        return new int[]{i1, i2};  
    }  
  
    /** 
     * @return ���ذ��������������Ŀ����СRectangle 
     */  
    public Rectangle getNodeRectangle()  
    {  
        if(usedSpace > 0)  
        {  
            Rectangle[] rectangles = new Rectangle[usedSpace];  
            System.arraycopy(getDatas(), 0, rectangles, 0, usedSpace);  
            return Rectangle.getUnionRectangle(rectangles);  
        }else {  
            return new Rectangle(new Point(new double[]{0,0}), new Point(new double[]{0,0}));  
        }  
    }  
      
    /** 
     * @return �Ƿ���ڵ� 
     */  
    public boolean isRoot()  
    {  
        return (parent == Constants.RTNode_NULL);  
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
  
      
    /** 
     * <b>����CL1��</b>��ʼ��������R���ĸ��ڵ�ΪN��<br> 
     * <b>����CL2��</b>���Ҷ�ڵ㡪�����N�Ǹ�Ҷ�ڵ㣬����N<br> 
     * <b>����CL3��</b>ѡ�������������N����Ҷ�ڵ㣬���N�����е���Ŀ��ѡ��һ����ѵ���ĿF�� 
     *      ѡ��ı�׼�ǣ����E����F��F����������FI������С����F������ѵ���Ŀ����������� 
     *      ��Ŀ�ڼ���E���������ε����ų̶���ȣ�������������ѡ���������ν�С���Ǹ���<br> 
     * <b>����CL4��</b>����Ѱ��ֱ���ﵽҶ�ڵ㡪����Fpָ��ĺ��ӽڵ�ΪN��Ȼ�󷵻ز���CL2ѭ�����㣬 
     *      ֱ�����ҵ�Ҷ�ڵ㡣<p> 
     * @param Rectangle  
     * @return RTDataNode 
     */  
    protected abstract RTDataNode chooseLeaf(Rectangle rectangle);  
      
    /** 
     * R���ĸ��ڵ�ΪT�����Ұ���rectangle��Ҷ�ӽ��<p> 
     * 1�����T����Ҷ�ӽ�㣬���������T�е�ÿ����Ŀ�Ƿ��Χrectangle������Χ��ݹ����findLeaf()<br> 
     * 2�����T��һ��Ҷ�ӽ�㣬��������T�е�ÿ����Ŀ�ܷ�ƥ��rectangle<br> 
     * @param rectangle 
     * @return ���ذ���rectangle��Ҷ�ڵ� 
     */  
    //protected abstract RTDataNode findLeaf(Rectangle rectangle);  
    
    /**
     * ����TΪһ��R���ĸ���㣬����������������S���ǵļ�¼��Ŀ��<p> 
     * S1:[��������] ���T�Ƿ�Ҷ�ӽ�㣬���T����Ӧ�ľ�����S���غϣ���ô�������T�д洢����Ŀ������������Щ��Ŀ��ʹ��Search����������ÿһ����Ŀ��ָ��������ĸ�����ϣ���T���ĺ��ӽ�㣩��<br> 
     * S2:[����Ҷ�ӽ��] ���T��Ҷ�ӽ�㣬���T����Ӧ�ľ�����S���غϣ���ôֱ�Ӽ��S��ָ������м�¼��Ŀ�����ط��������ļ�¼��<br> 
     * @param rectangle
     * @return
     */
    protected abstract List<Rectangle> search(Rectangle rectangle);

	public Rectangle[] getDatas() {
		return datas;
	}

	public void setDatas(Rectangle[] datas) {
		this.datas = datas;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
      
}  