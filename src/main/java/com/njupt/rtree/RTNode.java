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
     * 结点所在的树 
     */  
    protected RTree rtree;        
      
    /** 
     * 结点所在的层 
     */  
    private int level;          
      
    /** 
     * 相当于条目 
     */  
    private Rectangle[] datas;  
      
//  /**  
//   * 结点的容量  
//   */  
//  protected int capacity;  
      
    /** 
     * 父节点 
     */  
    protected RTNode parent;  
      
    /** 
     * 结点已用的空间 
     */  
    protected int usedSpace;  
      
    /** 
     * 记录插入的搜索路径索引 
     */  
    protected int insertIndex;  
      
    public RTNode(RTree rtree, RTNode parent, int level)   
    {  
        this.rtree = rtree;  
        this.parent = parent;  
        this.setLevel(level);  
//      this.capacity = capacity;  
        setDatas(new Rectangle[rtree.getNodeCapacity() + 1]);//多出的一个用于结点分裂  
        usedSpace = 0;  
    }  
      
    /** 
     * @return 返回父节点 
     */  
    public RTNode getParent()  
    {  
        return parent;  
    }  
      
//  /**  
//   * @return 结点容量  
//   */  
//  public int getNodeCapacity()  
//  {  
//      return capacity;  
//  }  
      
    /** 
     * 向结点中添加Rectangle，即添加条目 
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
     * 分裂结点的平方算法<p> 
     * 1、为两个组选择第一个条目--调用算法pickSeeds()来为两个组选择第一个元素，分别把选中的两个条目分配到两个组当中。<br> 
     * 2、检查是否已经分配完毕，如果一个组中的条目太少，为避免下溢，将剩余的所有条目全部分配到这个组中，算法终止<br> 
     * 3、调用pickNext来选择下一个进行分配的条目--计算把每个条目加入每个组之后面积的增量，选择两个组面积增量差最大的条目索引, 
     *  如果面积增量相等则选择面积较小的组，若面积也相等则选择条目数更少的组<br> 
     * @param rectangle 导致分裂的溢出Rectangle 
     * @return 两个组中的条目的索引 
     */  
    protected int[][] quadraticSplit(Rectangle rectangle)   
    {  
        if(rectangle == null)  
        {  
            throw new IllegalArgumentException("Rectangle cannot be null.");  
        }  
          
        getDatas()[usedSpace] = rectangle;   //先添加进去  
//      if(this instanceof RTDirNode)  
//      {  
//          (RTDirNode)(this).children.add()  
//      }  
        int total = usedSpace + 1;      //结点总数  
          
        //标记访问的条目  
        int[] mask = new int[total];  
        for(int i = 0; i < total; i ++)  
        {  
            mask[i] = 1;  
        }  
          
        //每个组只是有total/2个条目  
        int c = total/2 + 1;  
        //每个结点最小条目个数  
        int minNodeSize = Math.round(rtree.getNodeCapacity() * rtree.getFillFactor()); 
        //至少有两个  
        if(minNodeSize < 2)  
            minNodeSize = 2;  
          
        //记录没有被检查的条目的个数  
        int rem = total;  
          
        int[] group1 = new int[c];//记录分配的条目的索引  
        int[] group2 = new int[c];//记录分配的条目的索引  
        //跟踪被插入每个组的条目的索引  
        int i1 = 0, i2 = 0;  
          
        int[] seed = pickSeeds();  
        group1[i1 ++] = seed[0];  
        group2[i2 ++] = seed[1];  
        rem -=2;  
        mask[group1[0]] = -1;  
        mask[group2[0]] = -1;  
          
        while(rem > 0)  
        {  
            //将剩余的所有条目全部分配到group1组中，算法终止  
            if(minNodeSize - i1 == rem)  
            {  
                for(int i = 0; i < total; i ++)//总共rem个  
                {  
                    if(mask[i] != -1)//还没有被分配  
                    {  
                        group1[i1 ++] = i;  
                        mask[i] = -1;  
                        rem --;  
                    }  
                }  
            //将剩余的所有条目全部分配到group2组中，算法终止  
            }else if(minNodeSize - i2 == rem)  
            {  
                for(int i = 0; i < total; i ++)//总共rem个  
                {  
                    if(mask[i] != -1)//还没有被分配  
                    {  
                        group2[i2 ++] = i;  
                        mask[i] = -1;  
                        rem --;  
                    }  
                }  
            }else  
            {  
                //求group1中所有条目的最小外包矩形  
                Rectangle mbr1 = (Rectangle) getDatas()[group1[0]].clone();  
                for(int i = 1; i < i1; i ++)  
                {  
                    mbr1 = mbr1.getUnionRectangle(getDatas()[group1[i]]);  
                }  
                //求group2中所有条目的外包矩形  
                Rectangle mbr2 = (Rectangle) getDatas()[group2[0]].clone();  
                for(int i = 1; i < i2; i ++)  
                {  
                    mbr2 = mbr2.getUnionRectangle(getDatas()[group2[i]]);  
                }  
                  
                //找出下一个进行分配的条目  
                double dif = Double.NEGATIVE_INFINITY;  
                double areaDiff1 = 0, areaDiff2 = 0;  
                double diff1=0,diff2=0;
                int sel = -1;                 
                for(int i = 0; i < total; i ++)  
                {  
                    if(mask[i] != -1)//还没有被分配的条目  
                    {  
                        //计算把每个条目加入每个组之后面积的增量，选择两个组面积增量差最大的条目索引  
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
                  
                if(diff1 < diff2)//先比较面积增量  
                {  
                    group1[i1 ++] = sel;  
                }else if(diff1 > diff2)  
                {  
                    group2[i2 ++] = sel;  
                }else if(mbr1.getArea() < mbr2.getArea())//再比较自身面积  
                {  
                    group1[i1 ++] = sel;  
                }else if(mbr1.getArea() > mbr2.getArea())  
                {  
                    group2[i2 ++] = sel;  
                }else if(i1 < i2)//最后比较条目个数  
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
     * 1、对每一对条目E1和E2，计算包围它们的Rectangle J，计算d = area(J) - area(E1) - area(E2);<br> 
     * 2、Choose the pair with the largest d 
     * @return 返回两个条目如果放在一起会有最多的冗余空间的条目索引 
     */  
    protected int[] pickSeeds()   
    {  
        double inefficiency = Double.NEGATIVE_INFINITY;  
        int i1 = 0, i2 = 0;  
          
        //  
        for(int i = 0; i < usedSpace; i ++)  
        {  
            for(int j = i + 1; j <= usedSpace; j ++)//注意此处的j值  
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
     * @return 返回包含结点中所有条目的最小Rectangle 
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
     * @return 是否根节点 
     */  
    public boolean isRoot()  
    {  
        return (parent == Constants.RTNode_NULL);  
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
  
      
    /** 
     * <b>步骤CL1：</b>初始化——记R树的根节点为N。<br> 
     * <b>步骤CL2：</b>检查叶节点——如果N是个叶节点，返回N<br> 
     * <b>步骤CL3：</b>选择子树——如果N不是叶节点，则从N中所有的条目中选出一个最佳的条目F， 
     *      选择的标准是：如果E加入F后，F的外廓矩形FI扩张最小，则F就是最佳的条目。如果有两个 
     *      条目在加入E后外廓矩形的扩张程度相等，则在这两者中选择外廓矩形较小的那个。<br> 
     * <b>步骤CL4：</b>向下寻找直至达到叶节点——记Fp指向的孩子节点为N，然后返回步骤CL2循环运算， 
     *      直至查找到叶节点。<p> 
     * @param Rectangle  
     * @return RTDataNode 
     */  
    protected abstract RTDataNode chooseLeaf(Rectangle rectangle);  
      
    /** 
     * R树的根节点为T，查找包含rectangle的叶子结点<p> 
     * 1、如果T不是叶子结点，则逐个查找T中的每个条目是否包围rectangle，若包围则递归调用findLeaf()<br> 
     * 2、如果T是一个叶子结点，则逐个检查T中的每个条目能否匹配rectangle<br> 
     * @param rectangle 
     * @return 返回包含rectangle的叶节点 
     */  
    //protected abstract RTDataNode findLeaf(Rectangle rectangle);  
    
    /**
     * 假设T为一棵R树的根结点，查找所有搜索矩形S覆盖的记录条目。<p> 
     * S1:[查找子树] 如果T是非叶子结点，如果T所对应的矩形与S有重合，那么检查所有T中存储的条目，对于所有这些条目，使用Search操作作用在每一个条目所指向的子树的根结点上（即T结点的孩子结点）。<br> 
     * S2:[查找叶子结点] 如果T是叶子结点，如果T所对应的矩形与S有重合，那么直接检查S所指向的所有记录条目。返回符合条件的记录。<br> 
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